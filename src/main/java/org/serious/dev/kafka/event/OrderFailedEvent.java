package org.serious.dev.kafka.event;

import lombok.experimental.SuperBuilder;

import static java.lang.String.format;

@SuperBuilder
public class OrderFailedEvent extends OrderEvent {

    @Override
    public String toString() {
        return format(
                "userId=%d, postId=%d",
                this.getUserId(),
                this.getPostId()
        );
    }
}
