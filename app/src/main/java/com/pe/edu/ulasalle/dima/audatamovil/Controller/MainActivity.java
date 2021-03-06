package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;
import com.pe.edu.ulasalle.dima.audatamovil.Remote.UrlActivity;

import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    private int CODE_IP = 1;

    EditText edtTts;
    Button btnTts;

    EditText edtHtml;
    Button btnHtml;

    TextView textPdf;
    Button btnPdf;

    Button btnOpenIp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart",true);

        if (firstStart) {
            requestStoragePermission();
        }

        edtTts = findViewById(R.id.edtTts);
        btnTts = findViewById(R.id.btnTts);

        edtHtml = findViewById(R.id.edtHtml);
        btnHtml = findViewById(R.id.btnHtml);

        textPdf =findViewById(R.id.textPdf);
        btnPdf = findViewById(R.id.btnPdf);

        btnOpenIp = findViewById(R.id.btnOpenIp);

        btnOpenIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UrlActivity.class);
                startActivity(i);
            }
        });

        btnTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtTts.getText().toString() == null || edtTts.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Se necesita un Texto", Toast.LENGTH_SHORT).show();
                } else {
                    String ip = getIntent().getStringExtra("ip");
                    Intent i = new Intent(getApplicationContext(), TtsActivity.class);
                    i.putExtra("text",edtTts.getText().toString());
                    i.putExtra("ip",ip);
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
                    String url=edtHtml.getText().toString();
                    String ip = getIntent().getStringExtra("ip");
                    Intent i = new Intent(getApplicationContext(), HtmlActivity.class);
                    i.putExtra("url",url);
                    i.putExtra("ip",ip);
                    System.out.println("urlMainAct"+url);
                    startActivity(i);
                }

            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode ==RESULT_OK){
                Uri uri = data.getData();
                String uriString =uri.toString();
                String namePdf = null;
                System.out.println("What is Data: " + data);
                if (uriString.startsWith("content://")){
                    Cursor cursor = null;
                    try{
                        cursor=getContentResolver().query(uri,null,null,null,null);
                        if(cursor!=null && cursor.moveToFirst()){
                            namePdf = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    }finally{
                        cursor.close();
                    }

                }else if(uriString.startsWith("file://")){
                    namePdf = uriString;

                }

                textPdf.setText(namePdf);
                System.out.println("Uri string: "+ uriString);
                if(textPdf.getText().toString() != "Selecciona un PDF"){
                    String ip = getIntent().getStringExtra("ip");
                    Intent i = new Intent(getApplicationContext(), PdfActivity.class);
                    String TEMP_DIR_PATH = Environment.getExternalStorageDirectory().getPath();
                    String rutapdf = TEMP_DIR_PATH + File.separator +  "Download" + File.separator + namePdf;
                    System.out.println("Ruda pdf: " + rutapdf);
                    i.putExtra("pdf", rutapdf);
                    i.putExtra("ip", ip);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Debes seleccionar un pdf", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(MainActivity.this, "Respuesta del pdf:"+namePdf, Toast.LENGTH_SHORT).show();
                System.out.println("Respuesta del pdf:" + namePdf);

            }


            }
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
                Integer code_ip = getIntent().getIntExtra("code_ip",1);
                if(CODE_IP == code_ip){
                    Intent i = new Intent(getApplicationContext(), UrlActivity.class);
                    startActivity(i);
                }
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
