package com.mtm.ojt.api.commons;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtm.ojt.api.commons.exception.TokenException;
import com.mtm.ojt.api.commons.reponse.ErrorResponse;
import com.mtm.ojt.api.utils.JwtTokenUtil;

@Component
public class JwtFilterChain extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            try {
                jwtTokenUtil.validateAccessToken(token);
                setAuthentication(token,request);
            } catch (TokenException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonData = objectMapper.writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token.", new Date().getTime(),e.getMessage()));
                response.getWriter().write(jsonData);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = getTokenFormAccessTokenParameter(request);
        if (request.getHeaders(HttpHeaders.AUTHORIZATION) != null) {
            token = getTokenFromBearerAuth(request);
        }
        return token;
    }

    private String getTokenFromBearerAuth(HttpServletRequest request) {
        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null)
            return null;
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return token.split("Bearer ")[1];
    }

    private String getTokenFormAccessTokenParameter(HttpServletRequest request) {
        if (request.getParameter("access_token") == null)
            return null;
        return request.getParameter("access_token");
    }

    private void setAuthentication(String token, HttpServletRequest request) {
        UserDetails userDetails = jwtTokenUtil.parseToken(token);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
