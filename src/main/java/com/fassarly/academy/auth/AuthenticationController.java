package com.fassarly.academy.auth;


import com.fassarly.academy.DTO.AuthenticationRequest;
import com.fassarly.academy.DTO.AuthenticationResponse;
import com.fassarly.academy.DTO.RegisterRequest;
import com.fassarly.academy.SmsToken.VerifySmsRequest;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200/")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws PSQLException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logout(@RequestParam("numtel") String numtel) {
        return ResponseEntity.ok(service.logout(numtel));
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<AuthenticationResponse> verifySmsCode(@RequestBody VerifySmsRequest verifySmsRequest) {
        try {
            AuthenticationResponse response = service.verifyCode(verifySmsRequest.getPhoneNumber(), verifySmsRequest.getVerificationCode());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthenticationResponse.builder()
                            .errormessage("An error occurred during SMS verification.")
                            .build());
        }
    }

}
