package org.serious.dev.dto;

import lombok.Builder;

@Builder
public record OrderRequestDto(Long userId, Long postId) {
}
