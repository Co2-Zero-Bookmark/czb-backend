package com.carbonhater.co2zerobookmark.user.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter //entity는 setter x
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String userEmail;

    private String userPassword;

    private int userPoint;

}
