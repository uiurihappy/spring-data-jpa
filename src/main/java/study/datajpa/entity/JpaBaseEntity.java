package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

// 속성만 상속관계가 있는데 속성만 테이블에서 같이 쓸수있도록 공유한다.
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    // createdAt은 업데이트 못하도록 설정
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
