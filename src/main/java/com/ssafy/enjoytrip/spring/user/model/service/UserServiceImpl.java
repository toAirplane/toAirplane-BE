package com.ssafy.enjoytrip.spring.user.model.service;

import com.ssafy.enjoytrip.spring.user.model.UserDto;
import com.ssafy.enjoytrip.spring.user.model.mapper.UserMapper;
import com.ssafy.enjoytrip.spring.util.JWTUtil;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper, JWTUtil jwtUtil) {
        super();
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    //회원가입
    @Override
    public void joinUser(UserDto userDto) {
        //아이디 유효성 검사
        if(!isValidId(userDto.getId())) {
            throw new IllegalArgumentException("아이디 실패");
        }

        //중복 아이디 확인
        if(isIdDuplicate(userDto.getId())) {
            throw new IllegalArgumentException("아이디 중복");
        }

        //비밀번호 유효성 검사
        if(!isValidPassword(userDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호 실패");
        }

        userMapper.joinUser(userDto);
    }

    //회원정보수정
    @Override
    public void updateUser(UserDto userDto) {
        userMapper.updateUser(userDto);
    }

    //아이디 찾기
    @Override
    public String findId(String name, String email) {
        String id = userMapper.findId(name, email);
        if (id != null) {
            return id;  // 아이디가 존재하는 경우
        } else {
            return "존재하지 않는 아이디입니다.";  // 아이디가 존재하지 않는 경우
        }
    }

    //////////유효성 검사//////////
    //아이디 : 영어+숫자
    private boolean isValidId(String id) {
        return id != null && id.matches("^[a-zA-Z0-9]+$");
    }

    //아이디 중복
    private boolean isIdDuplicate(String id) {
        return userMapper.countById(id) > 0;
        //0보다 크면 중복된 아이디
    }

    //비밀번호 : 영어+숫자 && 9~16자
    private boolean isValidPassword(String password) {
        return password != null && password.matches("^[a-zA-Z0-9]{9,16}$");
    }

    //회원탈퇴
    @Override
    public void deleteUser(UserDto userDto) {
        userMapper.deleteUser(userDto);
    }

    //로그인
    @Override
    public UserDto loginUser(UserDto userDto) throws SQLException {
        // 1. 로그인 시도
        UserDto loginUser = userMapper.loginUser(userDto);

        // 2. 로그인 실패 시 null 반환
        if (loginUser == null) {
            return null; // 로그인 실패 시 null 반환
        }

        // 3. 로그인 성공 시 refreshToken 생성
        String refreshToken = jwtUtil.createRefreshToken(userDto.getId());

        // 4. 생성된 refreshToken을 UserDto에 설정
        loginUser.setRefreshToken(refreshToken);

        // 5. Map을 생성하여 refreshToken을 DB에 저장
        Map<String, String> map = new HashMap<>();
        map.put("id", loginUser.getId());  // userId
        map.put("refreshToken", refreshToken);  // refreshToken

        // 6. DB에 refreshToken 저장
        userMapper.saveRefreshToken(map);

        // 7. 로그인된 UserDto 반환
        return loginUser;
    }




    @Override
    public void saveRefreshToken(String userId, String refreshToken) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userId);
        map.put("refreshToken", refreshToken);
        userMapper.saveRefreshToken(map);
    }

    @Override
    public Object getRefreshToken(String userId) throws SQLException {
        return userMapper.getRefreshToken(userId);
    }

    @Override
    public void deleteRefreshToken(String userId) throws SQLException {
        Map<String, String> map = new HashMap<String,String>();
        map.put("id", userId);
        map.put("refreshToken", null);
        userMapper.deleteRefreshToken(map);

    }

    @Override
    public UserDto getUser(String userId) throws Exception {
        return userMapper.getUser(userId);
    }


}
