package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "dataset_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DatasetUser {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;


}
