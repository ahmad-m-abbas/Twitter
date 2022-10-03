select tweet.*
from tweet t, uset u, likes l
where u.id=:userId and t.id=l.tweetId and u.id=l.userId