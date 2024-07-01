package com.yukgaejang.inflearnclone.domain.application;

import com.yukgaejang.inflearnclone.domain.board.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.board.dto.UserDto;
import com.yukgaejang.inflearnclone.domain.dto.CustomOAuth2User;
import com.yukgaejang.inflearnclone.domain.dto.KakaoResponse;
import com.yukgaejang.inflearnclone.domain.dto.OAuth2Response;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
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

        if ("kakao".equals(registration)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String userEmail = oAuth2Response.getEmail();

        User existData = userDao.findByEmail(userEmail);

        if (existData == null) {
            User user = new User(oAuth2Response.getName(), oAuth2Response.getEmail(), LoginType.KAKAO);

            userDao.save(user);

            UserDto userDto = new UserDto();
            userDto.setUserName(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId());
            userDto.setNickname(oAuth2Response.getName());
            userDto.setRole("ROLE_USER");

            return new CustomOAuth2User(userDto);
        } else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setNickname(oAuth2Response.getName());

            userDao.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUserName(oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId());
            userDto.setNickname(existData.getNickname());
            userDto.setRole("ROLE_USER");

            return new CustomOAuth2User(userDto);
        }
    }
}