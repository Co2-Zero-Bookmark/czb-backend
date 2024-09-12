package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

        @Column(name = "deleted_yn", columnDefinition = "char default 'N'")
        private char deletedYn;

        @CreatedDate
        private LocalDateTime createdAt;

        @CreatedBy
        private Long createdId;

        @LastModifiedDate
        private LocalDateTime modifiedAt;

        @LastModifiedBy
        private Long modifiedId;
}
