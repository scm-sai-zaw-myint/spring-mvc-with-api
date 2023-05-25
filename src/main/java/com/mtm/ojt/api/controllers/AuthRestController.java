package com.mtm.ojt.api.controllers;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtm.ojt.api.commons.reponse.APIResponse;
import com.mtm.ojt.api.commons.reponse.ErrorResponse;
import com.mtm.ojt.api.response.AuthResponse;
import com.mtm.ojt.api.response.UserResponse;
import com.mtm.ojt.api.utils.JwtTokenUtil;
import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;
import com.mtm.ojt.utils.FunctionUtils;
import com.mtm.ojt.web.forms.UserForm;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.validity}")
    private long validity;

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody UserForm form, HttpServletRequest request) {
        try {
            request.login(form.getName(), form.getPassword());
        } catch (ServletException e) {
            return new ResponseEntity<APIResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Invalid username or password", new Date().getTime(), e.toString()), HttpStatus.BAD_REQUEST);
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        UserDTO userDetails = this.userService.doGetUserByName(auth.getName());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(),
                auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // token IP
        String tokenIP = userDetails.getTokenIp() == null ? FunctionUtils.generateTokenIp() : userDetails.getTokenIp();
        if (userDetails.getTokenIp() == null)
            this.userService.doUpdateTokenIp(userDetails.getId(), tokenIP);
        String token = jwtTokenUtil.createUserToken(userDetails, tokenIP);
        Long expiredIn = new Date(System.currentTimeMillis() + (validity * 60) * 1000).getTime();

        return new ResponseEntity<APIResponse>(new AuthResponse(HttpStatus.OK.value(), "Login success!",
                new Date().getTime(), token, expiredIn, new UserResponse(userDetails)), HttpStatus.OK);
    }

}
