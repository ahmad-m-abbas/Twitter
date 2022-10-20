SELECT t.*
FROM tweet t, `user` u, likes l
WHERE u.id=:userId AND t.id=l.tweet_id AND u.id=l.user_id;