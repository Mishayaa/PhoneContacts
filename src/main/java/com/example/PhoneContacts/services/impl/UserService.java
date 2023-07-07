package com.example.PhoneContacts.services.impl;

import com.example.PhoneContacts.dto.UserDto;
import com.example.PhoneContacts.entities.User;
import com.example.PhoneContacts.repos.RoleRepository;
import com.example.PhoneContacts.repos.UserRepository;
import com.example.PhoneContacts.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public boolean createUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            return false;
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByTitle("USER")));
        userRepository.save(user);
        return true;
    }

}