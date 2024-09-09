package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
public class CommonCodeId implements Serializable {

    private Long groupCodeId;
    private Long codeId;

    @Builder
    public CommonCodeId(Long groupCodeId, Long codeId) {
        this.groupCodeId = groupCodeId;
        this.codeId = codeId;
    }
}
