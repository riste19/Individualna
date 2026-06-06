package com.example.individualna.model;

import androidx.annotation.DrawableRes;

import java.util.Set;

/**
 * Една компанија во бизнис директориумот.
 * Една компанија може да припаѓа во повеќе категории истовремено.
 */
public class Company {

    private final long id;
    private final String name;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String email;
    private final String phone;
    private final String website;
    private final Set<Category> categories;
    @DrawableRes
    private final int logoResId;

    public Company(long id, String name, String address, Double latitude, Double longitude,
                   String email, String phone, String website,
                   Set<Category> categories, @DrawableRes int logoResId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.categories = categories;
        this.logoResId = logoResId;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getWebsite() { return website; }
    public Set<Category> getCategories() { return categories; }
    @DrawableRes
    public int getLogoResId() { return logoResId; }
}
