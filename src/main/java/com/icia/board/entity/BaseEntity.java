package com.icia.board.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 작성시간, 수정시간 컬럼을 가지는 클래스
 * 이 클래스를 상속받는 클래스는 작성시가(createAt), 수정시간(updataAt) 컬럼이 추가됨
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 작성시간 컬럼
    @CreationTimestamp // create 할 때 현재시간을 입력
    @Column(updatable = false) // update 할 때는 작동하지 말아라
    private LocalDateTime createdAt;

    @UpdateTimestamp // update 시 현재시간을 입력
    @Column(insertable = false) // insert 시 작동하지 말아라
    private LocalDateTime updatedAt;
}
