package org.example.service;


import org.example.exceptions.UsedEmailException;
import org.example.dto.TweetDto;
import org.example.dto.UserDto;
import org.example.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDto> listUsers(String name) {
        return userRepository.list(name);
    }

    public List<UserDto> getUserByName(String name) {
        return userRepository.getUserByName(name);
    }

    public long addUser(UserDto userDto) throws UsedEmailException {
        List<UserDto> list = userRepository.list("");
        boolean exist = list.stream().filter(user -> user.getEmail()
                        .equals(userDto.getEmail()))
                .collect(Collectors.toList()).isEmpty();
        if (!exist) {
            throw new UsedEmailException(userDto.getEmail());
        }
        return userRepository.insert(userDto);
    }

    public void updateUser(String id, UserDto userDto) throws UsedEmailException {
        List<UserDto> list = userRepository.list("");
        boolean exist = list.stream().filter(user -> user.getEmail()
                        .equals(userDto.getEmail()))
                .collect(Collectors.toList()).isEmpty();
        if (!exist) {
            throw new UsedEmailException(userDto.getEmail());
        }
        userRepository.update(id, userDto);
    }

    public List<TweetDto> getUserTweets(String id) {
        return userRepository.getUserTweets(id);

    }
}
