package com.example.individualna.data;

import com.example.individualna.data.api.ApiClient;
import com.example.individualna.data.api.CompanyDto;
import com.example.individualna.data.api.NewCompanyDto;
import com.example.individualna.model.Category;

import java.util.List;

import retrofit2.Call;

/**
 * Единствена точка за пристап до податоците — преку оддалечениот REST API.
 * Методите враќаат Retrofit Call; повикувачот користи enqueue(...) за асинхрон повик.
 */
public final class CompanyRepository {

    private CompanyRepository() {
    }

    /** Компании од дадена категорија, опционално филтрирани по назив (на серверот). */
    public static Call<List<CompanyDto>> getByCategory(Category category, String query) {
        String q = (query == null || query.trim().isEmpty()) ? null : query.trim();
        return ApiClient.getApi().getCompanies(category.name(), q);
    }

    /** Сите компании од серверот. */
    public static Call<List<CompanyDto>> getAll() {
        return ApiClient.getApi().getCompanies(null, null);
    }

    /** Внес на нова компанија преку POST. */
    public static Call<CompanyDto> insert(NewCompanyDto company) {
        return ApiClient.getApi().addCompany(company);
    }
}
