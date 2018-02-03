package com.sai.weatherio.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by sai on 2/3/18.
 */

public class Resource<T> {


    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String LOADING = "loading";
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SUCCESS,
            ERROR,
            LOADING
    })
    public @interface Status {}

    @NonNull
    public final @Status String status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    public Resource(@NonNull String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<T>(SUCCESS, null, data);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(ERROR, msg, data);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, null, data);
    }

}
