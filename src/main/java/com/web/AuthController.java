package com.web;

import com.payload.reponse.JWTTokenSuccessResponse;
import com.payload.reponse.MessageResponse;
import com.payload.request.LoginRequest;
import com.payload.request.SignupRequest;
import com.security.JWTTokenProvider;
import com.security.SecurityConstants;
import com.services.UserService;
import com.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private UserService userService;

//создадим api, куда можем заслать юзернэйм и пароль юзера для того, чтобы он авторизировался
//при авторизации пользователя мы получаем объект loginRequest и делаем валидацию.
    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        //Если есть какие то ошибки,то перкращаем валидацию.
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
//Если нет ошибок, то задаем SecurityContextHolder приложению
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // генерируем токен, передаем туда все данные юзера
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
//и передаем его уже на Client на Angular
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    //когда пользователи будут регистрироваться, мы будем засылать api/auth/signup  будем присылать сюда(ниже) объект.
    //о есть пользователь во время регистрации присылает объект SignupRequest  с полями и если нет никаких ошибок
    //если нет никаких ошибок, то создать нового пользователя UserService и этот сервис сохраняет нашего пользователя в БД
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}

