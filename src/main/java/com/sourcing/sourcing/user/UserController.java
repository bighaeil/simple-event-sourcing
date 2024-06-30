package com.sourcing.sourcing.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.getUserId(), request.getUsername());
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable String userId, @RequestParam String newUsername) {
        userService.updateUser(userId, newUsername);
    }

    @GetMapping("/{userId}")
    public UserDocument getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }
}
