package com.entity;


import com.entity.enums.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

//аннотация Lombock для геттеров и сеттеров
@Data
@Entity
@Table(name="users")
//интефейс UserDetails идет из спринг секьюрити
public class User implements UserDetails {
    //все эти вещи валидации, которые происходят перед сохрананением значений в БД (чтобы не сохранять пустую, например)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //колонна может быть пустая
    @Column(nullable = false)
    private String name;
    //никнейм уник и не можем обновлять, чтобы знать кто лайкнул какой пост
    @Column(unique = true, updatable = false )
    private String username;
    @Column(nullable = false)
    private String Lastname;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private  String bio;
    @Column(length = 3000)
    private String password;
    //зависимость между пользователями и ролями (OneToMany)
    @ElementCollection(targetClass = ERole.class)
    //сохраню эту зависимость в БД в отдельную таблицу с двумя полями: user_role
    @CollectionTable(name = "user_role",
    //и второе поле присоединяется к колоннне юзеров
    joinColumns = @JoinColumn(name = "user_id"))
    private Set<ERole> role = new HashSet<>();
    //каскадный тип ALL - удаляю юзера и удаляются все его посты; LAZY - хотим получить юзера по id, но его посты не нужны
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user",orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    //атрибут для отслеживания даты создания того или иного элемента
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createDate;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    public User(Long id,
                String username,
                String email,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //метод сохраниния в БД этого атрибута
    //аннотация задает значение атрибута createDate до того, как сделаю новую запись в БД
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    /**
     * SECURITY
     */

    @Override
    public  String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
