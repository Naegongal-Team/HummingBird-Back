package com.negongal.hummingbird.domain.user.application;

import com.negongal.hummingbird.domain.user.exception.AccessDeniedException;
import com.negongal.hummingbird.domain.user.exception.UserNotFoundException;
import com.negongal.hummingbird.domain.user.domain.Role;
import com.negongal.hummingbird.domain.user.dto.UserDetailDto;
import com.negongal.hummingbird.domain.user.dto.UserUpdateDto;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void modifyUserNickname(String oauthId, UserUpdateDto updateParam) {
        User findUser = userRepository.findByOauth2Id(oauthId).orElseThrow(UserNotFoundException::new);
        findUser.updateNickname(updateParam.getNickname(), updateParam.getProfileImage());
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

    public Authentication modifyAuthority(String oauthId) {
        User user = userRepository.findByOauth2Id(oauthId).orElseThrow(UserNotFoundException::new);

        user.updateAuthority(Role.ADMIN); //나중에 삭제 예정
        Authentication newAuth = null;
        if(user.getRole()== Role.ADMIN) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
            updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            newAuth = new UsernamePasswordAuthenticationToken(
                    auth.getPrincipal(),
                    auth.getCredentials(),
                    updatedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        else {
            throw new AccessDeniedException();
        }
        return newAuth;
    }

}
