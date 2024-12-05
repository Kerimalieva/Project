package com.example.project.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")  // Указание имени таблицы
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = false)
//    private Post post;

//    @Column(name = "post_id")  // Внешний ключ для связи с таблицей posts
//    private Integer postId;
//
//    @Column(name = "user_id")  // Внешний ключ для связи с таблицей users
//    private Integer userId;



    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)  // Внешний ключ для связи с таблицей Post
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)  // Внешний ключ для связи с таблицей User
    private User user;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Геттеры и сеттеры
}
