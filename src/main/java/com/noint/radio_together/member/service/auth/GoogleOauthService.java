package com.noint.radio_together.member.service.auth;

import com.noint.radio_together.member.dto.auth.AccessTokenDto;
import com.noint.radio_together.member.dto.auth.AuthTokenDto;
import com.noint.radio_together.member.dto.auth.IdTokenMemberDto;
import com.noint.radio_together.member.entity.Member;
import com.noint.radio_together.member.repository.auth.TokenRedisRepository;
import com.noint.radio_together.member.request.auth.AuthCodeRequest;
import com.noint.radio_together.member.response.auth.LoginResponse;
import com.noint.radio_together.member.service.GetMemberService;
import com.noint.radio_together.member.service.RegisterMemberService;
import com.noint.radio_together.member.util.auth.IdTokenParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauthService {
    private final WebClient webClient;
    private final IdTokenParseUtil idTokenParseUtil;
    private final GetMemberService getMemberService;
    private final TokenService tokenService;
    private final RefreshTokenRegistrationService refreshTokenRegistrationService;
    private final RegisterMemberService registerMemberService;
    private final TokenRedisRepository tokenRedisRepository;


    final static String TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public LoginResponse googleLogin(AuthCodeRequest request) {
        // 1. authCode로 Access Token 요청
        AuthTokenDto tokenDto = authenticate(request.authCode(), request.redirectUri(), request.codeVerifier());

        IdTokenMemberDto tokenMember = idTokenParseUtil.getMember(tokenDto.idToken());
        Member member = getMemberService.getNullAbleMemberByEmail(tokenMember.email())
                .orElseGet(() -> {
                    // member가 등록이 안돼있으면 null -> 회원가입 시켜주기
                    Member newMember = Member.of(tokenMember.email(),
                            tokenMember.name(),
                            tokenMember.picture(),
                            tokenMember.auth()
                    );
                    registerMemberService.registerMember(newMember);
                    return newMember;
                });

        AccessTokenDto accessToken = tokenService.createAccessToken(member);
        String refreshToken = tokenService.createRefreshToken();

        refreshTokenRegistrationService.RegisterRefreshToken(member, refreshToken);
        tokenRedisRepository.RegisterAccessToken(
                member.getId(),
                accessToken.accessToken(),
                accessToken.exp() - Instant.now().getEpochSecond()
        );

        return LoginResponse.of(
                member.getId(),
                accessToken.accessToken(),
                refreshToken,
                member.getEmail(),
                member.getName(),
                member.getThumbnail()
        );
    }

    private AuthTokenDto authenticate(String authCode, String redirectUri, String codeVerifier) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authCode);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code_verifier", codeVerifier);
        params.add("grant_type", "authorization_code");

        return webClient.post()
                .uri(TOKEN_URL)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(AuthTokenDto.class)
                .block()
                ;
    }
}
