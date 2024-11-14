package com.ssafy.enjoytrip.spring.user.controller;

import com.ssafy.enjoytrip.spring.user.model.UserDto;
import com.ssafy.enjoytrip.spring.user.model.service.UserService;
import com.ssafy.enjoytrip.spring.util.JWTUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
@Tag(name = "회원 인증 컨트롤러", description = "로그인 로그아웃, 토큰처리등 회원의 인증관련 처리하는 클래스.")
public class UserController {

    private UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/joinUser")
    public ResponseEntity<String> register(@RequestBody UserDto user) throws IOException {
        try {
            userService.joinUser(user);
            return ResponseEntity.ok("등록 성공");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UserDto user) throws IOException {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/findId")
    public String findUserId(@RequestBody UserDto user) {
        return userService.findId(user.getName(), user.getEmail());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @RequestBody UserDto user) throws IOException {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @Parameter(description = "로그인시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDto userDto) {
        log.debug("login user : {}", userDto);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            UserDto loginUser = userService.loginUser(userDto);
            System.out.println(userDto);
            if (loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getId());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getId());
                log.debug("access token : {}", accessToken);
                log.debug("refresh token : {}", refreshToken);

//				발급받은 refresh token 을 DB에 저장.
                userService.saveRefreshToken(loginUser.getId(), refreshToken);

//				JSON 으로 token 전달.
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);

                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
                status = HttpStatus.UNAUTHORIZED;
            }

        } catch (Exception e) {
            log.debug("로그인 에러 발생 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }



    @Operation(summary = "로그아웃", description = "회원 정보를 담은 Token 을 제거한다.")
    @GetMapping("/logout/{id}")
    @Hidden
    public ResponseEntity<?> removeToken(
            @PathVariable("id") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            userService.deleteRefreshToken(userId);
            status = HttpStatus.OK;
            resultMap.put("message", "로그아웃 성공");
        } catch (Exception e) {
            log.error("로그아웃 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody UserDto userDto, @RequestHeader("refreshToken") String token)
            throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        log.debug("token : {}, UserDto : {}", token, userDto);
        if (jwtUtil.checkToken(token)) {
            if (token.equals(userService.getRefreshToken(userDto.getId()))) {
                String accessToken = jwtUtil.createAccessToken(userDto.getId());
                log.debug("token : {}", accessToken);
                log.debug("정상적으로 access token 재발급!!!");
                resultMap.put("access-token", accessToken);
                status = HttpStatus.CREATED;
            }
        } else {
            log.debug("refresh token 도 사용 불가!!!!!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }


}
