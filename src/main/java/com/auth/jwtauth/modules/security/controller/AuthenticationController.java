package com.auth.jwtauth.modules.security.controller;

import com.auth.jwtauth.modules.security.domain.AuthenticationRequest;
import com.auth.jwtauth.modules.security.domain.AuthenticationResponse;
import com.auth.jwtauth.modules.security.jwt.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "authenticate", tags = {"authenticate"})
@RequestMapping("authenticate")
public class AuthenticationController
{
    private final AuthenticationManager authenticationManager;
    private final TokenUtil tokenUtil;

    public AuthenticationController (AuthenticationManager authenticationManager, TokenUtil tokenUtil)
    {
        this.authenticationManager = authenticationManager;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    @ApiOperation(value="Authenticate user with username or email and password.", nickname="authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest)
    {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getLogin(),
                authenticationRequest.getPassword());

        Authentication successfulAuthentication = authenticationManager.authenticate(authentication);

        String token = tokenUtil.createToken(successfulAuthentication);

        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
    }

}
