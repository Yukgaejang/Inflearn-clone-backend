package com.yukgaejang.inflearnclone.global.error;

public class NotFoundBoardException extends RuntimeException {

    public NotFoundBoardException() {
        this("존재하지 않는 게시글입니다.");
    }

    public NotFoundBoardException(String message) {
        super(message);
    }
}
