package link.youchu.youchuserver.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "channel_category")
@Entity
public class ChannelCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_index")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
