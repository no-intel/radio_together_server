package com.noint.radio_together.member.controller;

import com.noint.radio_together.member.request.AuthCodeRequest;
import com.noint.radio_together.member.response.LoginResponse;
import com.noint.radio_together.member.service.GoogleOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final GoogleOauthService googleOauthService;

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> authGoogle(@RequestBody AuthCodeRequest request){
        LoginResponse loginResponse = googleOauthService.googleLogin(request);
        return ResponseEntity.ok(loginResponse);
    }
}
