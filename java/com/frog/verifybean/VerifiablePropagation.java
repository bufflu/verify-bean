package com.frog.verifybean;

/**
 * ClassName: VerifiablePropagation
 * Description: Verifiable Bean's propagation, only verify current bean use CURRENT,
 *              verify current bean and field that is custom Bean use RECURSIVE.
 * Date: 2019/8/8 10:58
 *
 * @author bufflu
 */
public enum VerifiablePropagation {

    /**
     * only verify current Bean's field.
     */
    CURRENT,

    /**
     * if choose recursive, recursive verify parent Bean's every field(String or custom Bean).
     * if field's Bean(Sub Bean) have @VerifiableBean and VerifiablePropagation is current, stop parent Bean recursive.
     */
    RECURSIVE
}
