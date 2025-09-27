package com.example.hotelbookingmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String requiredRole;
    private final String defaultRedirectUrl;
    private final String failureUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // ✅ Check if the user has the required role
        if (roles.contains(requiredRole)) {
            // If the user has the correct role, proceed with normal redirection
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            if (savedRequest != null) {
                response.sendRedirect(savedRequest.getRedirectUrl());
            } else {
                response.sendRedirect(defaultRedirectUrl);
            }
        } else {
            System.err.println("Authentication failed: User does not have the required role " + requiredRole);
            SecurityContextHolder.clearContext(); // Clear the security context
            request.getSession().invalidate(); // Invalidate the session
            response.sendRedirect(failureUrl + "?error=role"); // Redirect with a specific error
        }
    }
}
