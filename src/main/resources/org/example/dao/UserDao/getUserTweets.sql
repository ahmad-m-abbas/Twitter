select t.*
from `user` u, `tweet` t
where u.id=:id and u.id=t.user_id

