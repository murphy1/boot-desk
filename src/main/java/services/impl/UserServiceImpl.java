package services.impl;

import model.User;
import org.springframework.stereotype.Service;
import repositories.UserRepository;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        userRepository.findAll().iterator().forEachRemaining(users :: add);

        return users;
    }
}
