package ru.test.account.exception;

public class BusinessException extends Exception {

    private int code;

    public BusinessException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
