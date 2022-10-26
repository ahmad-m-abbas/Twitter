package org.example.dao.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchTweetQuery {
    private final String name;
    private final String toBeIn;

    private final String orderBy;

    public String getOrderBy() {
        if(orderBy==null){
            return null;
        }
        switch (orderBy) {
            case "name":
            case "Date":
                return orderBy;
            default:
                return null;
        }
    }


}
