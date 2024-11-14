package com.ssafy.enjoytrip.spring.map.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceInfoDto implements Comparable<PlaceInfoDto>{

    private int id;
    @JsonProperty("contenttypeid")
    private int content_type_id;
    @JsonProperty("title")
    private String name;
    @JsonProperty("addr1")
    private String addr1;
    @JsonProperty("addr2")
    private String addr2;
    @JsonProperty("mapy")
    private double latitude;
    @JsonProperty("mapx")
    private double longitude;
    @JsonProperty("areacode")
    private int sidoCode;
    @JsonProperty("sigungucode")
    private int gugunCode;
    @JsonProperty("cat1")
    private String cat1;
    @JsonProperty("cat2")
    private String cat2;
    @JsonProperty("cat3")
    private String cat3;
    @JsonProperty("readcount")
    private int readCount;

    @Override
    public int compareTo(PlaceInfoDto o) {
        return o.readCount - this.readCount;
    }
}
