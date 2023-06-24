# java-filmorate
Template repository for Filmorate project.
![Схема базы данных](/schema.png)
Примеры запросов:
1. Получение списка всех пользователей:
SELECT*
FROM users;
2. Получение списка всех фильмов: 
SELECT*
FROM users;
3. Получение всех фильмов которые лайкнул пользователь с id=1 :
SELECT f.name 
FROM users u
JOIN likes l ON u.id = l.user_id
JOIN films f ON l.film_id = f.id
WHERE u.id=1; 