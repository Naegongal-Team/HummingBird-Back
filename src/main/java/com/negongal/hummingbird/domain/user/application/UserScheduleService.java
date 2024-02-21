package com.negongal.hummingbird.domain.user.application;

import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.UserStatus;
import com.negongal.hummingbird.domain.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserScheduleService {

    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 1000*60*60*24) // 24시간마다 실행
    public void userRemove() {
        List<User> inactiveUser = userRepository.findAllByStatusIs(UserStatus.INACTIVE);
        inactiveUser.stream()
            .filter(user -> Period.between(user.getInactiveDate(), LocalDate.now()).getMonths() >= 6)
            .forEach(userRepository::delete);
    }
}
