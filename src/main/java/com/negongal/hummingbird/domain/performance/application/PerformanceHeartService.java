package com.negongal.hummingbird.domain.performance.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.performance.dao.PerformanceHeartRepository;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.PerformancePageDto;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.AlreadyExistException;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PerformanceHeartService {
    private final PerformanceHeartRepository performanceHeartRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(Long performanceId) {
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

        Optional<PerformanceHeart> findHeart = performanceHeartRepository
                .findByUserAndPerformance(user, performance);

        

        if(findHeart.isPresent()) { // 이미 하트 누른 경우
            throw new AlreadyExistException(PERFORMANCE_HEART_ALREADY_EXIST);
        }
        PerformanceHeart performanceHeart = PerformanceHeart.builder()
                .performance(performance)
                .user(user)
                .build();

        performanceHeartRepository.save(performanceHeart);
    }

    @Transactional
    public void delete(Long performanceId) {
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

        PerformanceHeart performanceHeart = performanceHeartRepository
                .findByUserAndPerformance(user, performance)
                .orElseThrow(() -> new NotExistException(PERFORMANCE_HEART_NOT_EXIST));

        performanceHeartRepository.delete(performanceHeart);
    }

    public PerformancePageDto findByUserHeart(Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId()
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        Page<PerformanceDto> dtoPage = performanceHeartRepository.findAllByUserHeart(pageable, userId);
        return PerformancePageDto.builder()
                .performanceDto(dtoPage.getContent())
                .totalPages(dtoPage.getTotalPages())
                .totalElements(dtoPage.getTotalElements())
                .isLast(dtoPage.isLast())
                .currPage(dtoPage.getPageable().getPageNumber())
                .build();
    }
}
