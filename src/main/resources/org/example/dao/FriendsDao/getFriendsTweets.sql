SELECT t.*
FROM `user` u , `friends` f , `tweet` t
WHERE f.firstUser=:userId and f.secondUser=u.id and t.user_id = u.id
ORDER BY t.created_on DESC;
