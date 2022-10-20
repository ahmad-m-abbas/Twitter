package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendsDto {

    private String firstUser;
    private String secondUser;
    private Date dayOfFriendShip;
}
