package com.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String title;
    private String caption;
    private String location;
    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> likedUsers = new HashSet<>();
    //у одного поста один пользователь
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    //у поста много комментариев; EAGER - когда загружаю пост хочу сразу видеть комментарии
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER,mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
