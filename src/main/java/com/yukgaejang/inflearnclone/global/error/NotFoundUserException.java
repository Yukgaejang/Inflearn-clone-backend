package com.yukgaejang.inflearnclone.global.error;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        this("접근 권한이 없습니다.");
    }

    public NotFoundUserException(String message) {
        super(message);
    }

}
