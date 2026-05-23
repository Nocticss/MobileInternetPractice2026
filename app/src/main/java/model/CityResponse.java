package com.example.mobileinternetpractice.model;

import java.util.List;

// 高德API返回数据模型
public class CityResponse {
    private String status;
    private List<District> districts;

    // Getter & Setter
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}