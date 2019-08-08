package com.frog.verifybean;

/**
 * ClassName: VerifiableExcpetion
 * Description: VerifiableExcpetion
 * Date: 2019/8/7 9:51
 *
 * @author bufflu
 */
public class ValidatorException extends RuntimeException {

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
