package com.frog.test;

import com.frog.verifybean.VerifiableBean;
import com.frog.verifybean.VerifiableLevel;
import com.frog.verifybean.VerifiablePropagation;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName: TestBean
 * Description: TestBean
 * Date: 2019/8/7 7:59
 *
 * @author bufflu
 */
@VerifiableBean(level = VerifiableLevel.NOTBLANK, propagation = VerifiablePropagation.RECURSIVE)
public class TestBean {

    private TestBeanEdu testBeanEdu = new TestBeanEdu();

    private String string = "";

    private String string1 = "  ";

    private int anInt;

    private Integer integer;

    private List<String> stringList = Arrays.asList("001","0"," ");


    private BigInteger bigInteger = new BigInteger("1");
}
