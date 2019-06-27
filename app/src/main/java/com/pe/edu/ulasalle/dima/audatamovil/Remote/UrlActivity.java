package com.pe.edu.ulasalle.dima.audatamovil.Remote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.Controller.MainActivity;
import com.pe.edu.ulasalle.dima.audatamovil.R;

public class UrlActivity extends AppCompatActivity {

    EditText edtIp;
    Button btnIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);

        int width = ds.widthPixels;
        int height = ds.heightPixels;

        getWindow().setLayout((int)(width*.4),(int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        edtIp = findViewById(R.id.edtIp);
        btnIp = findViewById(R.id.btnIp);

        btnIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = edtIp.getText().toString();
                Integer CODE_IP = 2;
                Toast.makeText(UrlActivity.this, "Ip: "+ ip, Toast.LENGTH_SHORT).show();
                Toast.makeText(UrlActivity.this, "Ip seteada correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("ip", ip);
                i.putExtra("code_ip", CODE_IP);
                startActivity(i);
            }
        });
    }
}
