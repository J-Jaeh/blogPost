package com.sparta.bolgpost.service;


import com.sparta.bolgpost.dto.LoginRequestDto;
import com.sparta.bolgpost.dto.MessageResponseDto;
import com.sparta.bolgpost.dto.SignupRequestDto;
import com.sparta.bolgpost.entity.User;
import com.sparta.bolgpost.enums.UserRoleEnum;
import com.sparta.bolgpost.jwt.JwtUtil;
import com.sparta.bolgpost.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "DeXi341@dNDI";

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
//        String email = signupRequestDto.getEmail();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }        // 회원 중복 확인
//        Optional<User> emailFound = userRepository.findByEmail(email);
//        if (emailFound .isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
//        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public MessageResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        // 사용자 확인
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            return new MessageResponseDto("사용자가 존재하지 않습니다.", 400);
        }
        // 비밀번호 확인
        if(!user.get().getPassword().equals(password)){
            return new MessageResponseDto("비밀번호가 틀렸습니다.", 400);
        }
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
        return new MessageResponseDto(jwtUtil.createToken(user.get().getUsername(), user.get().getRole()));
    }
}