package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "topic")
public class Topic {
    @Id
    @Column(name = "topic_index")
    private Long id;

    @Column(name = "topic_name")
    private String topic_name;

    @Column(name = "topic_id")
    private String topic_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;
}
