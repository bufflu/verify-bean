package com.frog.test;

import com.frog.verifybean.VerifiableBean;
import com.frog.verifybean.VerifiableLevel;
import com.frog.verifybean.VerifiablePropagation;

/**
 * ClassName: className
 * Description: desc
 * Date: 2019/8/8 11:26
 *
 * @author guoxinlu
 */
@VerifiableBean(propagation = VerifiablePropagation.CURRENT)
public class TestBeanEdu {

    private String eduname = "  ";

    private TestBeanEduSub sub = new TestBeanEduSub();
}
