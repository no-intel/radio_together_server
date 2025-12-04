package com.noint.radio_together.member.response.auth;

public record LoginResponse(
        Long userId,
        String accessToken,
        String refreshToken,
        String email,
        String name,
        String thumbnail
) {
    public static LoginResponse of(Long userId,
                                   String accessToken,
                                   String refreshToken,
                                   String email,
                                   String name,
                                   String thumbnail) {

        return new LoginResponse(userId, accessToken, refreshToken, email, name, thumbnail);
    }
}
