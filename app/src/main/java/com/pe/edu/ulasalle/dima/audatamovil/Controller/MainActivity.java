package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;
import com.pe.edu.ulasalle.dima.audatamovil.Remote.Links;
import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText edtTts;
    Button btnTts;

    TtsService ttsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTts = findViewById(R.id.edtTts);
        btnTts = findViewById(R.id.btnTts);

        ttsService = Links.getTtsService();

        btnTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtTts.getText().toString() == null || edtTts.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Se necesita un Texto", Toast.LENGTH_SHORT).show();
                } else {
                    sendText(edtTts.getText().toString());
                }

            }
        });
    }

    public void sendText(String text){
        Call<ResponseBody> call = ttsService.sendText(text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Nombre enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta:", response.body().toString());
                    System.out.println(response.body());
                } else {
                    Toast.makeText(MainActivity.this, "Error conexion con el Servidor", Toast.LENGTH_SHORT).show();
                    Integer error = response.code();
                    Toast.makeText(MainActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }
    //Code here ...
}
