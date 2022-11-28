package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    // Persistable 인터페이스를 구현해서 판단 로직 변경 가능

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        // 새로운 엔티티인지 로직을 구현하여 boolean값으로 return이 가능하다.
        //  createdAt이 null이면 생성이 아직 안되었으니 true가 떨어지고 아니면 false가 떨어질 것이다.
        return createdAt == null;
    }
}
