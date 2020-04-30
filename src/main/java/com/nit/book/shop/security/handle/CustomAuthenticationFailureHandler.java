package com.nit.book.shop.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nit.book.shop.common.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Throwable cause = exception.getCause();
        ObjectMapper objectMapper = new ObjectMapper();
        if (exception instanceof LockedException){
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(JsonResult.error(exception.getMessage())));
        }else{
            request.getRequestDispatcher("/login?error=用户名或密码错误").forward(request, response);
        }
    }
}
