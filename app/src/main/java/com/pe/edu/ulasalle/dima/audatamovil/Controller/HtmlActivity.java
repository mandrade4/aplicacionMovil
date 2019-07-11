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
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    EditText edtHtmlStopList;
    EditText edtHtmlStopTagContentList;
    EditText edtHtmlTagDivisor;
    EditText edtHtmlTagInicio;
    EditText edtHtmlTagFin;

    RadioGroup htmlRadioGroup;
    RadioButton htmlRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);

        int width = ds.widthPixels;
        int height = ds.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.6));

        htmlRadioGroup = findViewById(R.id.htmlRadioGroup);

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
        edtHtmlStopList=findViewById(R.id.edtHtmlStopList);
        edtHtmlStopTagContentList=findViewById(R.id.edtHtmlStopTagContentList);
        edtHtmlTagDivisor=findViewById(R.id.edtHtmlTagDivisor);
        edtHtmlTagInicio=findViewById(R.id.edtHtmlTagInicio);
        edtHtmlTagFin=findViewById(R.id.edtHtmlTagFin);

        btnEnviarHTML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = htmlRadioGroup.getCheckedRadioButtonId();
                htmlRadioButton = findViewById(radioId);
                String formato = htmlRadioButton.getText().toString();

                String url=getIntent().getStringExtra("url");
                String tag= edtHtmlTag.getText().toString();
                String stopTag= edtHtmlStopList.getText().toString();
                String contentTagList= edtHtmlStopTagContentList.getText().toString();
                String divisor=edtHtmlTagDivisor.getText().toString();
                String palInicio=edtHtmlTagInicio.getText().toString();
                String palFin=edtHtmlTagFin.getText().toString();

                System.out.println("papa"+url);

                if(formato == null | formato.length() == 0){
                    Toast.makeText(HtmlActivity.this, "Selecciona un formato", Toast.LENGTH_SHORT).show();
                } else {
                    if(formato.equals("MP3")) {
                        if(edtHtmlTagInicio.getText().toString().trim().length() == 0 && edtHtmlTagFin.getText().toString().trim().length() == 0 && edtHtmlTag.getText().toString().trim().length() == 0 && edtHtmlTagDivisor.getText().toString().trim().length()!=0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 ) {
                            sendHtmlMp3Divisor(url,divisor);
                            System.out.println("Func mp3 divisor");
                        }else if(edtHtmlTagInicio.getText().toString().trim().length() == 0 && edtHtmlTagFin.getText().toString().trim().length() == 0 && edtHtmlTag.getText().toString().trim().length() != 0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToMp3Tag(url,tag);
                            System.out.println("Func solo mp3 tag");
                        }else if(edtHtmlTagInicio.getText().toString().trim().length() == 0 && edtHtmlTagFin.getText().toString().trim().length() == 0 && edtHtmlTag.getText().toString().trim().length() != 0 && edtHtmlStopList.getText().toString().trim().length()!=0 && edtHtmlStopTagContentList.getText().toString().trim().length()!=0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToMp3TagStopListContentTag(url,tag,stopTag,contentTagList);
                            System.out.println("Func mp3 tagStopContent");
                        }else if(edtHtmlTagInicio.getText().toString().trim().length() == 0 && edtHtmlTagFin.getText().toString().trim().length() == 0 && edtHtmlTag.getText().toString().trim().length()==0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToMp3(url);
                            System.out.println("Func solo mp3");
                        }else if(edtHtmlTagInicio.getText().toString().trim().length() != 0 && edtHtmlTagFin.getText().toString().trim().length() != 0 && edtHtmlTag.getText().toString().trim().length()==0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendMp3HtmlPalIniPalFin(url,palInicio,palFin);
                            System.out.println("Func pala inicio fin mp3");
                        }
                    }
                    if(formato.equals("AAC")){
                        if(edtHtmlTagInicio.getText().toString().trim().length() == 0 && edtHtmlTagFin.getText().toString().trim().length() == 0 && edtHtmlTag.getText().toString().trim().length() == 0 && edtHtmlTagDivisor.getText().toString().trim().length()!=0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 ) {
                            sendHtmlAacDivisor(url,divisor);
                            System.out.println("Func aac divisor");
                        }else if(edtHtmlTag.getText().toString().trim().length() != 0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToAacTag(url,tag);
                            System.out.println("Func solo aac tag");
                        }else if(edtHtmlTag.getText().toString().trim().length() != 0 && edtHtmlStopList.getText().toString().trim().length()!=0 && edtHtmlStopTagContentList.getText().toString().trim().length()!=0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToAacTagStopListContentTag(url,tag,stopTag,contentTagList);
                            System.out.println("Func aac tagStopContent");
                        }else if(edtHtmlTag.getText().toString().trim().length()==0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendHtmlToAac(url);
                            System.out.println("Func solo aac");
                        }else if(edtHtmlTagInicio.getText().toString().trim().length() != 0 && edtHtmlTagFin.getText().toString().trim().length() != 0 && edtHtmlTag.getText().toString().trim().length()==0 && edtHtmlStopList.getText().toString().trim().length()==0 && edtHtmlStopTagContentList.getText().toString().trim().length()==0 && edtHtmlTagDivisor.getText().toString().trim().length()==0){
                            sendAacHtmlPalIniPalFin(url,palInicio,palFin);
                            System.out.println("Func pala inicio fin aac");
                        }
                    }
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
                        saveFileAudioMP3(response.body().bytes());
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
    public void sendHtmlToMp3TagStopListContentTag(String url,String tag,String stoplist,String contentTagList){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody tagHtml = RequestBody.create(MediaType.parse("text/plain"), tag);
        RequestBody stopTagHtml = RequestBody.create(MediaType.parse("text/plain"), stoplist);
        RequestBody contentTagListHtml = RequestBody.create(MediaType.parse("text/plain"), contentTagList);
        Call<ResponseBody> call=htmlService.mp3HtmlTagSLTCL(urlHtml,tagHtml,stopTagHtml,contentTagListHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioMP3(response.body().bytes());
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
                        saveFileAudioMP3(response.body().bytes());
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

    public void sendHtmlMp3Divisor(String url,String divisor){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody divisorHtml = RequestBody.create(MediaType.parse("text/plain"), divisor);
        Call<ResponseBody> call=htmlService.mp3HtmlDivisor(urlHtml,divisorHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioMP3(response.body().bytes());
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

    //////////////////////aac

    public void sendHtmlAacDivisor(String url,String divisor){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody divisorHtml = RequestBody.create(MediaType.parse("text/plain"), divisor);
        Call<ResponseBody> call=htmlService.aacHtmlDivisor(urlHtml,divisorHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
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

    public void sendHtmlToAac(String url){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        Call<ResponseBody> call=htmlService.aacHtml(urlHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
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
    public void sendHtmlToAacTagStopListContentTag(String url,String tag,String stoplist,String contentTagList){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody tagHtml = RequestBody.create(MediaType.parse("text/plain"), tag);
        RequestBody stopTagHtml = RequestBody.create(MediaType.parse("text/plain"), stoplist);
        RequestBody contentTagListHtml = RequestBody.create(MediaType.parse("text/plain"), contentTagList);
        Call<ResponseBody> call=htmlService.aacHtmlTagSLTCL(urlHtml,tagHtml,stopTagHtml,contentTagListHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
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
    public void sendHtmlToAacTag(String url,String tag){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody tagHtml = RequestBody.create(MediaType.parse("text/plain"), tag);
        Call<ResponseBody> call=htmlService.aacHtmlTagContents(urlHtml,tagHtml);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
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

    public void sendMp3HtmlPalIniPalFin(String url, String pI, String pF){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody palInicio = RequestBody.create(MediaType.parse("text/plain"), pI);
        RequestBody palFin = RequestBody.create(MediaType.parse("text/plain"), pF);
        Call<ResponseBody> call=htmlService.mp3HtmlPalIniPalFin(urlHtml,palInicio,palFin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioMP3(response.body().bytes());
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

    public void sendAacHtmlPalIniPalFin(String url, String pI, String pF){
        RequestBody urlHtml = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody palInicio = RequestBody.create(MediaType.parse("text/plain"), pI);
        RequestBody palFin = RequestBody.create(MediaType.parse("text/plain"), pF);
        Call<ResponseBody> call=htmlService.aacHtmlPalIniPalFin(urlHtml,palInicio,palFin);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HtmlActivity.this,"Url enviada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta to String:", response.body().toString());
                    try {
                        saveFileAudioAAC(response.body().bytes());
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


