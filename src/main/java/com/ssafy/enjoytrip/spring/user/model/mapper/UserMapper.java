package com.ssafy.enjoytrip.spring.user.model.mapper;

import com.ssafy.enjoytrip.spring.user.model.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.Map;

@Mapper
public interface UserMapper {
    void joinUser(UserDto userDto);     //회원가입
    void updateUser(UserDto userDto);   //회원정보수정
    int countById(String id);           //회원가입 : 아이디 중복 확인
    String findId(String name, String email);   //아이디 찾기
    void deleteUser(UserDto userDto);   //회원탈퇴
    UserDto loginUser(UserDto userDto) throws SQLException; //로그인
    //JWT
    void saveRefreshToken(Map<String, String> map) throws SQLException;
    Object getRefreshToken(String userid) throws SQLException;
    void deleteRefreshToken(Map<String, String> map) throws SQLException;
    //admin
    UserDto getUser(String userId) throws SQLException;

}
