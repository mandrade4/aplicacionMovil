package com.pe.edu.ulasalle.dima.audatamovil.Remote;

import com.pe.edu.ulasalle.dima.audatamovil.Service.PdfService;
import com.pe.edu.ulasalle.dima.audatamovil.Service.TtsService;

public class Links {

    public static TtsService getTtsService(String ip){
        return Connection.getClient("http://"+ip+"/Audata/service/").create(TtsService.class);
    }

    public static PdfService getPdfService(String ip){
        return Connection.getClient("http://"+ip+"/Audata/service/api/").create(PdfService.class);
    }
}
