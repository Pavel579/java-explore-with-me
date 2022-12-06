package ru.practicum.ewm.exceptions;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ErrorApi {
    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
