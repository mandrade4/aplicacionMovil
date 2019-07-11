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

    @Multipart
    @POST("funcion22")
    Call<ResponseBody> mp3PdfPageInicioPageFin(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin);

    @Multipart
    @POST("funcion20")
    Call<ResponseBody> mp3PdfPagIPagFPalIPalF(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("pini") RequestBody pini, @Part("pfin") RequestBody pfin);

    @Multipart
    @POST("funcion18")
    Call<ResponseBody> mp3PdfPagIPagFPalIPalFsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("pini") RequestBody pini, @Part("pfin") RequestBody pfin, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion19")
    Call<ResponseBody> mp3PdfBooMarsL(@Part("uploadedFile") RequestBody uploadedFile,@Part("bookmark") RequestBody bookmark, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion23")
    Call<ResponseBody> mp3PdfBooMar(@Part("uploadedFile") RequestBody uploadedFile,@Part("bookmark") RequestBody bookmark);

    @Multipart
    @POST("funcion24")
    Call<ResponseBody> mp3PdfPagIPagFsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion25")
    Call<ResponseBody> mp3PdfsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion21")
    Call<ResponseBody> mp3Pdf(@Part("uploadedFile") RequestBody uploadedFile);

    //AAC
    @Multipart
    @POST("funcion27")
    Call<ResponseBody> aacPdfPageInicio(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio);

    @Multipart
    @POST("funcion30")
    Call<ResponseBody> aacPdfPageInicioPageFin(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin);

    @Multipart
    @POST("funcion26")
    Call<ResponseBody> aacPdfPagIPagFPalIPalF(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("pini") RequestBody pini, @Part("pfin") RequestBody pfin);

    @Multipart
    @POST("funcion29")
    Call<ResponseBody> aacPdfPagIPagFPalIPalFsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("pini") RequestBody pini, @Part("pfin") RequestBody pfin, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion033")
    Call<ResponseBody> aacPdfBooMarsL(@Part("uploadedFile") RequestBody uploadedFile,@Part("bookmark") RequestBody bookmark, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion34")
    Call<ResponseBody> aacPdfPagIPagFsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("paginaInicio") RequestBody paginaInicio, @Part("paginaFin") RequestBody paginaFin, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcion31")
    Call<ResponseBody> aacPdfBooMar(@Part("uploadedFile") RequestBody uploadedFile,@Part("bookmark") RequestBody bookmark);

    @Multipart
    @POST("funcion28")
    Call<ResponseBody> aacPdfsL(@Part("uploadedFile") RequestBody uploadedFile, @Part("sL") RequestBody sL);

    @Multipart
    @POST("funcionpdf002")
    Call<ResponseBody> aacPdf(@Part("uploadedFile") RequestBody uploadedFile);
}
