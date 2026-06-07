package com.example.individualna.data.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Единствен Retrofit клиент кон нашиот PHP REST API.
 *
 * 10.0.2.2 е специјалната адреса преку која Android емулаторот пристапува до
 * localhost на хост-машината (каде што работи Apache/MySQL од XAMPP). Бекендот се
 * стартува локално преку XAMPP — детали во README.md. За вистински оддалечен сервер
 * заменете ја BASE_URL со адресата на хостот (заврши со "/").
 */
public final class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2/business-directory/";

    private static CompanyApi api;

    private ApiClient() {
    }

    public static CompanyApi getApi() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();

            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CompanyApi.class);
        }
        return api;
    }
}
