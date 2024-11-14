package com.ssafy.enjoytrip.spring.map.model.service;


import com.ssafy.enjoytrip.spring.map.model.PlaceInfoDto;
import com.ssafy.enjoytrip.spring.map.model.mapper.PlaceInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlaceInfoServiceImpl implements PlaceInfoService{

    private final PlaceInfoMapper placeInfoMapper;

    public PlaceInfoServiceImpl(PlaceInfoMapper placeInfoMapper) {
        this.placeInfoMapper = placeInfoMapper;
    }

    @Override
    public void insertPlace(PlaceInfoDto placeInfoDto) {
        placeInfoMapper.insertPlace(placeInfoDto);
    }

    @Override
    public List<Map<String, Object>> findAreaCode() {
        return placeInfoMapper.findAreaCode();
    }

    @Override
    public List<Map<String, Object>> findSigunguCodebyAreaCode(int areaCode) {
        return placeInfoMapper.findSigunguCodebyAreaCode(areaCode);
    }


}
