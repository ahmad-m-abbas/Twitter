DELETE FROM `friends`
WHERE (firstUser=:firstUser and secondUser=:secondUser)
OR (secondUser=:firstUser and firstUser=:secondUser);