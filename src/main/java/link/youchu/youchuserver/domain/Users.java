package link.youchu.youchuserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "google_user_id")
    private String google_user_id;

    @Column(name = "user_email")
    private String user_email;

    @OneToMany(mappedBy = "users",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<PrefferedChannels> prefferedChannels;

    @OneToMany(mappedBy = "users",cascade = CascadeType.REMOVE,orphanRemoval = true)
    List<DislikeChannels> dislikeChannels;

    public Users(Long user_id) {
        this(user_id,null);
    }

    public Users(Long user_id, String user_email) {
        this.user_id = user_id;
        this.user_email = user_email;
    }

    public Users(String google_user_id, String user_email){
        this.google_user_id = google_user_id;
        this.user_email = user_email;
    }

    public Long getUser_id() {
        return user_id;
    }


}
