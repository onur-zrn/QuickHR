package com.quickhr.dto.request;

import com.quickhr.enums.company.ECommentStatus;

public record CommentApproveOrRejectRequestDto(
        ECommentStatus status
) {
}