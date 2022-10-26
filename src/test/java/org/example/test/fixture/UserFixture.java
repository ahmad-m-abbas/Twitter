package org.example.fixture;

import org.example.dto.UserDto;
import org.example.exceptions.UsedEmailException;
import org.example.service.UserService;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFixture {
    public static List<UserDto> create(int count) throws UsedEmailException {
        List<UserDto> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDto user = make();
            UserService.instance().addUser(user);
            users.add(user);
        }

        return users;
    }

    public static UserDto make() {
        UserDto userToCreate = new UserDto();
        userToCreate.setId(UUID.randomUUID().toString());
        userToCreate.setName(RandomStringUtils.randomAlphabetic(30));
        userToCreate.setEmail(RandomStringUtils.randomAlphabetic(30));

        return userToCreate;
    }
}
