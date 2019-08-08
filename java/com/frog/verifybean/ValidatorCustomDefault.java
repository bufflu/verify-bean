package com.frog.verifybean;

/**
 * ClassName: className
 * Description: desc
 * Date: 2019/8/8 8:26
 *
 * @author bufflu
 */
public class ValidatorCustomDefault implements ValidatorCustom {

    @Override
    public String customVerify(Object bean) {
        return "ValidatorCustomDefault failed.";
    }
}
