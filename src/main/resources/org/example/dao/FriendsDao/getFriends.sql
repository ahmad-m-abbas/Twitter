SELECT u.*
FROM `user` u , `friends` f
WHERE CASE
    WHEN f.firstUser=:userId THEN f.secondUser=u.id
    WHEN f.secondUser=:userId THEN f.firstUser=u.id
END
;
