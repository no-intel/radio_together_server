package com.noint.radio_together.member.dto;

public record AccessTokenDto(
        String accessToken,
        long exp
) {
    public static AccessTokenDto of(String accessToken, long exp) {
        return new AccessTokenDto(accessToken, exp);
    }
}
