package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channel")
@Entity
public class Channel {
    @Id
    @GeneratedValue
    @Column(name = "channel_index")
    private Long id;

    @Column(name = "channel_id")
    private String channel_id;

    @Column(name = "title",length = 255,nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "published_at",length = 255)
    private String publishedAt;

    @Column(name = "thumbnail",length = 255)
    private String thumbnail;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "subscribe_count")
    private Long subScribeCount;

    @Column(name = "banner_image")
    private String bannerImage;

    @Column(name = "video_count")
    private Long video_count;

    @OneToMany(mappedBy = "channel",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<PrefferedChannels> prefferedChannels;

    @OneToMany(mappedBy = "channel",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<DislikeChannels> dislikeChannels;

    @OneToMany(mappedBy = "channel",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<ChannelKeyword> channelKeywords;

    @OneToMany(mappedBy = "channel",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<ChannelTopic> channelTopics;

    @OneToMany(mappedBy = "channel")
    List<DatasetUser> datasetUsers;

    public Channel(String channel_id, String title) {
        this.channel_id = channel_id;
        this.title = title;
    }

    public Channel(Long id) {
        this.id = id;
    }
}
