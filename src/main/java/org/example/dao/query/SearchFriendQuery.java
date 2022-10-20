package org.example.dao.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchFriendQuery {
    private final String name;
    private final String orderBy;
    private final String order;

}
