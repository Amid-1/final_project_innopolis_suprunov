//константы, которые сможем изменить здесь и они изменятся в других местах


package com.security;

public class SecurityConstants {
//хотим, чтобы пользователь авторизировался под этим url - "/api/auth/**
//** - любой url
    public static final String SIGN_UP_URLS = "/api/auth/**";
// это все требуется для генерации JSON веб токена
    public static final String SECRET = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
// JSON веб токен может иссякнуть(чтобы сессия прервалась через какое то время) для доп.защиты
    public static final long EXPIRATION_TIME = 600_000; //10min

}
