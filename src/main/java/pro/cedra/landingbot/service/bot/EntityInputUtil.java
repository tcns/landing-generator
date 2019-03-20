package pro.cedra.landingbot.service.bot;

import org.apache.commons.beanutils.BeanUtils;
import pro.cedra.landingbot.anno.Cast;
import pro.cedra.landingbot.anno.Step;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class EntityInputUtil<T> {

    Map<Integer, Field> stepFieldMap = new HashMap<>();
    T object;

    Map<Integer, Function<String, Object>> customMappings = new HashMap<>();

    public void registerCustomMapping(Integer step, Function<String, Object> method) {
        customMappings.put(step, method);
    }

    private void register (T entity) {
        this.object = entity;
        Class<T> tClass = (Class<T>) object.getClass();
        List<Field> fields = Arrays.asList(tClass.getDeclaredFields());
        fields.forEach(field -> {
            Step step = field.getAnnotation(Step.class);
            if (step != null) {
                stepFieldMap.put(step.value(), field);
            }
        });


    }
    public void setField(Integer step, String value, T entity) throws Exception {
        register(entity);
        Field field = stepFieldMap.get(step);
        try {
            Object valueToSet = value;
            if (customMappings.get(step) != null) {
                valueToSet = customMappings.get(step).apply(value);
            } else if (field.getAnnotation(Cast.class)!=null) {
                valueToSet = field.getAnnotation(Cast.class).value().cast().apply(value);
            }
            BeanUtils.setProperty(entity, field.getName(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
