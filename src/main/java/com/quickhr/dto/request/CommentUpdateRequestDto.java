package com.quickhr.dto.request;

public record CommentUpdateRequestDto(
        Long commentId,
        String position,
        String content
) {
}
