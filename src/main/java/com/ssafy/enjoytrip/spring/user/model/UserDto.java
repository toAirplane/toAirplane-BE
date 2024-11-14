package com.ssafy.enjoytrip.spring.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;

// @Getter
// @Setter
@Data
public class UserDto {
    private String id;          //아이디
    private String name;        //이름
    private String password;    //비밀번호
    private String email;       //이메일
    private Timestamp joinDate; //가입일 (DB 자동 설정)
    private String roll;        //역할 : 'admin' vs 'user'
    private int exp;            //경험치 : 레벨
    private byte[] imageDate;   //이미지 : 개인 프로필 사진
    private String phoneNumber;
    private String refreshToken;
}
