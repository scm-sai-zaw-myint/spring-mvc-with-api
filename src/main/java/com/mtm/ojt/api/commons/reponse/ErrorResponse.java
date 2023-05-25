package com.mtm.ojt.api.commons.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends APIResponse {
    
    @JsonInclude(Include.NON_NULL)
    Object error;
    
    public ErrorResponse(Integer code, String message, Long timestamp, Object error) {
        super(code,message,timestamp);
        this.error = error;
    }
}
