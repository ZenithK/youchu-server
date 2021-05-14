package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dislike_channels")
@Entity
public class DislikeChannels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_index")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private Users users;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public DislikeChannels(Long channel_index, Long user_id) {
        this.channel = new Channel(channel_index);
        this.users = new Users(user_id);
    }
}
