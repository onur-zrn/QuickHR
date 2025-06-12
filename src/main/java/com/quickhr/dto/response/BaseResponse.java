package com.quickhr.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class BaseResponse<T> {
	Boolean success;
	String message;
	Integer code;
	T data;
}
