//будет использоваться в другом классе SecurityConfig и в нем будет AuthenticationManagerBuilder
// который будет использовать методы этого класса и искать пользователя из БД.
//То есть этот класс будет помогать доставать данный из БД


package com.services;

import com.entity.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//этот класс  используется в классе SecurityConfig и помогает доставать данные из БД
public class CustomUserDetailsService implements UserDetailsService {
//этот класс помогает брать данные из хранилища
    private final UserRepository userRepository;

    @Autowired
// вставляю Bin репозитори в другой Bin, который уже сервис и Спринг во время компиляции будет внедрять его в наш класс.
// Вставили репозитори сюда через конструктор и можем полтзоваться всем функционалом из UserRepository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    //со сторны клиента будем передавать username, но в БД сохраним как email
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));

        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }


    public static User build(User user) {
        List<GrantedAuthority> authorities = user.getRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
// вернуть юзера с конструктором
        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }




}

