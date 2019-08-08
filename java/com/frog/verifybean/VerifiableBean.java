package com.frog.verifybean;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * classname: VerifiableBean
 * description: Verifiable Bean's annotation.
 * date: 2019/8/6 22:29
 *
 * @author bufflu
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface VerifiableBean {

    /**
     * Verifiable Bean's level, default level is not null.
     */
    VerifiableLevel level() default VerifiableLevel.NOTNULL;

    /**
     * The name corresponding Bean's field will not verify.
     */
    String[] exclude() default {};

    /**
     * Custom Verifiable Class, that is not null when VerifiableLevel is CUSTOM.
     */
    Class<?> customClass() default Class.class;

    /**
     * Verifiable Bean's propagation, default is current bean. Can choose recursive.
     */
    VerifiablePropagation propagation() default VerifiablePropagation.CURRENT;
}
