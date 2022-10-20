package org.example.dao;

import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.AllowUnusedBindings;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import java.util.List;

public interface UserDao {

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(UserDto.class)
    UserDto getUser(@Bind("id") String userId);

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> getUserTweets(@Bind("id") String userId);

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(UserDto.class)
    List<UserDto> getUserByName(@Bind("name") String name);

    @SqlUpdate
    @UseClasspathSqlLocator
    @GetGeneratedKeys
    long insert(@BindBean UserDto userDto);

    @SqlUpdate
    @UseClasspathSqlLocator
    void update(@Bind("id") String id, @BindBean("user") UserDto userDto);

    @SqlQuery
    @UseStringTemplateSqlLocator
    @RegisterBeanMapper(UserDto.class)
    @AllowUnusedBindings
    List<UserDto> list(@Define("userName") String name);
}
