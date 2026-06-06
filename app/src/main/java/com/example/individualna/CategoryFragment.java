package com.example.individualna;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.individualna.data.CompanyRepository;
import com.example.individualna.data.api.CompanyDto;
import com.example.individualna.databinding.FragmentCategoryBinding;
import com.example.individualna.model.Category;
import com.example.individualna.model.Company;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Прикажува листа (ListView) на компании за една категорија, прочитани од
 * оддалечениот сервер. На дното има поле за пребарување по назив во рамките на
 * тековната категорија (филтрирањето го прави серверот).
 */
public class CategoryFragment extends Fragment {

    private static final String ARG_POSITION = "arg_position";

    private FragmentCategoryBinding binding;
    private Category category;
    private CompanyAdapter adapter;

    public static CategoryFragment newInstance(int position) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        category = Category.fromPosition(requireArguments().getInt(ARG_POSITION));

        adapter = new CompanyAdapter(requireContext(), new ArrayList<>());
        binding.companyListView.setAdapter(adapter);
        binding.companyListView.setEmptyView(binding.emptyView);

        // Демонстрација на намера (Intent): клик на компанија ја отвора нејзината web страна.
        binding.companyListView.setOnItemClickListener((parent, v, pos, id) ->
                openWebsite(adapter.getItem(pos).getWebsite()));

        // Пребарување по назив во рамките на тековната категорија.
        binding.searchField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { reload(); }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Освежи по враќање од екранот за внес (нова компанија може да е додадена).
        reload();
    }

    /** Чита од оддалечениот сервер според категоријата + поимот за пребарување. */
    private void reload() {
        String query = binding.searchField.getText() != null
                ? binding.searchField.getText().toString() : "";

        CompanyRepository.getByCategory(category, query).enqueue(new Callback<List<CompanyDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompanyDto>> call,
                                   @NonNull Response<List<CompanyDto>> response) {
                if (binding == null) return;
                List<Company> result = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    for (CompanyDto dto : response.body()) result.add(dto.toCompany());
                }
                adapter.updateData(result);
            }

            @Override
            public void onFailure(@NonNull Call<List<CompanyDto>> call, @NonNull Throwable t) {
                if (binding == null) return;
                adapter.updateData(new ArrayList<>());
                Toast.makeText(requireContext(), "Грешка при поврзување со серверот",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openWebsite(String website) {
        if (website == null || website.trim().isEmpty()) return;
        String url = website.startsWith("http") ? website : "https://" + website;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), website, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
