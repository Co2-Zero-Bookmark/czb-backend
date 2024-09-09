package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CommonCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupCodeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeId;

    private String codeName;
    private String codeDescription;
    private String codeSortNumber;
}
