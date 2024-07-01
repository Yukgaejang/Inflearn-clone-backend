package com.yukgaejang.inflearnclone.domain.application;

import com.yukgaejang.inflearnclone.domain.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.dto.CustomOAuth2User;
import com.yukgaejang.inflearnclone.domain.dto.KakaoResponse;
import com.yukgaejang.inflearnclone.domain.dto.OAuth2Response;
import com.yukgaejang.inflearnclone.domain.dto.UserDto;
import com.yukgaejang.inflearnclone.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserDao userDao;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String registration = request.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        if("kakao".equals(registration)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String userEmail = oAuth2Response.getEmail();

        User existData = userDao.findByEmail(userEmail);

        if(existData == null) {
            User user = new User();
            user.setNickname(oAuth2Response.getName());
            user.setEmail(oAuth2Response.getEmail());
            user.setProfileImage(oAuth2Response.getProfileImage());
            user.setSocialType("KAKAO");

            userDao.save(user);

            UserDto userDto = new UserDto();
            userDto.setUserName(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId());
            userDto.setName(oAuth2Response.getName());
            userDto.setProfileImage(oAuth2Response.getProfileImage());
            userDto.setRole("ROLE_USER");

            return new CustomOAuth2User(userDto);
        } else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setNickname(oAuth2Response.getName());
            existData.setProfileImage(oAuth2Response.getProfileImage());

            userDao.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUserName(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId());
            userDto.setName(existData.getNickname());
            userDto.setProfileImage(existData.getProfileImage());
            userDto.setRole("ROLE_USER");

            return new CustomOAuth2User(userDto);
        }
    }
}