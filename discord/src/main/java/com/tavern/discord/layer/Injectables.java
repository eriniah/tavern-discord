package com.tavern.discord.layer;

import com.tavern.utilities.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

public interface Injectables {

    <T> T get(Class<T> type);

    static Builder builder() {
        return new Builder();
    }

    final class Builder {
        private final Map<Class<?>, InjectablesImpl.Injectable<?>> injectables;

        public Builder() {
            this.injectables = new HashMap<>();
        }

        public <T> Builder add(Class<T> type) {
            injectables.put(type, new InjectablesImpl.Injectable<>(type, () -> {
                try {
                    Constructor<T> constructor = type.getConstructor();
                    return constructor.newInstance();
                } catch (NoSuchMethodException | InstantiationException |
                         InvocationTargetException | IllegalAccessException ex) {
                    throw new UnsupportedOperationException("Failed to instantiate type " + type.getName(), ex);
                }
            }));
            return this;
        }

        public <T> Builder add(Class<T> type, Supplier<T> supplier) {
            injectables.put(type, new InjectablesImpl.Injectable<>(type, supplier));
            return this;
        }

        public Injectables build() {
            return new InjectablesImpl(injectables);
        }

        private final class InjectablesImpl implements Injectables {
            private final Map<Class<?>, Injectable<?>> injectables;

            private InjectablesImpl(Map<Class<?>, Injectable<?>> injectables) {
                this.injectables = CollectionUtils.wrapIfPresent(injectables, HashMap::new);
            }

            @SuppressWarnings("unchecked")
            public <T> T get(Class<T> type) {
                Injectable<T> injectable = (Injectable<T>) injectables.get(type);
                if (null == injectable) {
                    return null;
                }
                return injectable.getInstance();
            }

            private static class Injectable<T> {
                private final Supplier<T> supplier;
                private final Class<T> type;
                private T instance;

                public Injectable(Class<T> type, Supplier<T> supplier) {
                    this.type = type;
                    this.supplier = supplier;
                }

                public T getInstance() {
                    if (null == instance) {
                        instance = supplier.get();
                    }
                    return instance;
                }

                public Class<T> getType() {
                    return type;
                }
            }
        }
    }
}
