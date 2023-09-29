//это тот самый объект, который будем передавать на сервер, когда будем пытаться
// авторизироваться на этом вэбсайте


package com.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

}

