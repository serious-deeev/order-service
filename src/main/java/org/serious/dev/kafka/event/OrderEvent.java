package org.serious.dev.kafka.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@RequiredArgsConstructor
public abstract class OrderEvent {

    private final Long userId;
    private final Long postId;
}
