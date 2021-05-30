package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.QUsers;
import link.youchu.youchuserver.domain.Users;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.AuthenticationException;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static link.youchu.youchuserver.domain.QUsers.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public Long getUserToken(TokenUpdateCondition condition) throws AuthenticationException {
        RestTemplate restTemplate = new RestTemplate();
        String jwtToken = condition.getUser_token();
        JSONParser parser = new JSONParser();
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("access_token", jwtToken).encode().toUriString();

            // result
            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            JSONObject user_inform = (JSONObject) parser.parse(resultJson);

            // token expired check
            String user_email = user_inform.get("email").toString();

            UserSearchCondition userSearchCondition = new UserSearchCondition();
            userSearchCondition.setGoogle_user_id(condition.getGoogle_user_id());
            Long userIndex = getUserIndex(userSearchCondition);
            return userIndex;
        }catch(Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    public Long getUserIndex(UserSearchCondition condition) {
        Long index = queryFactory.select(users.user_id)
                .from(users)
                .where(googleIdEq(condition.getGoogle_user_id()))
                .fetchOne();
        return index;
    }

    @Override
    public UserDto getUserData(UserSearchCondition condition) {
        UserDto userDto = queryFactory
                .select(new QUserDto(users.user_id, users.user_email))
                .from(users)
                .where(useridEq(condition.getUser_id()),
                        useremailEq(condition.getUser_email()),
                        googleIdEq(condition.getGoogle_user_id()))
                .fetchOne();
        if (userDto == null) {
            System.out.println("null");
        }
        System.out.println(userDto);
        return userDto;
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
    public List<String> registerUsers(UserPostCondition condition) throws AuthenticationException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        List<String> channelIdList = null;
        String jwtToken = condition.getUser_token();
        JSONParser parser = new JSONParser();
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("access_token", jwtToken).encode().toUriString();

            // result
            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            JSONObject user_inform = (JSONObject) parser.parse(resultJson);

            // token expired check
            String user_email = user_inform.get("email").toString();

            Users user = new Users(condition.getGoogle_user_id(), user_email);
            em.persist(user);

        }catch(Exception e) {
            throw new AuthenticationException();
        }
        try {
            String youtubeRequest = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/subscriptions")
                    .queryParam("part", "snippet").queryParam("mine", true).queryParam("maxResults", 1000)
                    .queryParam("access_token", jwtToken).encode().toUriString();

            String resultYoutube = restTemplate.getForObject(youtubeRequest, String.class);

            Object obj = new Object();
            try {
                obj = parser.parse(resultYoutube);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            channelIdList = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray item = (JSONArray) jsonObject.get("items");
            for (int i = 0; i < item.size(); i++) {
                JSONObject json = (JSONObject) item.get(i);
                JSONObject snippet = (JSONObject) json.get("snippet");
                JSONObject resource = (JSONObject) snippet.get("resourceId");
                channelIdList.add(resource.get("channelId").toString());

            }
        }catch(HttpClientErrorException e){
            return null;
        }
        return channelIdList;
    }

    @Override
    public List<String> updateUsers(UserPostCondition condition) throws AuthenticationException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        List<String> channelIdList = null;
        String jwtToken = condition.getUser_token();
        String resultJson;
        try {
            String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("access_token", jwtToken).encode().toUriString();

            // result
            resultJson = restTemplate.getForObject(requestUrl, String.class);
            System.out.println(resultJson);



        }catch(Exception e){
            throw new AuthenticationException();
        }

            String youtubeRequest = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/subscriptions")
                    .queryParam("part", "snippet").queryParam("mine", true).queryParam("maxResults",1000)
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

            queryFactory.update(users).where(useridEq(condition.getUser_id()))
                    .set(users.user_email, user_email)
                    .set(users.google_user_id, condition.getGoogle_user_id())
                    .execute();


            channelIdList = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray item = (JSONArray) jsonObject.get("items");
            for(int i=0; i<item.size(); i++){
                JSONObject json = (JSONObject) item.get(i);
                JSONObject snippet = (JSONObject) json.get("snippet");
                JSONObject resource = (JSONObject) snippet.get("resourceId");
                channelIdList.add(resource.get("channelId").toString());

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
