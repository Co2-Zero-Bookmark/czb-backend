package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Getter
@ToString
public class CommonCode extends BaseEntity implements Serializable {
    @EmbeddedId
    private CommonCodeId commonCodeId;

    private String codeName;
    private String codeDescription;
    private String codeSortNumber;
}
