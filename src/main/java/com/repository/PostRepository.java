package com.repository;

import com.entity.Post;
import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // SELECT * FROM POST as p WHERE User='user' SORT DESC на заднем плане Хибернейт генерирует эти запросы
    //посты конкретного юзера. Стратегия Десцендикт - видим последний пост данного юзера, остальные посты ниже
    List<Post> findAllByUserOrderByCreatedDateDesc(User user);
    // когда захожу на страницу вижу все посты из БД
    List<Post> findAllByOrderByCreatedDateDesc();
    Optional<Post> findPostByIdAndUser(Long id, User user);
}
