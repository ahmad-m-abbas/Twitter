package org.example.dao;

import io.sentry.event.User;
import org.example.dao.query.SearchFriendQuery;
import org.example.dao.query.SearchTweetQuery;
import org.example.dto.FriendsDto;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.AllowUnusedBindings;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

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
    @Transaction
    void addFriends(@BindBean FriendsDto friendsDto);

    @SqlUpdate
    @UseClasspathSqlLocator
    void delete(@BindBean FriendsDto friendsDto);

    @SqlQuery
    @UseStringTemplateSqlLocator
    @RegisterBeanMapper(UserDto.class)
    @AllowUnusedBindings
    List<UserDto> search(@Define("userId") String userId,@Define("query") SearchFriendQuery query);

}
