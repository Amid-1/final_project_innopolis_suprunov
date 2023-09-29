//dto - date transfer objekt. Его будем передавать прямо на клиента. Почему не юзер модели?
//Потому что в больших приложениях у нас существуют много полей, но на клиентской стороне
//нам нужно их намного меньше. И чтобы не доставать целый объект из БД со всеми атрибьютами
//с этим объектом мы будем передавать только то, что нам нужно



package com.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDTO {

    private Long id;
    @NotEmpty
    private String message;
    private String username;

}

