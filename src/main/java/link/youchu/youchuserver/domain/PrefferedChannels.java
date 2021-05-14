package link.youchu.youchuserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "preffered_channels")
@Entity
public class PrefferedChannels {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_index")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public void setId(Long id) {
        this.id = id;
    }

    public PrefferedChannels(Long channel_index, Long user_id) {
        this.channel = new Channel(channel_index);
        this.users = new Users(user_id);
    }
}
