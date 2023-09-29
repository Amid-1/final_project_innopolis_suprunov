package com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {

    @Id
    // стратегия IDENTITY - когда сохраняем объект, то количество просто будет увеличиваться по одному верх: первый олбъект id 1 и т.д.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "bytea")
    //фотографии сохраняю в блок
    private byte[] imageBytes;
    // JsonIgnore чтобы не передавать эти данные на клиента
    @JsonIgnore
    //фотографии юзеров
    private Long userId;
    @JsonIgnore
    //фотографии поста
    private Long postId;
}
