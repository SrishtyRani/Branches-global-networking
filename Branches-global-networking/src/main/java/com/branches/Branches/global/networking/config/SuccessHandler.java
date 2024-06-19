package com.branches.Branches.global.networking.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SuccessHandler   implements AuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request,   HttpServletResponse response, Authentication authentication) throws IOException  {
       Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
       if (roles.contains("ROLE_ADMIN")) {
           response.sendRedirect("/admin/dashboard");
       }
   }


	



}


