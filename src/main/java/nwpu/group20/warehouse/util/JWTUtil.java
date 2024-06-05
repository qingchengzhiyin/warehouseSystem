package nwpu.group20.warehouse.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nwpu.group20.warehouse.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
@Component
public class JWTUtil {
    @Value("7")
    public int expiration;
    private static final String key = "group20";
    private static final String PREFIX = "WHSHOU";

    public String creatJWT(User user) throws Exception{
        Map<String,String> map = new HashMap<>();
        map.put("userId", String.valueOf(user.getUserId()));
        map.put("userType", String.valueOf(user.getUserType()));
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, expiration);

        return PREFIX + JWT.create().
                withClaim("user",map).
                withExpiresAt(instance.getTime()).
                sign(Algorithm.HMAC256(key));

    }

    public User getUserFromJWT(String jwt){
        if (jwt.length() <= PREFIX.length()) {
            throw new JWTDecodeException("JWT格式不对");
        }
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(key)).build().verify(jwt.substring(7));
        Map<String, Object> map = decodedJWT.getClaim("user").asMap();
        User user = new User();
        user.setUserId(Integer.parseInt((String) map.get("userId")));
        user.setUserType(Integer.parseInt((String) map.get("userType")));
        return user;
    }
}
