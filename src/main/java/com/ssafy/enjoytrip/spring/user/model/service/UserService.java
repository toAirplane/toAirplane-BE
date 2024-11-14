package com.ssafy.enjoytrip.spring.user.model.service;

import com.ssafy.enjoytrip.spring.user.model.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

public interface UserService {
    void joinUser(UserDto userDto);     //회원가입
    void updateUser(UserDto userDto);   //회원정보수정
    String findId(String name, String email);   //아이디 찾기
    void deleteUser(UserDto userDto);   //회원탈퇴
    UserDto loginUser(UserDto userDto) throws Exception;//로그인
    //JWT
    void saveRefreshToken(String userId, String refreshToken) throws Exception;
    Object getRefreshToken(String userId) throws Exception;
    void deleteRefreshToken(String userId) throws Exception;
    //admin
    UserDto getUser(String userId) throws Exception;

}
