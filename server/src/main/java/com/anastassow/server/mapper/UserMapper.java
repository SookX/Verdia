package com.anastassow.server.mapper;

import com.anastassow.server.dto.UserDto;
import com.anastassow.server.models.User;

public class UserMapper {
    
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }
    
}
