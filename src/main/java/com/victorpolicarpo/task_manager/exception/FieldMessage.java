package com.victorpolicarpo.task_manager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldMessage {
    private String fieldName;
    private String message;
}
