package org.serious.dev.exception;

public class OrderCreationException extends RuntimeException {

    private static final String TEMPLATE = "при сохранении заказа на пост id=%d пользователя userId=%d возникла ошибка";

    public OrderCreationException(Long userId, Long postId, Exception e) {
        super(String.format(TEMPLATE, userId, postId), e);
    }
}
