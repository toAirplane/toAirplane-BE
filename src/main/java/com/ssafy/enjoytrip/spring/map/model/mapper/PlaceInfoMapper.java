package com.ssafy.enjoytrip.spring.map.model.mapper;

import com.ssafy.enjoytrip.spring.map.model.PlaceInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlaceInfoMapper {
    public void insertPlace(PlaceInfoDto placeInfoDto);  //DB 장소 추가
    public List<Map<String, Object>> findAreaCode();  //지역코드 찾기
    public List<Map<String, Object>> findSigunguCodebyAreaCode(int areaCode);  //시군구 코드 찾기
}
