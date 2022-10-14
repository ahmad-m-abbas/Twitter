package org.example.dao;

import io.sentry.event.User;
import org.example.dto.FriendsDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface FriendsDao {

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> getFriendsTweets(@Bind("userId") String userId);

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(UserDto.class)
    List<UserDto> getFriends(@Bind("userId") String userId);

    @SqlUpdate
    @UseClasspathSqlLocator
    void addFriends(@BindBean FriendsDto friendsDto);
}
