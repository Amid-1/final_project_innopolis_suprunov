// будет возвращать мару с ошибками или вообще ничего не будет возвращать

package com.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation {
// этот BindingResult будет содержать в себе ошибки, которые могуе сохраниться в LoginRequest
//Например, если попытаемся авторизовать пользователя с пустым полем, то выскачит ошибка
//из  LoginRequest - @NotEmpty(message = "Username cannot be empty"). Но для этого мы ее должны получить
// и она приходит в этот объект BindingResult
    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        //если ошибки в этом result
        if (result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            //если коллекция не пустая, то есть в ней будут какие то ошибки из BindingResult
            if (!CollectionUtils.isEmpty(result.getAllErrors())) {
                //тогда будем лупить через них ObjectError. И как раз getAllErrors возвращает list ObjectError
                for (ObjectError error : result.getAllErrors()) {
                    //и положим в эту мапу мапу код ошибки и сообщение
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}

