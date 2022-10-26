package org.example;

import org.example.dto.UserDto;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserApiTest extends TwitterTest{

    @Test
    public void create_user_test() {
        startActor((server, client) -> {
            UserDto userToCreate = new UserDto();
            userToCreate.setName("Ahmad Abbas");
            userToCreate.setEmail("ahmad@test.ps");

            String idDto = parseBody(client.post("/api/user/", userToCreate), String.class);
            UserDto createdUser = parseBody(
                    client.get(String.format("/api/user/%s", idDto)),
                    UserDto.class
            );

            assertEquals(userToCreate.getName(), createdUser.getName());
            assertEquals(userToCreate.getEmail(), createdUser.getEmail());
        }, "tayserr", "tayseer@gmail.com", String.valueOf(Role.USER));

    }
}
