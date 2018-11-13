package com.hilo.service.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hilo.service.api.confiuration.security.InvalidJwtTokenException;
import com.hilo.service.api.confiuration.security.JwtTokenProvider;
import com.hilo.service.api.model.request.TokenRefreshRequest;
import com.scooter.common.api.JsonResponse;
import com.scooter.common.api.LogicException;

@RestController
@RequestMapping(value = "/token")
public class TokenController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    /**
     * Refresh json response.
     *
     * @param tokenRefresh the token refresh
     * @return the json response
     */
    @PostMapping(value = "/refresh")
    public JsonResponse<?> refresh(@RequestBody TokenRefreshRequest tokenRefresh) {
        try {
            String phone = this.jwtTokenProvider.parseRefreshToken(tokenRefresh.getRefreshToken());
            //find user by phone
            String accessToken = this.jwtTokenProvider.getAccessToken(null);
            return JsonResponse.success(accessToken);
        } catch (InvalidJwtTokenException e) {
            throw new LogicException("Token.invalid");
        }
    }

    /**
     * Is valid json response.
     *
     * @return the json response
     */
    @PostMapping(value = "/validate")
    public JsonResponse<?> isValid() {
        // If request can get here, it indicates that this request has valid jwt token
        return JsonResponse.success();
    }

}
