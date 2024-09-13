package com.carbonhater.co2zerobookmark.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

        private char deletedYn;

        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createdAt;

        @CreatedBy
        private long createdId;

        @LastModifiedDate
        private LocalDateTime modifiedAt;

        @LastModifiedBy
        private long modifiedId;

        @PrePersist
        public void prePersist() {
                this.deletedYn = this.deletedYn == '\u0000' ? 'N' : this.deletedYn; // 기본값이 없으면 'N' 설정
        }

}
