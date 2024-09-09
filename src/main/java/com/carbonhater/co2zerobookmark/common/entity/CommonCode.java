package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class CommonCode extends BaseEntity implements Serializable {
    @EmbeddedId
    private CommonCodeId commonCodeId;

    private String codeName;
    private String codeDescription;
    private String codeSortNumber;
}
