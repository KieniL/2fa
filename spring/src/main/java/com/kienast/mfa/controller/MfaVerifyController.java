package com.kienast.mfa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kienast.mfa.model.TokenVerification;

import dev.samstevens.totp.code.CodeVerifier;

@Controller
public class MfaVerifyController {
    @Autowired
    private CodeVerifier verifier;

    @PostMapping("/mfa/verify")
    @ResponseBody
    public String verify(@RequestBody TokenVerification token) {
        // secret is fetched from some storage

        if (verifier.isValidCode(token.getSecret(), token.getToken())) {
            return "CORRECT CODE";
        }

        return "INCORRECT CODE";
    }
}