package org.serious.dev.kafka.event;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import static java.lang.String.format;

@Getter
@SuperBuilder
public class OrderCreatedEvent extends OrderEvent {

    private final Long orderId;

    @Override
    public String toString() {
        return format(
                "orderId=%d, userId=%d, postId=%d",
                this.orderId,
                this.getUserId(),
                this.getPostId()
        );
    }
}
