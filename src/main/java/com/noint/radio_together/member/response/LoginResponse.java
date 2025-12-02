package com.noint.radio_together.member.response;

public record LoginResponse(
        String accessToken,
        String idToken,
        String email,
        String name,
        String thumbnail
) {
    public static LoginResponse of(String accessToken,
                                   String idToken,
                                   String email,
                                   String name,
                                   String thumbnail) {

        return new LoginResponse(accessToken, idToken, email, name, thumbnail);
    }
}
