package org.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TweetDto {
    private String id;
    private String userId;
    private String text;
    private Date created_on;
}
