package com.pe.edu.ulasalle.dima.audatamovil.Remote;

import com.pe.edu.ulasalle.dima.audatamovil.Service.PdfService;
import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

public class Links {
    private String URL;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = "http:// " + URL + "/Audata/service/";
    }

    public static TtsService getTtsService(){
        Links links = new Links();
        return Connection.getClient("http://192.168.60.102:8080/Audata/service/").create(TtsService.class);
    }

    public static PdfService getPdfService(){
        return Connection.getClient("http://192.168.60.102:8080/Audata/service/api/").create(PdfService.class);
    }
}
