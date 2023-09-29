// для создания нового пользователя в БД


package com.services;

import com.dto.UserDTO;
import com.entity.User;
import com.entity.enums.ERole;
import com.exeptoins.UserExistExeption;
import com.payload.request.SignupRequest;
import com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    //класс для коммуникации с базой данных
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //метод для создания нового юзера (импорт из entity). Будет поступать SignupRequest userIn
    public User createUser(SignupRequest userIn) {
        //создаем нового юзера
        User user = new User();
        // получаем из браузера
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        //прежде чем сохранить пароль в БД надо его закодировать. Берем пароль, который поступет из брузера (getPassword())
        // кодируем его и задаем пользователю, которого будем сохранять в БД
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        //И сразу же зададим одну роль. Все пользователи при регистрации будут получать роль юзер (ROLE_USER)
        //Если хотим сделать кого то админом, то можем прописать в БД вручную
        user.getRole() .add(ERole.ROLE_USER);

        try {
        //делаем info, что сохраняем пользователя
            LOG.info("Saving User {}", userIn.getEmail());
        //вернем пользователя, которого возвращает хибернейт из базы данных
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistExeption("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }
//метод, позволяющий пользователю обновить свои данные: имя, фамилию, добавить биографию
    //объект principal будет содержать в себе данные юзера(id, userName). И благодаря этому объекту мы можем достать нашего пользователя
    public User updateUser(UserDTO userDTO, Principal principal) {
        //сначала берем пользователя из БД
        User user = getUserByPrincipal(principal);
        //задам данные, которые получили из объекта DTO
        user.setName(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    // метод, позволяющий доставать юзера из объекта principal
    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();  // getName это Username
        return userRepository.findUserByUsername(username)
                //если объект не найдется, то выкинется ошибка
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

