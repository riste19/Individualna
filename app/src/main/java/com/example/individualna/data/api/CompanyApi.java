package com.example.individualna.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/** Веб сервисите (REST endpoints) изложени од нашиот PHP сервер. */
public interface CompanyApi {

    @GET("companies.php")
    Call<List<CompanyDto>> getCompanies(@Query("category") String category, @Query("q") String query);

    @POST("companies.php")
    Call<CompanyDto> addCompany(@Body NewCompanyDto company);
}
