package com.example.individualna;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.individualna.data.CompanyRepository;
import com.example.individualna.data.api.CompanyDto;
import com.example.individualna.data.api.NewCompanyDto;
import com.example.individualna.databinding.ActivityAddCompanyBinding;
import com.example.individualna.model.Category;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Екран за внес на нова компанија. По кликнување на ЗАЧУВАЈ податоците се
 * валидираат и се испраќаат до серверот (POST), по што екранот се затвора.
 */
public class AddCompanyActivity extends AppCompatActivity {

    private ActivityAddCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.btnSave.setOnClickListener(v -> saveCompany());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveCompany() {
        String name = textOf(binding.etName);
        if (name.isEmpty()) {
            binding.etName.setError(getString(R.string.error_name_required));
            binding.etName.requestFocus();
            return;
        }

        List<String> categories = collectCategories();
        if (categories.isEmpty()) {
            Toast.makeText(this, R.string.error_category_required, Toast.LENGTH_SHORT).show();
            return;
        }

        NewCompanyDto dto = new NewCompanyDto();
        dto.name = name;
        dto.address = textOf(binding.etAddress);
        dto.latitude = parseDoubleOrNull(textOf(binding.etLatitude));
        dto.longitude = parseDoubleOrNull(textOf(binding.etLongitude));
        dto.email = textOf(binding.etEmail);
        dto.phone = textOf(binding.etPhone);
        dto.website = textOf(binding.etWebsite);
        dto.categories = categories;

        // Снимање на серверот преку POST (асинхрон мрежен повик).
        binding.btnSave.setEnabled(false);
        CompanyRepository.insert(dto).enqueue(new Callback<CompanyDto>() {
            @Override
            public void onResponse(@NonNull Call<CompanyDto> call, @NonNull Response<CompanyDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCompanyActivity.this, R.string.company_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(AddCompanyActivity.this,
                            "Серверот одби: проверете ги податоците", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompanyDto> call, @NonNull Throwable t) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(AddCompanyActivity.this,
                        "Грешка: компанијата не е зачувана (проверете го серверот)", Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> collectCategories() {
        List<String> list = new ArrayList<>();
        if (binding.cbIndustry.isChecked()) list.add(Category.INDUSTRY.name());
        if (binding.cbFun.isChecked()) list.add(Category.ENTERTAINMENT.name());
        if (binding.cbEducation.isChecked()) list.add(Category.EDUCATION.name());
        if (binding.cbServices.isChecked()) list.add(Category.SERVICES.name());
        return list;
    }

    private String textOf(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
