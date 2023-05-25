package com.mtm.ojt.api.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mtm.ojt.api.commons.reponse.APIResponse;
import com.mtm.ojt.api.commons.reponse.ErrorResponse;
import com.mtm.ojt.api.response.UserResponse;
import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;

@RestController
@RequestMapping("/api")
public class AppRestController {

    @Autowired
    UserService userService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse index() {
        return new APIResponse(200, "Welcome to the app", new Date().getTime());
    }

    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public APIResponse admin(Authentication authentication) {
        return new APIResponse(200, "Welcome to the admin page", new Date().getTime());
    }

    @GetMapping("/access-denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public APIResponse denied() {
        return new APIResponse(HttpStatus.FORBIDDEN.value(), "You don't have enough permission to access this page!", new Date().getTime());
    }

    @GetMapping("/me")
    public ResponseEntity<APIResponse> me(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<APIResponse>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                    "You're not authenticated yet!", new Date().getTime(), null), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO user = this.userService.doGetUserByName(userDetails.getUsername());
        return new ResponseEntity<APIResponse>(new UserResponse(HttpStatus.OK.value(),
                "Getting profile data success.", new Date().getTime(), user), HttpStatus.OK);
    }
}
