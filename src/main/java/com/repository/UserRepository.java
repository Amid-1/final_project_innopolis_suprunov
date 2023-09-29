package com.repository;

import com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//этот интерфейс растягивает Jpaинтерфейс. <> здесь два типа Дженерика: тип объекта из БД и тип ID Лонг
public interface UserRepository extends JpaRepository<User, Long> {
    // Класс Optional помогает избежать таких ошибок как NullPointExeption когда юзера нет  в БД и возвращает объект Optional
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
    // например кликнем на фото пользователя и хотим получить его профайл
    Optional<User> findUserById(Long id);

}
