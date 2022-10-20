SELECT u.*
FROM tweet t, `user` u, likes l
WHERE t.id=:tweetId AND t.id=l.tweet_Id AND u.id=l.user_Id