package com.negongal.hummingbird.domain.user.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.domain.user.dto.request.UpdateNicknameRequest;
import com.negongal.hummingbird.domain.user.dto.response.GetUserResponse;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserInfoService {

	@Value("${cloud.aws.s3.folder.folderName2}")
	private String userFolder;
	private final UserService userService;
	private final S3Uploader uploader;

	@Transactional
	public void updateUserNickname(Long userId, UpdateNicknameRequest request) {
		User user = userService.getById(userId);
		user.updateNickname(request.getNickname());
	}

	@Transactional
	public void updateUserPhoto(Long userId, MultipartFile file) {
		User user = userService.getById(userId);
		checkPhoto(user);
		String photo = (file == null) ? null : uploader.saveFileInFolder(file, userFolder);
		user.updatePhoto(photo);
	}

	public GetUserResponse getUser(Long id) {
		User user = userService.getById(id);
		return GetUserResponse.of(user);
	}

	@Transactional
	public void saveFCMToken(String fcmToken, Long id) {
		User findUser = userService.getById(id);
		findUser.updateFCMToken(fcmToken);
	}

	public boolean nicknameInUse(String nickname) {
		return userService.existByNickname(nickname);
	}

	private void checkPhoto(User user) {
		if (user.getProfileImage() != null) {
			uploader.deleteFile(user.getProfileImage());
		}
	}
}

