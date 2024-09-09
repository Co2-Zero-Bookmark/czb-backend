package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

        private char deletedYN;

        @CreatedDate
        private String createdBy;

        @CreatedBy
        private long createdId;

        @LastModifiedDate
        private String modifiedBy;

        @LastModifiedBy
        private long modifiedId;
}
