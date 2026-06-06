package com.example.individualna.data.api;

import com.example.individualna.model.Category;
import com.example.individualna.model.Company;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Како што компанијата изгледа во JSON одговорот од серверот. */
public class CompanyDto {

    public long id;
    public String name;
    public String address;
    public Double latitude;
    public Double longitude;
    public String email;
    public String phone;
    public String website;
    public List<String> categories;

    public Company toCompany() {
        Set<Category> cats = new LinkedHashSet<>();
        if (categories != null) {
            for (String s : categories) {
                Category c = Category.fromNameOrNull(s);
                if (c != null) cats.add(c);
            }
        }
        return new Company(
                id,
                name != null ? name : "",
                address != null ? address : "",
                latitude,
                longitude,
                email != null ? email : "",
                phone != null ? phone : "",
                website != null ? website : "",
                cats,
                Category.defaultLogoFor(cats)
        );
    }
}
