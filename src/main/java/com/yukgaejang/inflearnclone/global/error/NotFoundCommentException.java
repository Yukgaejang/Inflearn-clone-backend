package com.yukgaejang.inflearnclone.global.error;

public class NotFoundCommentException extends RuntimeException {

    public NotFoundCommentException() {
        this("존재하지 않는 댓글입니다.");
    }

    public NotFoundCommentException(String message) {
        super(message);
    }
}
