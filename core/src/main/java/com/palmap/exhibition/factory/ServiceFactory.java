package com.palmap.exhibition.factory;

import android.os.Environment;

import com.google.gson.Gson;
import com.palmap.exhibition.config.ServereConfig;
import com.palmap.library.utils.HttpLoggingInterceptor;

import java.io.File;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by 王天明 on 2015/12/18 0018.
 */
public class ServiceFactory {

    private static final String endpoint = ServereConfig.HOST;
    private static Retrofit retrofit;
    private static Gson gson = new Gson();
    private static WeakHashMap<String, Object> serviceCache = new WeakHashMap<>();

    static {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(15, TimeUnit.SECONDS);

        builder.cache(new Cache(new File(Environment.getExternalStorageDirectory() + ServereConfig.CECHE_FILE), ServereConfig.CECHE_SIZE));

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(httpLoggingInterceptor);

        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static <T> T create(Class<T> service) {
        T t = (T) serviceCache.get(service.getSimpleName());
        if (null == t) {
            long time = System.currentTimeMillis();
            t = retrofit.create(service);
            serviceCache.put(service.getSimpleName(), t);
        }
        return t;
    }

    public static Gson getGson() {
        return gson;
    }
}