package com.security;

import com.entity.User;
import com.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
// нужен httpServletRequest который поступает каждый раз, когда поступает какой то запрос на наш сервер
// будем каждый раз вызывать этот метод и брать данные с этого запроса HttpServletRequest httpServletRequest
        //они уже будут содержаться в этом объекте
            String jwt = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
//если все имеется и он проходит валидацию, то мы можем брать данные из этого токена
                User userDetails = customUserDetailsService.loadUserById(userId);
// есть ли у нас пользователь в БД с таким ID
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        // передаем туда данные пользователя
                        userDetails, null, Collections.emptyList()
                );
// задаем детали авторизации
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));;
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            LOG.error("Could not set user authentication");
        }
// фильтр запрос и ответ. Берем запрос httpServletRequest, выше с ним что то делаем и оттдаем ответ httpServletResponse
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    //метод, который будет помогать брать JSON веб токен прямо из запроса, который будет поступать к нам на сервер
    private String getJWTFromRequest(HttpServletRequest request) {
//каждый раз во время запроса на сервер с Angular, JSON веб токен будет передаваться внутри header
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearToken.split(" ")[1];
        }
        return null;
    }


}

