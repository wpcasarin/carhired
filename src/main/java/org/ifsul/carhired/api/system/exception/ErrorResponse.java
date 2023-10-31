package org.ifsul.carhired.api.system.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime time;
    private String status;
    private List<String> errors;


    public ErrorResponse(HttpStatus status, List<String> errors) {
        this.time = LocalDateTime.now();
        this.status = status.toString();
        this.errors = errors;
    }
}