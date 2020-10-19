package org.example.user.service.impl;

import com.github.javafaker.Faker;
import org.example.common.utils.StringUtils;
import org.example.user.model.User;
import org.example.user.repository.UserDao;
import org.example.user.repository.UserRepository;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApplicationContext applicationContext;

    private UserService self;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @PostConstruct
    void init() {
        self = applicationContext.getBean(UserServiceImpl.class);
        self.generateMockUsers();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void generateMockUsers() {
        Faker faker = new Faker();
        int count = 0;

        for(int i = 0; i < 100000; i++) {
            userDao.persist(new User((long) atomicInteger.incrementAndGet(), faker.name().username(), faker.name().firstName(), faker.name().lastName(),
                    faker.internet().emailAddress(), faker.phoneNumber().phoneNumber(), StringUtils.generateRandomString(10)));

            count++;
            if ( (count % 50) == 0) {
                em.flush();
                em.clear();
            }
        }
    }

}
