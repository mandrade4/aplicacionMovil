package com.pe.edu.ulasalle.dima.audatamovil.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface TtsService {

    @GET("tts/prueba/{name}")
    Call sendText(@Body String text);
}
