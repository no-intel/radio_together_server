package com.noint.radio_together.member.dto.auth;

import com.noint.radio_together.member.enums.auth.AuthEnum;

import static com.noint.radio_together.member.enums.auth.AuthEnum.APPLE;
import static com.noint.radio_together.member.enums.auth.AuthEnum.GOOGLE;

public record IdTokenMemberDto(
        AuthEnum auth,
        String email,
        String name,
        String picture
) {
    public static IdTokenMemberDto of(String iss, String email, String name, String picture) {
        AuthEnum auth = GOOGLE;
        if (!iss.contains("google")){
            auth = APPLE;
        }
        return new IdTokenMemberDto(auth, email, name, picture);
    }
}
