package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
import com.pe.edu.ulasalle.dima.audatamovil.Service.HtmlService;

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

public class HtmlActivity extends AppCompatActivity {
    HtmlService htmlService;
    Button btnEnviarHTML;
    EditText edtHtmlTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

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

        String ip=getIntent().getStringExtra("ip");
        System.out.println("esha:"+ip);
        htmlService = Links.getHtmlService(ip);
        btnEnviarHTML=findViewById(R.id.btnEnviarHTML);
        edtHtmlTag=findViewById(R.id.edtHtmlTag);

        btnEnviarHTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getIntent().getStringExtra("url");
                String tag= edtHtmlTag.getText().toString();
                System.out.println("papa"+url);
                if(edtHtmlTag.getText().toString().trim().length()==0){
                        sendHtmlToMp3(url);
                }else if(edtHtmlTag.getText().toString() != null){
                        sendHtmlToMp3Tag(url,tag);
                }
            }
        });
    }

    public void sendHtmlToMp3(String url){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        Call<ResponseBody> call=htmlService.mp3Html(urlHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudio(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(HtmlActivity.this, "Error conexion con el Servidor 2", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(HtmlActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void sendHtmlToMp3Tag(String url,String tag){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody tagHtml = RequestBody.create(MediaType.parse("text/plain"), tag);
        Call<ResponseBody> call=htmlService.mp3HtmlTagContents(urlHtml,tagHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudio(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(HtmlActivity.this, "Error conexion con el Servidor 2", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(HtmlActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
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


