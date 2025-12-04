package com.noint.radio_together.member.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthTokenDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        String expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("id_token")
        String idToken
) {
}
