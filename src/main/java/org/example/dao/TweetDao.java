package org.example.dao;

import org.example.dao.query.SearchTweetQuery;
import org.example.dto.TweetDto;
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

public interface TweetDao {


    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(TweetDto.class)
    List<TweetDto> list();

    @SqlQuery
    @UseClasspathSqlLocator
    @RegisterBeanMapper(TweetDto.class)
    TweetDto getTweet(@Bind("id") String tweetId);

    @SqlUpdate
    @UseClasspathSqlLocator
    void insert(@BindBean TweetDto tweetDto);

    @SqlUpdate
    @UseClasspathSqlLocator
    void update(@Bind("id") String id, @BindBean("tweet") TweetDto tweetDto);

    @SqlUpdate
    @UseClasspathSqlLocator
    void delete(@Bind("id") String id);

    @SqlQuery
    @UseStringTemplateSqlLocator
    @RegisterBeanMapper(TweetDto.class)
    @AllowUnusedBindings
    List<TweetDto> search(@Define("query") SearchTweetQuery query);


}
