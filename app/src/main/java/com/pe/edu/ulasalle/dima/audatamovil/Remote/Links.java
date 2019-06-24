package com.pe.edu.ulasalle.dima.audatamovil.Remote;

import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

public class Links {
    public static final String URL = "http://192.168.11.68:8185/Audata/service/";

    public static TtsService getTtsService(){
        return Connection.getClient(URL).create(TtsService.class);
    }
}
