package com.food.common.user.business.internal.impl;

import com.food.common.store.business.StoreOwnerEntityService;
import com.food.common.store.domain.StoreOwner;
import com.food.common.user.business.internal.UserCommonService;
import com.food.common.user.business.internal.dto.UserDto;
import com.food.common.user.domain.User;
import com.food.common.user.enumeration.Role;
import com.food.common.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class DefaultUserCommonService implements UserCommonService {
    private final UserEntityService userEntityService;
    private final StoreOwnerEntityService storeOwnerService;
    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::new);
    }

    public Role findRoleById(Long id) {
        User user = userEntityService.findById(id);

        Optional<StoreOwner> foundStoreOwner = storeOwnerService.findByUser(user);
        return foundStoreOwner.isPresent() ? Role.STORE_OWNER : Role.CUSTOMER;
    }
}

