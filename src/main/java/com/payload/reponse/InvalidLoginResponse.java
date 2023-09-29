//package payload это то, что сервер будет возвращать

// То есть, когда будет возникать ошибка 401, ты мы будем создавать этот объект InvalidLoginResponse
//// и выдавать его на Client. Там мы будем витдеть, что не авторизированы и получать эти данные - "Invalid Username";
//         "Invalid Password";

package com.payload.reponse;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {

    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }


}

