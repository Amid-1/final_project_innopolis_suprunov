//в security все будет отвечать за генерацию, валидацию токена, за то, когда юзер
//сможет сделать авторизацию, если авторизация не правильная и т.д.


//Этот класс будет возвращать объект, который будет содержать, что параметры, которые
// мы внесли не правильные. То есть этот класс будет ловить ошибку авторизации и выдавать
// статус 401, то есть не авторизирован, когда пользователь хочет получить защищенный ресурс
//без авторизации. То есть, когда будет возникать ошибка 401, ты мы будем создавать этот объект InvalidLoginResponse
// и выдавать его на Client. Там мы будем витдеть, что не авторизированы и получать эти данные -  "Invalid Username";
//      "Invalid Password";

package com.security;

import com.payload.reponse.InvalidLoginResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component

public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);
// объект HttpServletResponse, который будет возвращаться на Clientа, опять ему зададим данные хедера
        httpServletResponse.setContentType(SecurityConstants.CONTENT_TYPE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().println(jsonLoginResponse);
    }
}

