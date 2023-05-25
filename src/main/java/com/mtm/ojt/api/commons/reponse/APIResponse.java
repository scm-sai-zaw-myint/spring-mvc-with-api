package com.mtm.ojt.api.commons.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class APIResponse {
    @JsonInclude(Include.NON_NULL)
    Integer code;
    @JsonInclude(Include.NON_NULL)
    String message;
    @JsonInclude(Include.NON_NULL)
    Long timestamp;
    
    public APIResponse(Integer code, String message, Long timestamp) {
        super();
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }
}
