package com.noint.radio_together.member.util.auth;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.noint.radio_together.member.dto.auth.IdTokenMemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.text.ParseException;

@Slf4j
@Component
public class IdTokenParseUtil {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public IdTokenMemberDto getMember(String idToken) {
        JWTClaimsSet jwtClaimsSet = verifyAndParseIdToken(idToken);
        try {
            String issuer = jwtClaimsSet.getIssuer();
            String email = jwtClaimsSet.getStringClaim("email");
            String name = jwtClaimsSet.getStringClaim("name");
            String picture = jwtClaimsSet.getStringClaim("picture");

            return IdTokenMemberDto.of(issuer, email, name, picture);

        } catch (ParseException e) {
            log.error("{}", String.valueOf(e));
            throw new RuntimeException();
        }
    }

    private JWTClaimsSet verifyAndParseIdToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);

            JWSHeader header = signedJWT.getHeader();
            String keyId = header.getKeyID();

            JWKSet jwkSet = loadGoogleJwkSet();
            JWK jwk = jwkSet.getKeyByKeyId(keyId);
            if (jwk == null) {
                throw new RuntimeException("No JWK found for keyId: " + keyId);
            }

            RSAKey rsaKey = jwk.toRSAKey();
            JWSVerifier verifier = new RSASSAVerifier(rsaKey);
            boolean isValid = signedJWT.verify(verifier);
            if (!isValid) {
                throw new RuntimeException("Invalid id_token signature");
            }

            return getJwtClaimsSet(signedJWT);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify/parse id_token", e);
        }
    }

    private JWTClaimsSet getJwtClaimsSet(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        // 추가 검증: iss, aud, exp
        String issuer = claims.getIssuer();
        if (!"https://accounts.google.com".equals(issuer)
                && !"accounts.google.com".equals(issuer)) {
            throw new RuntimeException("Invalid issuer: " + issuer);
        }

        if (!claims.getAudience().contains(clientId)) {
            throw new RuntimeException("Invalid audience: " + claims.getAudience());
        }
        return claims;
    }

    private JWKSet loadGoogleJwkSet() {
        try {
            URL jwkSetUrl = new URI("https://www.googleapis.com/oauth2/v3/certs").toURL();
            return JWKSet.load(jwkSetUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Google JWKSet", e);
        }
    }
}
