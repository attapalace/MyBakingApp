package com.examples.apps.atta.mybakingapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    public static final String BASE_URL
            = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    static Retrofit retrofit;

    public static Retrofit getInstance(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder().create();

        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .callFactory(httpClient.build())
                    .build();
        }

        return retrofit;
    }
}
