package com.noint.radio_together.member.dto.auth;

public record AccessTokenDto(
        String accessToken,
        long exp
) {
    public static AccessTokenDto of(String accessToken, long exp) {
        return new AccessTokenDto(accessToken, exp);
    }
}
