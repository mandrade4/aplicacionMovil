package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    EditText edtTts;
    Button btnTts;
    Button btnHtml;
    EditText edtHtml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);

        if (firstStart) {
            //showStartDialog();
            requestStoragePermission();
        }

        edtTts = findViewById(R.id.edtTts);
        btnTts = findViewById(R.id.btnTts);

        edtHtml = findViewById(R.id.edtHtml);
        btnHtml = findViewById(R.id.btnPdf);


        btnTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtTts.getText().toString() == null || edtTts.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Se necesita un Texto", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), TtsActivity.class);
                    i.putExtra("text",edtTts.getText().toString());
                    startActivity(i);
                }
            }
        });

        btnHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtHtml.getText().toString() == null || edtHtml.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Se necesita una URL", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), HtmlActivity.class);

                    startActivity(i);
                }
            }
        });
    }

    private void showStartDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Dialogo")
                .setMessage("Esto solo se deberia mostrar una vez")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Se necsitan permisos")
                    .setMessage("Se necesita acceder a tus backs")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso condedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
