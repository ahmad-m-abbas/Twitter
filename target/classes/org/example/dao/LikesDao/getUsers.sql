select u.*
from tweet t, `user` u, likes l
where t.id=:tweetId and t.id=l.tweet_Id and u.id=l.user_Id