package com.mtm.ojt.api.controllers;

import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtm.ojt.api.commons.reponse.APIResponse;
import com.mtm.ojt.api.commons.reponse.ErrorResponse;
import com.mtm.ojt.api.commons.reponse.ObjectResponse;
import com.mtm.ojt.api.request.UserUpdateRequest;
import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;
import com.mtm.ojt.utils.FunctionUtils;
import com.mtm.ojt.web.forms.UserForm;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public APIResponse index(Authentication authentication) {
        if (authentication == null)
            return new APIResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized!", new Date().getTime());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO dto = userService.doGetUserByName(userDetails.getUsername());
        for (GrantedAuthority r : authentication.getAuthorities()) {
            if (r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_OPERATOR")) {
                return new ObjectResponse(HttpStatus.OK.value(), "Getting users list success.", new Date().getTime(),
                        this.userService.doGetUserList());
            }
        }
        return new ObjectResponse(HttpStatus.OK.value(), "Getting user success.", new Date().getTime(), dto);
    }

    @PostMapping("")
    public ResponseEntity<APIResponse> create(@Valid @RequestBody UserForm form, BindingResult validator) {
        if (validator.hasErrors()) {
            return new ResponseEntity<APIResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error.", new Date().getTime(),
                    FunctionUtils.getErrorMessages(validator)), HttpStatus.BAD_REQUEST);
        }
        UserDTO user = this.userService.doAddUser(form);
        return new ResponseEntity<APIResponse>(new ObjectResponse(HttpStatus.CREATED.value(), "Create user success.", new Date().getTime(), user),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> update(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest form,
            BindingResult validator, Authentication authentication) {
        if (validator.hasErrors()) {
            return new ResponseEntity<APIResponse>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error.", new Date().getTime(),
                    FunctionUtils.getErrorMessages(validator)), HttpStatus.BAD_REQUEST);
        } else if (form.getRoles() != null && !authentication.getAuthorities().stream().map(r -> r.getAuthority())
                .toList().contains("ROLE_ADMIN")) {
            return new ResponseEntity<APIResponse>(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                    "You don't enough permission to perform this action.", new Date().getTime(),
                    "Cannot update field roles."), HttpStatus.FORBIDDEN);
        }
        UserForm updateForm = new UserForm(form);
        updateForm.setId(id);
        UserDTO user = this.userService.doUpdateUser(updateForm);

        return new ResponseEntity<APIResponse>(new ObjectResponse(HttpStatus.ACCEPTED.value(), "Update user success.", new Date().getTime(), user),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<APIResponse> delete(@PathVariable Integer id) {
        boolean delete = this.userService.doDeleteUserById(id);
        if(delete) return new ResponseEntity<APIResponse>(new APIResponse(HttpStatus.ACCEPTED.value(), "Delete user success.", new Date().getTime()), HttpStatus.ACCEPTED);
        return new ResponseEntity<APIResponse>(new APIResponse(HttpStatus.FORBIDDEN.value(), "User cannot be deleted!", new Date().getTime()), HttpStatus.FORBIDDEN);
    }
    
}
