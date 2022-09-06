package com.payhere.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opusm_assignment.dto.userDto.LoginResponseDto;
import com.opusm_assignment.model.User;
import com.opusm_assignment.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        response.addHeader("username", userDetails.getUser().getUsername());
        response.addHeader("nickname", userDetails.getUser().getNickname());  //헤더에는 유저의 정보를 담기에는 좀 그렇다.

        response.setContentType("application/json; charset=utf-8");
        User user = userDetails.getUser();
        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getUsername(), user.getNickname(), user.getId());
        String result = mapper.writeValueAsString(loginResponseDto);
        response.getWriter().write(result);

        System.out.println("로그인 내려주는 값" + result);

    }
}
