package com.carbonhater.co2zerobookmark.user.repository.entity;

import com.carbonhater.co2zerobookmark.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter //entity는 setter x
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String userEmail;

    private String userPassword;

    private String role;

}
