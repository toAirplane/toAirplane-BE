package com.ssafy.enjoytrip.spring.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.spring.map.model.PlaceInfoDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TourApiCrawling {
    /**
     * 크롤링 작업
     */

    private final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";
    private final String apiUri = "/areaBasedList";
    private final String serviceKey = "?ServiceKey=QMf%2B%2Bv2SWgn80hSqTpS%2BpCdnBaSwhHqmYQoVr4sH%2BTrdpACJsX4fHdyHgThmMT%2Flm%2FcKUnfnjsj9lcm%2FpRV1yQ%3D%3D";
    private final String defaultQueryParam = "&MobileOS=ETC&MobileApp=AppTest&_type=json";
    private final String numOfRows = "&numOfRows=100";
    private final String areaCode = "&areaCode=";
    private final String contentTypeId = "&contentTypeId=";
    private final String cat1="&cat1=";
    private final String cat2="&cat2=";
    private final String cat3="&cat3=";

    private String makeUrl(int areaCode, int contentTypeId) throws UnsupportedEncodingException {
        return BASE_URL +
                apiUri +
                serviceKey +
                defaultQueryParam +
                numOfRows +
                this.areaCode + areaCode +
                this.contentTypeId + contentTypeId;
    }
    private String makeUrl(int areaCode) throws UnsupportedEncodingException {
        return BASE_URL +
                apiUri +
                serviceKey +
                defaultQueryParam +
                numOfRows +
                this.areaCode + areaCode;
    }

    private String makeUrl(int areaCode, int contentTypeId, String cat1, String cat2, String cat3) throws UnsupportedEncodingException {
        return BASE_URL +
                apiUri +
                serviceKey +
                defaultQueryParam +
                numOfRows +
                this.areaCode + areaCode +
                this.contentTypeId + contentTypeId +
                this.cat1 + cat1 +
                this.cat2 + cat2 +
                this.cat3 + cat3;
    }

    public List<PlaceInfoDto> fetch(int areaCode, int contentTypeId) throws UnsupportedEncodingException, URISyntaxException {
        System.out.println(makeUrl(areaCode, contentTypeId));
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> resultMap = restTemplate.exchange(new URI(makeUrl(areaCode, contentTypeId)), HttpMethod.GET, entity, Map.class);
        List<PlaceInfoDto> placeInfoDtoList = mapToPlaceInfo(resultMap);
        Collections.sort(placeInfoDtoList);
        return placeInfoDtoList;
    }

    public List<PlaceInfoDto> fetch(int areaCode) throws UnsupportedEncodingException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> resultMap = restTemplate.exchange(new URI(makeUrl(areaCode)), HttpMethod.GET, entity, Map.class);
        List<PlaceInfoDto> placeInfoDtoList = mapToPlaceInfo(resultMap);
        Collections.sort(placeInfoDtoList);
        return placeInfoDtoList;
    }

    public List<PlaceInfoDto> fetch(int areaCode, int contentTypeId, String cat1, String cat2, String cat3) throws UnsupportedEncodingException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> resultMap = restTemplate.exchange(new URI(makeUrl(areaCode, contentTypeId, cat1, cat2, cat3)), HttpMethod.GET, entity, Map.class);
        List<PlaceInfoDto> placeInfoDtoList = mapToPlaceInfo(resultMap);
        Collections.sort(placeInfoDtoList);
        return placeInfoDtoList;
    }

    public List<PlaceInfoDto> findFoodShoferch(int areaCode, int contentTypeId) throws UnsupportedEncodingException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Map> resultMap = restTemplate.exchange(new URI(makeUrl(areaCode, contentTypeId)), HttpMethod.GET, entity, Map.class);
        List<PlaceInfoDto> placeInfoDtoList = mapToPlaceInfo(resultMap);
        Collections.sort(placeInfoDtoList);

        //커피숍, 클럽 제외

        return placeInfoDtoList.stream()
                .filter(place -> !isExcludedCategory(place.getCat3())) // category 필드를 기반으로 필터링
                .collect(Collectors.toList());
    }

    public List<PlaceInfoDto> mapToPlaceInfo(ResponseEntity<Map> resultMap){
        // ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 구조에 맞게 필요한 데이터 추출
        Map responseBody = (Map) resultMap.getBody().get("response");
        Map body = (Map) responseBody.get("body");
        Map items = (Map) body.get("items");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");

        // itemList를 PlaceInfoDto 리스트로 변환
        List<PlaceInfoDto> placeInfoDtoList = objectMapper.convertValue(itemList, new TypeReference<List<PlaceInfoDto>>() {});

        // 각 PlaceInfoDto 객체의 id를 결과 인덱스 값으로 설정
        for (int i = 0; i < placeInfoDtoList.size(); i++) {
            placeInfoDtoList.get(i).setId(i+1);
        }

        return placeInfoDtoList;
    }

    // 카페, 클럽 확인하는 메서드
    private boolean isExcludedCategory(String cat3) {
        return "A05020900".equals(cat3) || "A05021000".equals(cat3);
    }
}
