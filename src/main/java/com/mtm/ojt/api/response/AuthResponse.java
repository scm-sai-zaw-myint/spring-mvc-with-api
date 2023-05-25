package com.mtm.ojt.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mtm.ojt.api.commons.reponse.APIResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse  extends APIResponse{
    
    @JsonInclude(Include.NON_NULL)
    Object data;
    @JsonInclude(Include.NON_NULL)
    String token;
    @JsonInclude(Include.NON_NULL)
    Long expiredIn;
    
    public AuthResponse(Integer code, String message, Long timestamp, String token,Long expiredIn, Object data) {
        super(code, message, timestamp);
        this.data = data;
        this.token = token;
        this.expiredIn = expiredIn;
    }
    
}
