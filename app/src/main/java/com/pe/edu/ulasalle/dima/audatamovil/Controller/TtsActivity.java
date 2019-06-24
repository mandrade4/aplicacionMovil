package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;

public class TtsActivity extends AppCompatActivity {

    RadioGroup ttsRadioGroup;
    RadioButton ttsRadioButton;
    Button btnEnviarTts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

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

        ttsRadioGroup = findViewById(R.id.ttsRadioGroup);
        btnEnviarTts = findViewById(R.id.btnEnviarTts);

        btnEnviarTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = ttsRadioGroup.getCheckedRadioButtonId();
                ttsRadioButton = findViewById(radioId);
                Toast.makeText(TtsActivity.this,"Radio seleccionado"+ ttsRadioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkButtonTts(View v) {
        int radioId = ttsRadioGroup.getCheckedRadioButtonId();

        ttsRadioButton = findViewById(radioId);

        Toast.makeText(this, "Radio seleccinado" + ttsRadioButton.getText(),Toast.LENGTH_SHORT).show();
    }
}
