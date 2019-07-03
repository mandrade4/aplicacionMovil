package com.pe.edu.ulasalle.dima.audatamovil.Service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TtsService {

    @GET("funcion1/{text}")
    Call<ResponseBody> sendTextToMP3(@Path("text") String text);

    @GET("funcion2/{text}")
    Call<ResponseBody> sendTextToAAC(@Path("text") String text);
}
