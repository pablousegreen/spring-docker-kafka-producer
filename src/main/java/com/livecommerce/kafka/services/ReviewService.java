package com.livecommerce.kafka.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Value("${val.online.auth.psw}")
    private String pswValOnline;

    @Value("${val.online.auth.user}")
    private String userValOnline;

    private static final String AUTH0 = "auth0";
    public static final String MERCHANT_ID = "merchantId";
    private static final String ACTIONS = "actions";

    public String getOnboardingUserToken(String userId) {
        Builder jwtBuilder = JWT.create();
        jwtBuilder.withIssuer("auth0");
        jwtBuilder.withSubject(userId);
        jwtBuilder.withClaim(ACTIONS, this.getActions(userId));
        jwtBuilder.withIssuedAt(LocalDateTime.now().toDate());
        jwtBuilder.withExpiresAt(LocalDateTime.now().plusHours(3).toDate());
        //getOpenpayCognitoAuth(jwtBuilder);
        Algorithm algorithm = Algorithm.HMAC256(this.pswValOnline);
        return jwtBuilder.sign(algorithm);
    }

    public List<String> getActions(String userId) {
        List<String> actions = new ArrayList<>();

        return actions;
    }

    public String getValOnlineToken() {
        return this.getValOnlineToken("mkv2k0eh1tx2yoxtcbcd", "LOCAL", "pablo.gonzalez+c002@openpay.mx", "MEX");
    }

    public String getValOnlineToken(final String merchantId, final String environment, final String email, String country) {
        Builder jwtBuilder = JWT.create();
        jwtBuilder.withIssuer(AUTH0);
        jwtBuilder.withClaim("userValOnline", this.userValOnline);
        jwtBuilder.withClaim(MERCHANT_ID, merchantId);
        jwtBuilder.withClaim("environment", environment);
        jwtBuilder.withClaim("email", email);
        jwtBuilder.withIssuedAt(LocalDateTime.now().toDate());
        jwtBuilder.withExpiresAt(LocalDateTime.now().plusHours(3).toDate());
        //this.addReferencedClaims(jwtBuilder, mapReferencedParams);
        //getOpenpayCognitoAuth(merchantId, country, jwtBuilder, StringUtils.EMPTY);
        Algorithm algorithm = Algorithm.HMAC256(this.pswValOnline);
        return jwtBuilder.sign(algorithm);
    }
}
