//facade будет мапить нашу модель из БД на клиента на DTO


package com.facade;

import com.dto.UserDTO;
import com.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    //обычного юзера в объект ДТО. Берем из базы данных user и возвращаем на клиента userDTO
    public UserDTO userToUserDTO(User user) {
        //создали новый объект
        UserDTO userDTO = new UserDTO();
        //замапили его
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setUsername(user.getUsername());
        userDTO.setBio(user.getBio());
        return userDTO;
    }
    //это объект будем вызывать на контроллере
}

