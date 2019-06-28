package com.pe.edu.ulasalle.dima.audatamovil.Service;

import android.content.res.TypedArray;

import java.io.File;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PdfService {

    @Multipart
    @POST("funcion17")
    Call<ResponseBody> mp3PdfPageInicio(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio);

}
