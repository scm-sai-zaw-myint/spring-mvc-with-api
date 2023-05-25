package com.mtm.ojt.web.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mtm.ojt.bl.services.dto.UserDTO;
import com.mtm.ojt.bl.services.role.RoleService;
import com.mtm.ojt.bl.services.user.UserService;
import com.mtm.ojt.utils.FunctionUtils;
import com.mtm.ojt.web.forms.UserForm;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    JavaMailSender mailSender;
    
    @Autowired
    HttpSession session;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping("/")
    public String index(Model model,Authentication authentication) {
        
        
        if(authentication == null) return "redirect:/login";
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO dto = userService.doGetUserByName(userDetails.getUsername());
        for(GrantedAuthority r: authentication.getAuthorities()) {
            if(r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_OPERATOR")) {
                return "redirect:/user/lists";
            }
        }
        model.addAttribute("user", dto);
        return "user/index";
    }
    
    @GetMapping("/lists")
    public String list(Model model) {
        model.addAttribute("usersList", userService.doGetUserList());
        return "user/list";
    }
    
    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("rolesList", this.roleService.getRoles());
        model.addAttribute("userForm", new UserForm());
        return "user/create";
    }
    
    @PostMapping("/create")
    public ModelAndView create(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult binding) {
        ModelAndView model = new ModelAndView();
        if(binding.hasErrors()) {
            model.addObject("rolesList", this.roleService.getRoles());
            model.addObject("userForm", userForm);
            session.setAttribute("sessionMessage", "Fails to create new user!");
            return model;
        }
        this.userService.doAddUser(userForm);
        session.setAttribute("sessionMessage", "Create new user success.");
        model.setViewName("redirect:/user/");
        return model;
    }
    
    @GetMapping("/update/{id}")
    public String updatePage(Model model, @PathVariable Integer id,Authentication authentication) {
        model.addAttribute("rolesList", this.roleService.getRoles());
        UserForm form = new UserForm(this.userService.doGetUserById(id));
        model.addAttribute("userForm", form);
        model.addAttribute("isAdmin", FunctionUtils.getAuthority(authentication).contains("ROLE_ADMIN"));
        return "user/update";
    }
    
    @PostMapping("/update")
    public ModelAndView update(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult binding,Authentication authentication) {
        ModelAndView model = new ModelAndView();
        if(binding.hasErrors()) {
            model.addObject("rolesList", this.roleService.getRoles());
            model.addObject("userForm", userForm);
            model.addObject("isAdmin", FunctionUtils.getAuthority(authentication).contains("ROLE_ADMIN"));
            session.setAttribute("sessionMessage", "Fails to update new user!");
            return model;
        }else if(userForm.getId() == null) {
            model.addObject("rolesList", this.roleService.getRoles());
            model.addObject("userForm", userForm);
            model.addObject("Error", "User id is null!");
            session.setAttribute("sessionMessage", "Fails to update new user!");
            return model;
        }
        this.userService.doUpdateUser(userForm);
        model.setViewName("redirect:/user/");
        session.setAttribute("sessionMessage", "Update user success.");
        return model;
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        boolean delete = this.userService.doDeleteUserById(id);
        if(delete) session.setAttribute("sessionMessage", "Delete user success.");
        else session.setAttribute("sessionMessage", "Fails to delete this user!");;
        return "redirect:/user/";
    }
}
