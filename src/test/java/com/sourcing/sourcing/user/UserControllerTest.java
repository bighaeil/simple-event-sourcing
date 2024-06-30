package com.sourcing.sourcing.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void API_TEST() {
        //given
        String userId = "1";
        BDDMockito.given(userService.getUser(userId)).willReturn(
                UserDocumentMock.create(userId, "test")
        );

        //when
        UserDocument user = userController.getUser(userId);

        //then
        assertEquals(userId, user.getId());
    }
}