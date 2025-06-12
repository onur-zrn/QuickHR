package com.quickhr.exception;

import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ErrorMessage {
	int code;
	String message;
	Boolean success;
	List<String> details;
}
