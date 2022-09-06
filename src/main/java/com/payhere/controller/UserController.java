package com.payhere.controller;

import com.payhere.dto.userDto.DubCheckRequestDto;
import com.payhere.dto.userDto.SignUpRequestDto;
import com.payhere.service.KakaoUserService;
import com.payhere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    // 회원가입
    @PostMapping("/user/signup")
    public void signup(@Valid @RequestBody SignUpRequestDto requestDto){
        userService.signup(requestDto);
    }

    // 아이디 중복체크
    @PostMapping("/user/dubcheck")  // get 으로 요청이 옴 띠용
    public boolean dubCheck(@RequestBody DubCheckRequestDto requestDto){
        return userService.dubCheck(requestDto);
    }

    // 카카오 아이디
    @GetMapping("/auth/kakao/callback") // username, nickname 같이 보내기
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        kakaoUserService.kakaoLogin(code, response);
    }
}
