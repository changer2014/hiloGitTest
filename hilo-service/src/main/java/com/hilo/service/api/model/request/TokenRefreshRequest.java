package com.hilo.service.api.model.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {

	private String refreshToken;
}
