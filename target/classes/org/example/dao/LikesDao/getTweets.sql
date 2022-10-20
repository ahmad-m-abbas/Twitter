select t.*
from tweet t, `user` u, likes l
where u.id=:userId and t.id=l.tweet_id and u.id=l.user_id