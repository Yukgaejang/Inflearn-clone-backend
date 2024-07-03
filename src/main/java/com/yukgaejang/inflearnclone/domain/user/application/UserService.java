package com.yukgaejang.inflearnclone.domain.user.application;

import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User createUser(CreateUserDto createUserDto) {
        User user = User.builder()
                .nickname(createUserDto.getNickname())
                .email(createUserDto.getEmail())
                .loginType(createUserDto.getLoginType())
                .build();
        return userDao.save(user);
    }
}
