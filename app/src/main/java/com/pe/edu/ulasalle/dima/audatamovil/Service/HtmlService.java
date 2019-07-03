package com.pe.edu.ulasalle.dima.audatamovil.Service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HtmlService {
    @Multipart
    @POST("funcionhtml1")
    Call<ResponseBody> mp3Html(@Part("uploadedFile2")RequestBody uploadFile2);

    @Multipart
    @POST("funcionhtml2")
    Call<ResponseBody> mp3HtmlTagContents(@Part("uploadedFile2")RequestBody uploadFile2,@Part("tag")RequestBody tag);

    @Multipart
    @POST("funcionhtml3")
    Call<ResponseBody> mp3HtmlTagSLTCL(@Part("uploadedFile2")RequestBody uploadFile2,@Part("tag")RequestBody tag,@Part("stL")RequestBody stL,@Part("stcL")RequestBody stcL);

    @Multipart
    @POST("funcionhtml4")
    Call<ResponseBody> mp3HtmlDivisor(@Part("uploadedFile2")RequestBody uploadFile2,@Part("divisor")RequestBody divisor);
}
