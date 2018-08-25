package com.xylink.auth;

import com.xylink.utils.SpringUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by konglk on 2018/8/25.
 */

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {AuthService authService = SpringUtils.getBean(AuthService.class);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userId = httpRequest.getHeader("userId");
        String certificate = httpRequest.getHeader("certificate");
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(certificate)){
            return;
        }
        if(authService.isValidUser(userId, certificate)){
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
