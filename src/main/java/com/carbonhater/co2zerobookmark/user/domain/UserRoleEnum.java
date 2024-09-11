package com.carbonhater.co2zerobookmark.user.domain;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");



    UserRoleEnum(String value) {

        this.value = value;
    }
    private String value;;
}
