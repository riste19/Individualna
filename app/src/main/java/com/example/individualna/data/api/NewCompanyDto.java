package com.example.individualna.data.api;

import java.util.List;

/** Тело за POST (без id — серверот го доделува). */
public class NewCompanyDto {
    public String name;
    public String address;
    public Double latitude;
    public Double longitude;
    public String email;
    public String phone;
    public String website;
    public List<String> categories;
}
