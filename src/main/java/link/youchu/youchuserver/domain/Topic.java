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
    @Column(name = "topic_id")
    private String id;

    @Column(name = "topic_name")
    private String topic_name;

    @OneToMany(mappedBy = "topic")
    List<ChannelTopic> channelTopicList;
}
