package com.pe.edu.ulasalle.dima.audatamovil.Service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TtsService {

    @GET("tts/talk/{text}")
    Call<ResponseBody> sendText(@Path("text") String text);

    @GET("tts/talk2/{text}")
    Call<ResponseBody> sendText2(@Path("text") String text);
}
