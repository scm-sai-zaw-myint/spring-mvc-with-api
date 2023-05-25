package com.mtm.ojt.api.commons.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectResponse extends APIResponse {

    @JsonInclude(Include.NON_NULL)
    Object data;
    
    public ObjectResponse(int code, String message, Long timestamp, Object data) {
        super(code, message, timestamp);
        this.data = data;
    }
    
}
