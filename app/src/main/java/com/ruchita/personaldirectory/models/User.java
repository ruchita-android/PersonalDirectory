package com.ruchita.personaldirectory.models;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by ruchita on 2/3/18.
 */

public class User extends SugarRecord implements Serializable{

    private String name;
    private String phone;
    private String address;
    private double lat;
    private double lang;
    private LatLng latlng;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
