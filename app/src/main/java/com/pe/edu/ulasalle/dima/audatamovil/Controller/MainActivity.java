package com.pe.edu.ulasalle.dima.audatamovil.Controller;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.edu.ulasalle.dima.audatamovil.R;
import com.pe.edu.ulasalle.dima.audatamovil.Remote.Links;
import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    EditText edtTts;
    Button btnTts;


    TtsService ttsService;
    private MediaPlayer mediaPlayer = new MediaPlayer();

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
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(MainActivity.this, "Ya tienes permisos", Toast.LENGTH_SHORT).show();
                        sendText(edtTts.getText().toString());
                    } else {
                        requestStoragePermission();
                    }

                }

            }
        });
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
                sendText(edtTts.getText().toString());
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendText(String text){
        Call<ResponseBody> call = ttsService.sendText(text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Nombre enviado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    InputStream inputStream = response.body().byteStream();
                    playMp3(inputStream);

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


    private void playMp3(InputStream mp3SoundByteArray) {

        /*int length = 22050 * 20; //10 seconds long
        byte[] data = new byte[length];
        //new Random().nextBytes(data);

        System.out.println("Data generada" + data);
        System.out.println("Data recivida" + mp3SoundByteArray);

        final int TEST_SR = 22050; //This is from an example I found online.
        final int TEST_CONF = AudioFormat.CHANNEL_OUT_MONO;
        final int TEST_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        final int TEST_MODE = AudioTrack.MODE_STATIC; //I need static mode.
        final int TEST_STREAM_TYPE = AudioManager.STREAM_ALARM;
        AudioTrack track = new AudioTrack(TEST_STREAM_TYPE, TEST_SR, TEST_CONF, TEST_FORMAT, length, TEST_MODE);
        track.write(mp3SoundByteArray, 0, mp3SoundByteArray.length);
        track.play();*/

        /*
        String encoded = Base64.encodeToString(mp3SoundByteArray, 0);
        Log.d("Encoded string: ", encoded);

        byte[] decoded = Base64.decode(encoded, 0);
        Log.d("Decoded bytes: ",  Arrays.toString(decoded));*/

        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
        while ((bytesRead = mp3SoundByteArray.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        } catch (IOException ex){
            ex.printStackTrace();
            Log.d("Error audio: ", String.valueOf(ex));
        }





        int TEST_SR = 48000; //This is from an example I found online.
        //int length = AudioTrack.getMinBufferSize(TEST_SR, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        int length = 22050 * 20; //10 seconds long
        byte[] data = new byte[length];
        new Random().nextBytes(data);
        System.out.println("Data generada" + data);

        AudioTrack track = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(TEST_SR)
                        .build())
                .setBufferSizeInBytes(length)
                .build();

        track.setPlaybackRate(TEST_SR);
        track.write(output.toByteArray(), 0, length );
        track.play();



        /*
        System.out.println("Data creada" + output.toByteArray());
        try {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "audio.mp3"
            );
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(output.toByteArray());
            Log.d("FileSize: ", String.valueOf(file.getTotalSpace()));
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("Error audio: ", String.valueOf(ex));
        }*/


        /*
        String encoded = Base64.encodeToString(mp3SoundByteArray, 0);
        Log.d("Encoded string: ", encoded);

        byte[] decoded = Base64.decode(encoded, 0);
        Log.d("Decoded bytes: ",  Arrays.toString(decoded));
        /*
        try {

            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "audio.mp3"
                    );
            FileOutputStream fos = new FileOutputStream(file);
            Integer bufferSize = 100;
            System.out.println("Tamañano" + mp3SoundByteArray.length);
            Log.d("FileSize: ", String.valueOf(file.getTotalSpace()));
            fos.close();
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize*2);
            int tick = 0;

            for (int i = 0; i < mp3SoundByteArray.length; i++) {
                tick++;
                fos.write(mp3SoundByteArray, i-tick+1, tick);
                buffer.clear();
                tick = 0;
            }

            Log.d("FileSize: ", String.valueOf(file.getTotalSpace()));
            fos.close();*/

            /*
            File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();
            mediaPlayer.reset();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare();
            mediaPlayer.start();*/
        /*
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("Error audio: ", String.valueOf(ex));
        }*/
    }
    //Code here ...
}
