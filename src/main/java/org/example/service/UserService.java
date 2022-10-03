package org.example.service;


import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.Map;

public class UserService {

    private static UserService instance = null;
    private final UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.instance();
    }

    public static UserService instance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public UserDto findUser(String userId) {

        return userRepository.get(userId);
    }

    public List<UserDto> listUsers() {
        return userRepository.list();
    }

    public long addUser(UserDto userDto) {
        return userRepository.insert(userDto);
    }

    public void updateUser(String id, UserDto userDto) {
        userRepository.update(id, userDto);
    }

    public List<TweetDto> getUserTweets(String id){
        return userRepository.getUserTweets(id);

    }
}
