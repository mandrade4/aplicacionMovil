package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;
import com.pe.edu.ulasalle.dima.audatamovil.Remote.Links;
import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TtsActivity extends AppCompatActivity {

    RadioGroup ttsRadioGroup;
    RadioButton ttsRadioButton;
    Button btnEnviarTts;

    TtsService ttsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);

        int width = ds.widthPixels;
        int height = ds.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        ttsRadioGroup = findViewById(R.id.ttsRadioGroup);
        btnEnviarTts = findViewById(R.id.btnEnviarTts);


        String ip = getIntent().getStringExtra("ip");
        ttsService = Links.getTtsService(ip);

        btnEnviarTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = ttsRadioGroup.getCheckedRadioButtonId();
                ttsRadioButton = findViewById(radioId);
                String text = getIntent().getStringExtra("text");
                String formato = ttsRadioButton.getText().toString();

                if(formato == null | formato.length() == 0){
                    Toast.makeText(TtsActivity.this, "Selecciona un formato", Toast.LENGTH_SHORT).show();
                } else {
                    if(formato.equals("MP3")){
                        sendTextToMP3(text,formato);
                    }
                    if(formato.equals("AAC")){
                        sendTextToAAC(text,formato);
                    }
                }
                Toast.makeText(TtsActivity.this,"Texto enviado"+ getIntent().getStringExtra("text"),Toast.LENGTH_SHORT).show();
                Toast.makeText(TtsActivity.this,"Radio seleccionado"+ ttsRadioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendTextToMP3(String text, String formato){
        final String type = formato;
        System.out.println(text);
        Call<ResponseBody> call = ttsService.sendTextToMP3(text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(TtsActivity.this,"Nombre enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioMP3(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(TtsActivity.this, "Error conexion con el Servidor", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(TtsActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void sendTextToAAC(String text, String formato){
        final String type = formato;
        System.out.println(text);
        Call<ResponseBody> call = ttsService.sendTextToAAC(text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(TtsActivity.this,"Nombre enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
                    } catch (IOException e) {
                        Log.i("Error audio:", e.toString());
                    }

                } else {
                    Toast.makeText(TtsActivity.this, "Error conexion con el Servidor", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(TtsActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }


    private void saveFileAudioMP3(byte[] mp3SoundByteArray) {
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

    private void saveFileAudioAAC(byte[] mp3SoundByteArray) {
        UUID uuid = UUID.randomUUID();
        String uuidStringRandom = uuid.toString();
        try {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    uuidStringRandom + ".aac"
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
