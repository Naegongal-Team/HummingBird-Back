package com.negongal.hummingbird.domain.user.application;

import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.domain.user.exception.UserNotFoundException;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader uploader;

    public void addUserNicknameAndImage(String oauthId, UserDto saveParam, String profileImage) {
        User findUser = userRepository.findByOauth2Id(oauthId).orElseThrow(UserNotFoundException::new);
        findUser.updateNicknameAndProfileImage(saveParam.getNickname(), profileImage);
    }

    public void modifyUserNicknameAndImage(String oauthId, UserDto updateParam, String profileImage) throws IOException {
        User findUser = userRepository.findByOauth2Id(oauthId).orElseThrow(UserNotFoundException::new);
        String file = findUser.getProfileImage();
        if(!file.isEmpty()){
            uploader.deleteFile(file);
        }
        findUser.updateNicknameAndProfileImage(updateParam.getNickname(), profileImage);
    }

    public UserDetailDto findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserDetailDto.of(user);
    }

    public UserDetailDto findByOauthId(String oauthId) {
        User user = userRepository.findByOauth2Id(oauthId).orElseThrow(UserNotFoundException::new);
        return UserDetailDto.of(user);
    }

    public int findByNickname(String nickname) {
        Optional<User> byNickname = userRepository.findByNickname(nickname);
        if(byNickname.isPresent()) { //중복
            return 0;
        }
        else { //사용 가능
            return 1;
        }
    }

}
