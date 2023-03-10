package com.food.auth.provider;

import com.food.auth.provider.dto.AccessToken;
import com.food.auth.provider.dto.AccessTokenContent;
import com.food.auth.provider.dto.AccessTokenValidationResult;

public interface AccessTokenProvider {
    AccessToken create(Long userId);
    AccessTokenValidationResult validate(AccessToken token);
    AccessTokenContent getContent(AccessToken token);
}
