package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "banner")
@Entity
public class Banner {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "banner_url")
    private String banner_url;

    @Column(name = "connect_url")
    private String connect_url;
}
