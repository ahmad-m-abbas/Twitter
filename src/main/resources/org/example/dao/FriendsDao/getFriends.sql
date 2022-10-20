SELECT u.*
FROM `user` u , `friends` f
WHERE f.firstUser=:userId and f.secondUser=u.id;
