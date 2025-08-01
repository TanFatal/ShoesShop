package com.example.learningAPISpring.exception;

public enum ErrorCode {
    USER_EXITED(1001,"User exited"),
    USERNAME_VALIDATION(1003,"username can it nhat co 3 tu"),
    INVALID_PASSWORD(1004,"Password phải có ít nhất 8 ký tự và đủ 3 trong 4 loại: chữ thường, chữ hoa, số, ký tự đặc biệt"),
    USERNAME_NOTBLANK(1005,"tai khoang khong duoc de trong"),
    PASSWORD_NOTBLANK(1005,"mat khau khong duoc de trong"),

    ;
    private  int code;
    private  String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
