package ru.yandex.practicum.filmorate.storage.user.friend;

public interface UserFrendsStorage {
    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

}
