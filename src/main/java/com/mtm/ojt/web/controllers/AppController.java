package com.mtm.ojt.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.user.UserService;
import com.mtm.ojt.utils.FunctionUtils;

@Controller
public class AppController {

    @Autowired
    JavaMailSender mailSender;
    
    @Autowired
    UserService userService;
    
    @GetMapping("/")
    public String homePage(Authentication authentication) {
        if(authentication != null) return "redirect:/user/"; 
        return "redirect:login";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        /*
         * SimpleMailMessage message = new SimpleMailMessage();
         * message.setFrom("shadowfightj25@gmail.com");
         * message.setTo("scm.saizawmyint@gmail.com");
         * message.setSubject("Testing Email"); message.setText("Hello world");
         * mailSender.send(message);
         */
        return "login";
    }
    
    @GetMapping("/admin")
    public String adminPage(Authentication auth){
        return "admin";
    }
    
    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO user = this.userService.doGetUserByName(userDetails.getUsername());
        this.userService.doUpdateTokenIp(user.getId(), FunctionUtils.generateTokenIp());
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, null, authentication);
        }
        request.getSession().invalidate();
        return "redirect:/login";
    }
    
}
