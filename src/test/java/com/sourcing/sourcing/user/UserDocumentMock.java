package com.sourcing.sourcing.user;

public class UserDocumentMock extends UserDocument {

    private UserDocumentMock(String id, String username) {
        super(id, username);
    }

    public static UserDocument create(String id, String username) {
        return new UserDocument(id, username);
    }
}
