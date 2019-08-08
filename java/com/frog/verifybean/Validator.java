package com.frog.verifybean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

/**
 * ClassName: Validator
 * Description: desc
 * Date: 2019/8/6 15:24
 *
 * @author bufflu
 */
public class Validator {

    private static final List<Class> primitiveClass = Arrays.asList(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
    );

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static void verify(Object bean) {
        verify(bean, Content.getInstance(null, VerifiableLevel.NOTNULL, bean.getClass().getClassLoader(), VerifiablePropagation.RECURSIVE));
    }

    private static void verify(Object bean, Content content) {
        // 0 Bean not null is default verify way.
        ifThrowException(() -> bean == null, content.getName() != null ? content.getName() + " is null." : "The Bean is null.");

        VerifiableLevel level = content.getLevel(); // used parent level
        List<String> exclude = new ArrayList<>();
        Class<?> customClass = Class.class;
        VerifiablePropagation propagation = content.getPropagation();
        Class<?> beanClass = bean.getClass();

        // if propagation is current, only verify current Bean's field.
        if (VerifiablePropagation.CURRENT.equals(content.getPropagation()) && beanClass != String.class) return;


        // 1 Get bean's annotations
        VerifiableBean vBAnnotations = beanClass.getDeclaredAnnotation(VerifiableBean.class);
        if (vBAnnotations != null) {
            level = vBAnnotations.level();
            exclude = Arrays.asList(vBAnnotations.exclude());
            customClass = vBAnnotations.customClass();
            propagation = vBAnnotations.propagation();
        }


        // 2 Handle custom verify
        if (VerifiableLevel.CUSTOM.equals(level)) {
            handleCustomVerify(customClass, bean);
        }

        String name = content.getName() != null ? content.getName() + " -> " + "Class@" + beanClass.getName() : "Class@" + beanClass.getName();
        Content content0 = Content.getInstance(name, level, beanClass.getClassLoader(), propagation);

        if (beanClass == String.class) {
            if (VerifiableLevel.NOTEMTRY.equals(level)) {
                ifThrowException(() -> isEmpty((String) bean), name + " is empty.");

            } else if (VerifiableLevel.NOTBLANK.equals(level)) {
                ifThrowException(() -> isBlank((String) bean), name + " is blank.");

            }

        } else if (List.class.isAssignableFrom(beanClass)) {
            for (int i = 0; i < ((List) bean).size(); i++) {
                verify(((List) bean).get(i), content0.getInstance(name + " Element@[" + i + "]"));
            }

        } else if (!beanClass.isPrimitive() && !primitiveClass.contains(beanClass)
                && content.getClassLoader().equals(beanClass.getClassLoader())) {
            Field[] fields = beanClass.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (exclude.contains(field.getName())) continue;
                    field.setAccessible(true);
                    Object fieldValue = field.get(bean);
                    verify(fieldValue, content0.getInstance(name + " Field@" + field.getName()));
                }
            } catch (Exception e) {
                throw new ValidatorException(e.getMessage());
            }
        }

    }


    private static void ifThrowException(Supplier<Boolean> supplier, String message) {
        if (supplier.get()) {
            throw new ValidatorException(message);
        }
    }

    private static class Content {
        private String name;
        private VerifiableLevel level;
        private ClassLoader classLoader;
        private VerifiablePropagation propagation;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public VerifiableLevel getLevel() {
            return level;
        }

        public void setLevel(VerifiableLevel level) {
            this.level = level;
        }

        ClassLoader getClassLoader() {
            return classLoader;
        }

        void setClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        VerifiablePropagation getPropagation() {
            return propagation;
        }

        public void setPropagation(VerifiablePropagation propagation) {
            this.propagation = propagation;
        }

        static Content getInstance(String name, VerifiableLevel level, ClassLoader classLoader, VerifiablePropagation propagation) {
            Content content = new Content();
            content.setName(name);
            content.setLevel(level);
            content.setClassLoader(classLoader);
            content.setPropagation(propagation);
            return content;
        }

        Content getInstance(String name) {
            Content content = new Content();
            content.setName(name);
            content.setLevel(this.getLevel());
            content.setClassLoader(this.getClassLoader());
            content.setPropagation(this.getPropagation());
            return content;
        }
    }


    private static void handleCustomVerify(Class<?> customClass, Object bean) {
        if (customClass == Class.class) throw new ValidatorException("Annotation @VerifiableBean level is CUSTOM, but customClass no definition.");
        if (!ValidatorCustom.class.isAssignableFrom(customClass)) throw new ValidatorException("Custom Class need implement ValidatorCustom Interface.");

        try {
            Constructor<?> constructor = customClass.getDeclaredConstructor(null);
            ValidatorCustom custom = (ValidatorCustom)constructor.newInstance(null);
            String msg = custom.customVerify(bean);
            if (msg != null) {
                throw new ValidatorException(msg);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ValidatorException("Not instance " + customClass.getName() + " from null parameter's constructor. Cause by: " + e.getMessage());
        }

    }

}
