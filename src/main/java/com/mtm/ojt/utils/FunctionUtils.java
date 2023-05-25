package com.mtm.ojt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class FunctionUtils {
    
    public static String generateTokenIp() {
        Random rand = new Random();
        return rand.nextInt(255)+"."+rand.nextInt(255)+"."+rand.nextInt(255)+"."+rand.nextInt(255);
    }
    
    public static Map<String,Object> getErrorMessages(BindingResult bindingResult) {
        Map<String, Object> data = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        // Field errors
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String errorMessage = fieldError.getField() + ": " + fieldError.getDefaultMessage();
            errorMessages.add(errorMessage);
        }

        // Global errors
        for (ObjectError objectError : bindingResult.getGlobalErrors()) {
            String errorMessage = objectError.getDefaultMessage();
            errorMessages.add(errorMessage);
        }
        data.put("errors", errorMessages);
        return data;
    }
    
    public static List<String> getAuthority(Authentication authentication){
        return authentication.getAuthorities().stream().map(r -> r.getAuthority()).toList();
    }

}
