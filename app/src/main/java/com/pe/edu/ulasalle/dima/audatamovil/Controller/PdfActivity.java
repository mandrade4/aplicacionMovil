package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;
import com.pe.edu.ulasalle.dima.audatamovil.Remote.Links;
import com.pe.edu.ulasalle.dima.audatamovil.Service.PdfService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfActivity extends AppCompatActivity {

    PdfService pdfService;

    EditText edtPdfpi;
    EditText edtPdfpf;
    EditText edtPdfPalabraInicio;
    EditText edtPdfPalabraFin;

    Button btnEnviarPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);

        int width = ds.widthPixels;
        int height = ds.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        String ip = getIntent().getStringExtra("ip");
        pdfService = Links.getPdfService(ip);

        edtPdfpi = findViewById(R.id.edtPdfpi);
        edtPdfpf = findViewById(R.id.edtPdfpf);
        edtPdfPalabraInicio = findViewById(R.id.edtPdfPalabraInicio);
        edtPdfPalabraFin = findViewById(R.id.edtPdfPalabraFin);

        btnEnviarPdf = findViewById(R.id.btnEnviarPdf);

        btnEnviarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pdf =  getIntent().getStringExtra("pdf");
                File uploadedFile = new File(pdf);

                String pageInicio = edtPdfpi.getText().toString();
                String pageFin = edtPdfpf.getText().toString();
                String palInicio = edtPdfPalabraInicio.getText().toString();
                String palFin = edtPdfPalabraFin.getText().toString();

                if(edtPdfpi.getText().toString() != null && edtPdfpf.getText().toString().trim().length() == 0){
                    sendPdftoMp3withPageStart(uploadedFile,pageInicio);
                } else if(edtPdfpi.getText().toString() != null && edtPdfpf.getText().toString() !=null && edtPdfPalabraInicio.getText().toString().trim().length() == 0 && edtPdfPalabraFin.getText().toString().trim().length() == 0){
                    sendPdftoMp3withPageStartandPageEnd(uploadedFile, pageInicio, pageFin);
                } else if(edtPdfpi.getText().toString() != null && edtPdfpf.getText().toString() !=null && edtPdfPalabraInicio.getText().toString() != null && edtPdfPalabraFin.getText().toString()!= null) {
                    sendPdftoMp3withPagStPagEnWorStWorEnd(uploadedFile,pageInicio, pageFin, palInicio, palFin);
                }
            }
        });

    }

    public void sendPdftoMp3withPageStart(File pdf, String paginaInicio){
        RequestBody filePdf = RequestBody.create(MediaType.parse("application/pdf"), pdf);
        RequestBody pageIni = RequestBody.create(MediaType.parse("text/plain"), paginaInicio);
        Call<ResponseBody> call = pdfService.mp3PdfPageInicio(filePdf, pageIni);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PdfActivity.this,"Pdf enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudio(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(PdfActivity.this, "Error conexion con el Servidor 1", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(PdfActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void sendPdftoMp3withPageStartandPageEnd(File pdf, String paginaInicio, String paginaFin){
        RequestBody filePdf = RequestBody.create(MediaType.parse("application/pdf"), pdf);
        RequestBody pageIni = RequestBody.create(MediaType.parse("text/plain"), paginaInicio);
        RequestBody pageFin = RequestBody.create(MediaType.parse("text/plain"), paginaFin);
        Call<ResponseBody> call = pdfService.mp3PdfPageInicioPageFin(filePdf, pageIni, pageFin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PdfActivity.this,"Pdf enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudio(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(PdfActivity.this, "Error conexion con el Servidor 2", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(PdfActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void sendPdftoMp3withPagStPagEnWorStWorEnd(File pdf, String paginaInicio, String paginaFin, String palabraInicio, String palabraFin){
        RequestBody filePdf = RequestBody.create(MediaType.parse("application/pdf"), pdf);
        RequestBody pageIni = RequestBody.create(MediaType.parse("text/plain"), paginaInicio);
        RequestBody pageFin = RequestBody.create(MediaType.parse("text/plain"), paginaFin);
        RequestBody palIni = RequestBody.create(MediaType.parse("text/plain"), palabraInicio);
        RequestBody palFin = RequestBody.create(MediaType.parse("text/plain"), palabraFin);

        Call<ResponseBody> call = pdfService.mp3PdfPagIPagFPalIPalF(filePdf, pageIni, pageFin, palIni, palFin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PdfActivity.this,"Pdf enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudio(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }
                } else {
                    Toast.makeText(PdfActivity.this, "Error conexion con el Servidor 2", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(PdfActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }

    private void saveFileAudio(byte[] mp3SoundByteArray) {
        UUID uuid = UUID.randomUUID();

        String uuidStringRandom = uuid.toString();

        try {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    uuidStringRandom + ".mp3"
            );
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(mp3SoundByteArray);
            fos.close();
            Log.d("FileSize: ", String.valueOf(file.getTotalSpace()));
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("Error audio: ", String.valueOf(ex));
        }
    }
}
