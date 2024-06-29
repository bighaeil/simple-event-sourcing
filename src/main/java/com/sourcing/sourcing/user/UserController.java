package com.sourcing.sourcing.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserAggregate getUser(@PathVariable String userId) {
        return userService.getUserAggregate(userId);
    }

    @PostMapping
    public void createUser(@RequestParam String userId, @RequestParam String username) {
        userService.createUser(userId, username);
    }
}
