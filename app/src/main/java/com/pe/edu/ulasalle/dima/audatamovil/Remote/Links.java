package com.pe.edu.ulasalle.dima.audatamovil.Remote;

import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

public class Links {
    public static final String URL = "http://192.168.43.6:8080/Audata/service/";

    public static TtsService getTtsService(){
        return Connection.getClient(URL).create(TtsService.class);
    }
}
