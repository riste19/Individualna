package com.example.individualna.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.example.individualna.R;

import java.util.Set;

/**
 * Категориите прикажани како табови. Редоследот тука е редоследот на табовите.
 */
public enum Category {
    SERVICES(R.string.tab_services, R.drawable.ic_logo_repair),
    ENTERTAINMENT(R.string.tab_entertainment, R.drawable.ic_logo_movie),
    INDUSTRY(R.string.tab_industry, R.drawable.ic_logo_factory),
    EDUCATION(R.string.tab_education, R.drawable.ic_logo_school);

    @StringRes
    public final int titleResId;
    @DrawableRes
    public final int defaultLogoResId;

    Category(@StringRes int titleResId, @DrawableRes int defaultLogoResId) {
        this.titleResId = titleResId;
        this.defaultLogoResId = defaultLogoResId;
    }

    public static Category fromPosition(int position) {
        return values()[position];
    }

    /** Безбедно парсирање од зачувано име (за читање од серверот). */
    public static Category fromNameOrNull(String value) {
        if (value == null) return null;
        for (Category c : values()) {
            if (c.name().equals(value)) return c;
        }
        return null;
    }

    /** Стандардно лого за компанија врз основа на нејзините категории. */
    @DrawableRes
    public static int defaultLogoFor(Set<Category> categories) {
        for (Category c : categories) {
            return c.defaultLogoResId;
        }
        return R.drawable.ic_logo_business;
    }
}
