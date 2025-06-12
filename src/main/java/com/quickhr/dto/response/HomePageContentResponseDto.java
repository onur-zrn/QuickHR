package com.quickhr.dto.response;

import java.util.*;

public record HomePageContentResponseDto(
        String title,
        String content,
        List<String> highlights
) {

}