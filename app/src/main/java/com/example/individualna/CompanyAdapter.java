package com.example.individualna;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.individualna.model.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * Прилагоден (custom) адаптер за ListView. Секој ред прикажува лого + назив,
 * адреса, телефон и web страна на една компанија. Користи ViewHolder за
 * ефикасно рециклирање на редовите.
 */
public class CompanyAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<Company> companies = new ArrayList<>();

    public CompanyAdapter(Context context, List<Company> initial) {
        this.inflater = LayoutInflater.from(context);
        if (initial != null) companies.addAll(initial);
    }

    /** Ја заменува содржината (на пр. по пребарување или промена на серверот). */
    public void updateData(List<Company> newCompanies) {
        companies.clear();
        if (newCompanies != null) companies.addAll(newCompanies);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Company getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_company, parent, false);
            holder = new ViewHolder();
            holder.logo = view.findViewById(R.id.companyLogo);
            holder.name = view.findViewById(R.id.companyName);
            holder.address = view.findViewById(R.id.companyAddress);
            holder.phone = view.findViewById(R.id.companyPhone);
            holder.web = view.findViewById(R.id.companyWeb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Company company = getItem(position);
        holder.logo.setImageResource(company.getLogoResId());
        holder.name.setText(company.getName());
        holder.address.setText(company.getAddress());
        holder.phone.setText(company.getPhone());
        holder.web.setText(company.getWebsite());
        return view;
    }

    private static class ViewHolder {
        ImageView logo;
        TextView name;
        TextView address;
        TextView phone;
        TextView web;
    }
}
