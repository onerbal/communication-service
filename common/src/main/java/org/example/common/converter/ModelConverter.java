package org.example.common.converter;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelConverter {

    static ModelMapper modelMapper = new ModelMapper();

    public static Object convert(Object src, Class dest) {
        if(null == src)
            return null;

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        if(src instanceof List) {
            return ((List) src).stream().map(obj -> ModelConverter.convert(obj, dest)).collect(Collectors.toList());
        }
        else if(src instanceof Set) {
            return ((Set) src).stream().map(obj -> ModelConverter.convert(obj, dest)).collect(Collectors.toSet());
        }
        return modelMapper.map(src, dest);
    }

    public static void map(Object src, Object dest, boolean excludeNullProperties) {
        if(null == src)
            return;

        if(excludeNullProperties) {
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(src, dest);
        } else {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.map(src, dest);
        }
    }
}
