package com.example.controller;

import java.util.Set;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import  org.springframework.security.core.Authentication;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.AuthorityUtils;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DashBoardController {

    @GetMapping("/dashboard")
    public String redirectToDashboard(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

            if (roles.contains("ROLE_USER")) {
                return "redirect:/user/dashboard";
            } if (roles.contains("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }
        
        return "redirect:/default_dashboard";
    }
}

