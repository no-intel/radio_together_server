package com.noint.radio_together.member.request.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthCodeRequest(
        @NotBlank
        String authCode,
        @NotBlank
        String redirectUri,
        @NotBlank
        String codeVerifier
) {
}
