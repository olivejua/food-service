package com.food.common.user.business.dto.response.accountFind;

import com.food.common.user.enumeration.AccountType;
import com.food.common.user.enumeration.Role;
import com.food.common.user.business.dto.response.accountDomain.FoundAppAccount;
import com.food.common.user.business.dto.response.accountDomain.FoundSocialAccount;
import com.food.common.user.business.dto.response.userDomain.FoundUser;
import lombok.Getter;

@Getter
public class AccountFindResponse {
    private AccountType accountType;
    private String loginId;
    private String password;
    private FoundUser user;

    public AccountFindResponse(FoundAppAccount account) {
        accountType = AccountType.APP;
        loginId = account.getLoginId();
        password = account.getPassword();
        user = account.getUser();
    }

    public AccountFindResponse(FoundSocialAccount account) {
        accountType = AccountType.SOCIAL;
        loginId = account.getLoginId();
        user = account.getUser();
    }

    public Long getUserId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
