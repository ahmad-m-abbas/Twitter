SELECT t.*
FROM `user` u , `friends` f , `tweet` t
WHERE CASE
          WHEN f.firstUser=:userId THEN f.secondUser=u.id and t.id = u.id
          WHEN f.secondUser=:userId THEN f.firstUser=u.id and t.id = u.id END;
