package org.example.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    private String name;

    private String lastName;

    private String email;

    private String mobile;

    private String deviceId;

    public User(String username, String name, String lastName, String email, String mobile, String deviceId) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.deviceId = deviceId;
    }
}