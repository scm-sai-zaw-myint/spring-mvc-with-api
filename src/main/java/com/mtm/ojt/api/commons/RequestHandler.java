package com.mtm.ojt.api.commons;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

public class RequestHandler extends AbstractHandlerMapping {

    private boolean throwExceptionIfNoHandlerFound;
    private String entry = "";

    public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound) {
        this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
    
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        Object handler = super.getDefaultHandler();
        if (handler == null && throwExceptionIfNoHandlerFound && request.getRequestURI().startsWith(request.getContextPath()+entry)) {
            throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), null);
        }
        return (handler != null ? (HandlerExecutionChain) handler : null);
    }

}
