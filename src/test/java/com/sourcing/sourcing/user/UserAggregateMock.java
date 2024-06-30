package com.sourcing.sourcing.user;

import com.sourcing.sourcing.event.UserAggregate;

public class UserAggregateMock extends UserAggregate {

    private UserAggregateMock(String userId, String username) {
        super(userId, username);
    }

    public static UserAggregate create(String userId, String username) {
        return new UserAggregateMock(userId, username);
    }
}
