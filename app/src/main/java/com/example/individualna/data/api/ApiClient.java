package com.example.individualna.data.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Единствен Retrofit клиент кон нашиот PHP REST API.
 *
 * ПРОДУКЦИЈА (InfinityFree): постави го BASE_URL на јавната HTTPS адреса каде што
 * се качени PHP датотеките, на пр. "https://<твој-поддомен>.infinityfreeapp.com/".
 * Датотеките одат во htdocs/ (коренот на сајтот), па патеката нема
 * "business-directory/" освен ако не ги качиш во таква потпапка. Заврши со "/".
 *
 * ЛОКАЛЕН РАЗВОЈ (XAMPP емулатор): "http://10.0.2.2/business-directory/" — 10.0.2.2 е
 * адресата преку која Android емулаторот пристапува до localhost на хост-машината.
 */
public final class ApiClient {

    // <-- замени со твојата InfinityFree адреса (заврши со "/")
    private static final String BASE_URL = "https://your-subdomain.infinityfreeapp.com/";

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
