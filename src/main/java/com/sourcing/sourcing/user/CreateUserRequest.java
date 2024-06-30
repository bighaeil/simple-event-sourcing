package com.sourcing.sourcing.user;

import lombok.Data;

@Data
class CreateUserRequest {
    private String userId;
    private String username;
}
