package loftily.core;

import loftily.utils.client.ClassUtils;
import loftily.utils.client.ClientUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractManager<T> extends ArrayList<T> {
    public AbstractManager(String childPackage, Class<T> superClass) {
        if (childPackage == null) return;
        
        List<Class<?>> classes = ClassUtils.resolvePackage(String.format("%s.%s", this.getClass().getPackage().getName(), childPackage));
        
        for (Class<?> clazz : classes) {
            try {
                if (superClass.isAssignableFrom(clazz)) {
                    add((T) clazz.getDeclaredConstructor().newInstance());
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    public <V extends T> V get(Class<V> clazz) {
        return (V) this.stream()
                .filter(item -> item.getClass() == clazz)
                .findFirst()
                .orElseGet(() -> {
                    ClientUtils.Logger.error("Item {} is null", clazz.getSimpleName());
                    return null;
                });
    }
}
