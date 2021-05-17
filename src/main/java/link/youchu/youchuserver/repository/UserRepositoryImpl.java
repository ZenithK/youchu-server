package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.QUserDto;
import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.QUsers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static link.youchu.youchuserver.domain.QUsers.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public UserDto getUserData(UserSearchCondition condition) {
        return queryFactory
                .select(new QUserDto(users.user_id, users.user_email))
                .from(users)
                .where(useridEq(condition.getUser_id()),
                        useremailEq(condition.getUser_email()),
                        googleIdEq(condition.getGoogle_user_id()))
                .fetchOne();
    }

    private BooleanExpression useridEq(Long id){
        return id == null ? null : users.user_id.eq(id);
    }

    private BooleanExpression useremailEq(String email){
        return email == null ? null : users.user_email.eq(email);
    }

    private BooleanExpression googleIdEq(String googleId){
        return googleId == null ? null : users.google_user_id.eq(googleId);
    }

    @Override
    public List<String> registerUsers(UserPostCondition condition) {
        RestTemplate restTemplate = new RestTemplate();
        List<String> channelIdList = new ArrayList<>();
        String jwtToken = condition.getUser_token();
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("aceess_token", jwtToken).encode().toUriString();

            // result
            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            String youtubeRequest = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/subscriptions")
                    .queryParam("part", "snippet").queryParam("mine", true).queryParam("maxResults",100)
                    .queryParam("access_token", jwtToken).encode().toUriString();

            String resultYoutube = restTemplate.getForObject(youtubeRequest, String.class);
            JSONParser parser = new JSONParser();
            Object obj = new Object();
            try {
                obj = parser.parse(resultYoutube);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONObject user_inform = (JSONObject) parser.parse(resultJson);

            // token expired check
            String user_email = user_inform.get("email").toString();

            queryFactory.insert(users)
                    .values(condition.getGoogle_user_id(), user_email, condition.getRefresh_token());

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray item = (JSONArray) jsonObject.get("items");
            for(int i=0; i<item.size(); i++){
                JSONObject json = (JSONObject) item.get(i);
                JSONObject snippet = (JSONObject) json.get("snippet");
                JSONObject resource = (JSONObject) snippet.get("resourceId");
                channelIdList.add(resource.get("channelId").toString());

            }

        }catch(HttpClientErrorException | ParseException e){
            // ID Token Invalid
            return channelIdList;
        }
        return channelIdList;
    }

    @Override
    public Long userPreferChannel() {
        return null;
    }

    @Override
    public Long userDislikeChannel() {
        return null;
    }

    @Override
    public Long exitUser() {
        return null;
    }
}
