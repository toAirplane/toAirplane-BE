package com.ssafy.enjoytrip.spring.map.model.service;

import com.ssafy.enjoytrip.spring.map.model.PlaceInfoDto;

import java.util.List;
import java.util.Map;

public interface PlaceInfoService {
    public void insertPlace(PlaceInfoDto placeInfoDto);  //DB 장소 추가
    public List<Map<String, Object>> findAreaCode();  //지역코드 찾기
    public List<Map<String, Object>> findSigunguCodebyAreaCode(int areaCode);  //시군구 코드 찾기
}
