package com.yukgaejang.inflearnclone.domain.application;

import com.yukgaejang.inflearnclone.domain.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.dto.CustomOAuth2User;
import com.yukgaejang.inflearnclone.domain.dto.KakaoResponse;
import com.yukgaejang.inflearnclone.domain.dto.OAuth2Response;
import com.yukgaejang.inflearnclone.domain.dto.UserDto;
import com.yukgaejang.inflearnclone.domain.model.UserEntity;
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

        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();

        UserEntity existData = userDao.findByUserName(username);

        if(existData == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(username);
            userEntity.setName(oAuth2Response.getName());
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setProfileImage(oAuth2Response.getProfileImage());
            userEntity.setRole("ROLE_USER");

            userDao.save(userEntity);

            UserDto userDTO = new UserDto();
            userDTO.setUserName(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setProfileImage(oAuth2Response.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            existData.setProfileImage(oAuth2Response.getProfileImage());

            userDao.save(existData);

            UserDto userDTO = new UserDto();
            userDTO.setUserName(username);
            userDTO.setName(existData.getName());
            userDTO.setProfileImage(existData.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
    }

}