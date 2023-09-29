// этот класс будет создавать наш токен

package com.security;

import com.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    public String generateToken(Authentication authentication) {
// это Principal будет хранить в себе данные юзера делая импорт из entity
// и так как getPrincipal возвращает объект, делаю каст на юзера  ... = (User) ...
        User user = (User) authentication.getPrincipal();
        //беру текущее время
        Date now = new Date(System.currentTimeMillis());
        //задаю время когда токен иссякнет. expiryDate - время иссякания токена = текущее время + те 10 мин
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
//создаю объект claims (объект Джейсон Веб Токена), который буду передавать в JSON ееб токен. Он и будет содержать данные юзера
        String userId = Long.toString(user.getId());
//ладем в это мапу
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());
// классы библиотеки io.jsonwebtoken будут сейчас строить токен
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                // когда был сгенерирован этот токен - now
                .setIssuedAt(now)
                // закончится через 600 000 милисек
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

    }
// это метод принимает токен, парсирует его. С помощью секретного слова SECRET декадирует его
//  брать оттуда все claimsы. По своему алгоритму декодирует и возвращает данные, которы мы клали
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            // если ошибка возникла, мы ее залогаем - возьме сообщение с этой ошибки
            LOG.error(ex.getMessage());
            return false;
        }
     //то есть делаем валидацию и если все норм то ок, если ошибка - фолс
    }
// метод берет ID из токена
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }

}

