package com.negongal.hummingbird.global.auth.jwt;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.error.exception.InvalidException;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.negongal.hummingbird.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void matches(String refreshToken, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        String savedRefreshToken = user.getRefreshToken();

        if(!jwtProvider.validateToken(savedRefreshToken)) {
            user.deleteRefreshToken();
            throw new InvalidException(TOKEN_INVALID);
        }

        validateSameToken(refreshToken, savedRefreshToken);
    }

    public void validateSameToken(String token, String savedRefreshToken) {
        if (!token.equals(savedRefreshToken)) {
            throw new InvalidException(TOKEN_INVALID);
        }
    }
}
