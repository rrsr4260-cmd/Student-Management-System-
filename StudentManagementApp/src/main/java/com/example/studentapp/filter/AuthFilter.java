package com.example.studentapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        boolean allowed =
                uri.endsWith("login.jsp") ||
                uri.endsWith("/login") ||
                uri.endsWith("/logout") ||
                uri.contains("/css/") ||
                uri.contains("/js/") ||
                uri.contains("/uploads/") ||
                uri.contains("/api/") ||
                uri.contains("favicon");

        if (allowed) {
            chain.doFilter(req, res);
            return;
        }

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}