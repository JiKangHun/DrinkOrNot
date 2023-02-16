package com.ssafy.cadang.service;


import com.ssafy.cadang.domain.User;
import com.ssafy.cadang.dto.user.UserGoalDto;
import com.ssafy.cadang.dto.user.UserModifyDto;
import com.ssafy.cadang.dto.user.UserPassChangeDto;
import com.ssafy.cadang.error.CustomException;
import com.ssafy.cadang.error.ExceptionEnum;
import com.ssafy.cadang.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = false)
@Slf4j
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${DEFAULT_PROFILE_FILE}")
    private String DefaultProfileFile;

    @Value("${EC2_PROFILE_PATH}")
    private String UserProfileImgPath;


    private String getFullPath(String imgPath, String filename) {
        return imgPath + filename;
    }


    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 초기 목표량 세팅하기
    @Transactional
    public void setGoal(UserGoalDto userGoalDto, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        user.setCaffeGoal(userGoalDto.getCaffeGoal());
        user.setSugarGoal(userGoalDto.getSugarGoal());

    }


    @Transactional
    public void modifyUserInfo(UserModifyDto userModifyDto, Long id) throws IOException {

        MultipartFile multipartFile;

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        if (userModifyDto.getIsModified() == 1) {
            // 수정
            if (userModifyDto.getImg() != null) {
                multipartFile = userModifyDto.getImg();
                String originalFilename = multipartFile.getOriginalFilename();
                String storeFilename = createStoreFileName(originalFilename);
                String storedPath = getFullPath(UserProfileImgPath, storeFilename);
                multipartFile.transferTo(new File(storedPath));
                user.setImgUrl(storeFilename);

            }
        } else if (userModifyDto.getIsModified() == 2) {
            // 기본 이미지로
            user.setImgUrl(DefaultProfileFile);
        }

        user.setNickname(userModifyDto.getNickname());
        user.setCaffeGoal(userModifyDto.getCaffeGoal());
        user.setSugarGoal(userModifyDto.getSugarGoal());


    }


    @Transactional
    public void newpass(UserPassChangeDto userPassChangeDto, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));
        if (!passwordEncoder.matches(userPassChangeDto.getCurpass(), user.getPassword())) {
            throw new CustomException(ExceptionEnum.PASSWORD_INCORRECT);
        }

        if (!userPassChangeDto.getNewPass().equals(userPassChangeDto.getNewPassRe())) {
            throw new CustomException(ExceptionEnum.NEWPASSWORD_DIFFERENT);
        }

        user.setPassword(userPassChangeDto.getNewPass());


    }

    @Transactional
    public void deleteUser(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionEnum.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password,user.getPassword())) {
            throw new CustomException(ExceptionEnum.PASSWORD_INCORRECT);
        }

        userRepository.deleteById(id);
    }

    public Optional<User> getMyInfo(Long id){
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new CustomException(ExceptionEnum.USER_NOT_FOUND);
        }
        return user;

    }


    // 서버에 저장할 파일명을 만든다.
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 원본 파일의 확장자를 반환한다.

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
