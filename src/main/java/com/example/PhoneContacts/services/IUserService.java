package com.example.PhoneContacts.services;

import com.example.PhoneContacts.dto.UserDto;

public interface IUserService {
    boolean createUser(UserDto userDto);
}