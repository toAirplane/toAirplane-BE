package com.ssafy.enjoytrip.spring.map.controller;

import com.ssafy.enjoytrip.spring.config.TourApiCrawling;
import com.ssafy.enjoytrip.spring.map.model.PlaceInfoDto;
import com.ssafy.enjoytrip.spring.map.model.service.PlaceInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/map")
public class PlaceInfoController {

    private final TourApiCrawling tourApiCrawling;
    private final PlaceInfoService placeInfoService;

    public PlaceInfoController(TourApiCrawling tourApiCrawling, PlaceInfoService placeInfoService) {
        this.tourApiCrawling = tourApiCrawling;
        this.placeInfoService = placeInfoService;
    }

    //지역코드 조회
    @GetMapping("/areacode")
    public ResponseEntity<?> findAreaCode(){
        List<Map<String, Object>> areaCodes = placeInfoService.findAreaCode();
        return ResponseEntity.ok(areaCodes);

    }

    //시군구코드 조회
    @GetMapping("/sigungu/{areacode}")
    public ResponseEntity<?> findSigunguCode(@PathVariable("areacode") int areaCode){
        List<Map<String, Object>> sigunguCodes = placeInfoService.findSigunguCodebyAreaCode(areaCode);
        return ResponseEntity.ok(sigunguCodes);
    }

    //지역기반 전체 조회
    @GetMapping("/area/{areaCode}")
    public ResponseEntity<?> placeInfoByAreaCode(@PathVariable("areaCode") int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.fetch(areaCode);
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //지역, 관광 타입별 조회
    @GetMapping("/area/{areaCode}/{contentTypeId}")
    public ResponseEntity<?> placeInfoByAreaCodeAndContentTypeId(@PathVariable("areaCode") int areaCode, @PathVariable("contentTypeId") int contentTypeId) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.fetch(areaCode, contentTypeId);
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //지역기반 카페 정보 조회
    @GetMapping("/area/coffee/{areaCode}")
    public ResponseEntity<?> findCofficePlace(@PathVariable("areaCode") int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.fetch(areaCode, 39, "A05", "A0502", "A05020900");
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //지역기반 관광지 정보 조회
    @GetMapping("/area/attraction/{areaCode}")
    public ResponseEntity<?> findAttractionPlace(@PathVariable("areaCode") int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.fetch(areaCode, 12);
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //지역기반 숙박 정보 조회
    @GetMapping("/area/accommodation/{areaCode}")
    public ResponseEntity<?> findAccommodationPlace(@PathVariable("areaCode") int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.fetch(areaCode, 32);
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //지역기반 음식점 조회
    @GetMapping("/area/food/{areaCode}")
    public ResponseEntity<?> findFoodPlace(@PathVariable("areaCode") int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        List<PlaceInfoDto> placeInfoDtoList = tourApiCrawling.findFoodShoferch(areaCode, 39);
        return ResponseEntity.ok(placeInfoDtoList);
    }

    //DB에 장소 추가
    @PostMapping("/insert")
    public ResponseEntity<String> insertPlace(@RequestBody PlaceInfoDto placeInfo) {
        try {
            placeInfoService.insertPlace(placeInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장에 실패했습니다.");
        }
    }


}

