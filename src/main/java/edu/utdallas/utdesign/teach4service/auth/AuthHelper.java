package edu.utdallas.utdesign.teach4service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.io.BaseEncoding;
import edu.utdallas.utdesign.teach4service.auth.csrfAuth.NewAuthCreds;
import edu.utdallas.utdesign.teach4service.db.entities.AdminUser;
import edu.utdallas.utdesign.teach4service.db.entities.User;
import edu.utdallas.utdesign.teach4service.db.entities.UserStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.joda.time.DateTime;

import javax.ws.rs.core.NewCookie;
import java.util.Date;
import java.util.Random;

import static edu.utdallas.utdesign.teach4service.T4SConfiguration.AuthConfig;

public class AuthHelper
{
    public static final String COOKIE_NAME = "utdesign.Teach4Service";
    public static final String CSRF_HEADER = "X-CSRF";

    public static final String ROLE_ADMIN   = "ADMIN";
    public static final String ROLE_USER    = "NON_ADMIN";
    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_TUTOR   = "TUTOR";
    public static final String ROLE_MANAGER = "MANAGER";

    private static final String JWT_ISSUER   = "Teach4Service";
    private static final String JWT_AUDIENCE = JWT_ISSUER;

    private static final String JWT_CLAIM_USER    = "userId";
    private static final String JWT_CLAIM_TUTOR   = "tutorId";
    private static final String JWT_CLAIM_STUDENT = "studentId";
    private static final String JWT_CLAIM_MANAGER = "isManager";
    private static final String JWT_CLAIM_STATUS  = "status";
    private static final String JWT_CLAIM_ISAMDIN = "isAdmin";
    private static final String JWT_CLAIM_CSRF    = "csrf-token";

    private final Random rand = new Random();

    @Getter
    private final AuthConfig config;

    private final Algorithm   key;
    private final JWTVerifier verifier;

    @SneakyThrows
    public AuthHelper(AuthConfig config)
    {
        this.config = config;
        key = Algorithm.HMAC512(config.getCookieSecret());
        verifier = JWT.require(key)
                .withIssuer(JWT_ISSUER)
                .withAudience(JWT_AUDIENCE)
                .acceptExpiresAt(5)
                .build();
    }

    @NonNull
    public NewAuthCreds buildCredentials(@NonNull AdminUser user)
    {
        SessionToken token = new SessionToken();

        token.setUserId(user.getId());
        token.setStatus(UserStatus.ACTIVE);
        token.setAdmin(true);

        return buildCredentials(token);
    }

    @NonNull
    public NewAuthCreds buildCredentials(@NonNull User user)
    {
        SessionToken token = new SessionToken();

        token.setUserId(user.getId());
        token.setTutorId(user.getTutorId() == null ? 0 : user.getTutorId());
        token.setStudentId(user.getStudentId() == null ? 0 : user.getStudentId());
        token.setStudentManager(user.isStudentManager());
        token.setStatus(user.getStatus());
        token.setAdmin(false);

        return buildCredentials(token);
    }

    @NonNull
    public NewAuthCreds buildCredentials(@NonNull SessionToken token)
    {
        String csrf = genCsrf();
        String jwt = buildJwt(token, csrf);

        return new NewAuthCreds(
                new NewCookie(
                        COOKIE_NAME,
                        jwt,
                        "/",
                        null,
                        null,
                        config.getCookieAge() + 60,
                        false,
                        true
                ),
                csrf
        );
    }

    @NonNull
    public NewCookie buildLogoutCookie()
    {
        return new NewCookie(
                COOKIE_NAME,
                "",
                "/",
                null,
                null,
                1,
                false,
                true
        );
    }

    /**
     * Generates 30 bytes of random and Base64 encodes it into a string
     *
     * @return 30 bytes of base64 encoded random
     */
    private String genCsrf()
    {
        byte[] buffer = new byte[30];
        rand.nextBytes(buffer);
        return BaseEncoding.base64().encode(buffer);
    }

    private String buildJwt(SessionToken jwtInfo, String csrf)
    {
        return JWT.create()
                .withIssuer(JWT_ISSUER)
                .withAudience(JWT_AUDIENCE)
                .withSubject("User" + jwtInfo.getUserId())

                .withIssuedAt(new Date())
                .withExpiresAt(DateTime.now().plusSeconds(config.getCookieAge()).toDate())
                .withNotBefore(new Date())

                .withClaim(JWT_CLAIM_USER, jwtInfo.getUserId())
                .withClaim(JWT_CLAIM_TUTOR, jwtInfo.getTutorId())
                .withClaim(JWT_CLAIM_STUDENT, jwtInfo.getStudentId())
                .withClaim(JWT_CLAIM_MANAGER, jwtInfo.isStudentManager())
                .withClaim(JWT_CLAIM_STATUS, jwtInfo.getStatus().ordinal())
                .withClaim(JWT_CLAIM_ISAMDIN, jwtInfo.isAdmin())

                .withClaim(JWT_CLAIM_CSRF, csrf)

                .sign(key);
    }

    @NonNull
    public DecodedJWT verifyJwt(@NonNull String jwt) throws JWTVerificationException
    {
        return verifier.verify(jwt);
    }

    /**
     * Validates a CSRF token against a decoded JWT.
     *
     * @param jwt a decode JWT
     * @param expectedCsrf the expected CSRF token from the header
     * @return TRUE if valid, FALSE if not.
     */
    public boolean verifyCSRF(DecodedJWT jwt, String expectedCsrf)
    {
        if(jwt == null || expectedCsrf == null)
        {
            return false;
        }

        return expectedCsrf.equals(jwt.getClaim(JWT_CLAIM_CSRF).asString());
    }

    @NonNull
    public SessionToken decodeJwt(@NonNull DecodedJWT jwt)
    {
        SessionToken token = new SessionToken();

        token.setExpiration(jwt.getExpiresAt());

        token.setUserId(jwt.getClaim(JWT_CLAIM_USER).asLong());
        token.setTutorId(jwt.getClaim(JWT_CLAIM_TUTOR).asLong());
        token.setStudentId(jwt.getClaim(JWT_CLAIM_STUDENT).asLong());
        token.setStudentManager(jwt.getClaim(JWT_CLAIM_MANAGER).asBoolean());
        token.setStatus(UserStatus.values()[jwt.getClaim(JWT_CLAIM_STATUS).asInt()]);
        token.setAdmin(jwt.getClaim(JWT_CLAIM_ISAMDIN).asBoolean());

        return token;
    }
}
