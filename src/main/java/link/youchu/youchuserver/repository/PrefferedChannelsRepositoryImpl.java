package link.youchu.youchuserver.repository;

import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.QChannelDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.PrefferedChannels;
import link.youchu.youchuserver.domain.QChannel;
import link.youchu.youchuserver.domain.QPrefferedChannels;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.List;

import static link.youchu.youchuserver.domain.QChannel.channel;
import static link.youchu.youchuserver.domain.QPrefferedChannels.*;

@Repository
public class PrefferedChannelsRepositoryImpl implements PrefferedChannelsRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    public PrefferedChannelsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<ChannelDto> getRecommendChannel(UserSearchCondition condition) {
        Byte[] data = {};
        RestTemplate restTemplate = new RestTemplate();
        String scoring_url = "http://a379b07d-882c-410e-a70c-4d9cfcaf830b.koreacentral.azurecontainer.io/score";
        String key = "L8k6h8EfA8yb018poYSK9NfOetwb9Bvh";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer " + key);
        Gson gson = new Gson();
        String requestValue = gson.toJson(data);
        HttpEntity<String> entity = new HttpEntity<String>(requestValue,headers);
        String requestUrl = UriComponentsBuilder.fromHttpUrl(scoring_url).queryParam("key",key).encode().toUriString();

        String result = restTemplate.postForObject(scoring_url,entity,String.class);

        System.out.println(result);
        return null;
    }

    @Override
    public Long deletePreffered(PrefferedPostCondition condition) {
        long count = queryFactory.delete(prefferedChannels)
                .where(userIdEq(condition.getUser_id()),
                        channelIndexEq(condition.getChannel_index()))
                .execute();
        return count;
    }

    private BooleanExpression userIdEq(Long user_id){
        return user_id == null ? null : prefferedChannels.users.user_id.eq(user_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : prefferedChannels.channel.id.eq(channel_index);
    }

    @Override
    public List<ChannelDto> getPrefferedList(UserSearchCondition condition) {
        return queryFactory.select(new QChannelDto(channel.title, channel.description, channel.publishedAt,
                channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                channel.video_count, channel.channel_id))
                .from(prefferedChannels)
                .join(prefferedChannels.channel, channel)
                .where(userIdEq(condition.getUser_id()))
                .fetch();
    }
}
