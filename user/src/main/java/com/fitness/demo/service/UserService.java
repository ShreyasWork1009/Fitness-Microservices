package com.fitness.demo.service;

import com.fitness.demo.dto.RegisterRequest;
import com.fitness.demo.dto.UserResponse;
import com.fitness.demo.model.User;
import com.fitness.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserProfile(String userId)
    {
        User user= userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setPassword(user.getPassword());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        userResponse.setCreatedAt(user.getCreatedAt());
        return userResponse;

    }

    public UserResponse register(RegisterRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already exists");
        }
        User user=new User();
        user.setEmail((request.getEmail()));
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser= userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        userResponse.setCreatedAt(savedUser.getCreatedAt());

        return userResponse;



    }

    public Boolean getUserById(String userId)
    {
        return userRepository.existsById(userId);
    }

}
