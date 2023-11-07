package com.negongal.hummingbird.domain.user.application;

import com.negongal.hummingbird.domain.user.dto.UserDto;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.negongal.hummingbird.global.error.ErrorCode.USER_NOT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader uploader;

    @Transactional
    public void addUserNicknameAndImage(Long userId, UserDto saveParam, String profileImage) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(()->new NotExistException(USER_NOT_EXIST));
        findUser.updateNicknameAndProfileImage(saveParam.getNickname(), profileImage);
    }

    @Transactional
    public void modifyUserNicknameAndImage(Long userId, UserDto updateParam, String profileImage) throws IOException {
        User findUser = userRepository.findById(userId)
                .orElseThrow(()->new NotExistException(USER_NOT_EXIST));
        String file = findUser.getProfileImage();
        if(file != null){
            uploader.deleteFile(file);
        }
        findUser.updateNicknameAndProfileImage(updateParam.getNickname(), profileImage);
    }

    public UserDetailDto findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new NotExistException(USER_NOT_EXIST));
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
