package com.asistencia.sena.aprendiz.appasistir.ConnectionServer;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by usuario on 20/11/2014.
 */
public class Connection extends AsyncTask<String,Integer,String> {
    @Override
    protected String doInBackground(String... strings) {

        String resul = "true";

        final String NAMESPACE = Constantes.NAMESPACE;
        final String URL=Constantes.URL;
        final String METHOD_NAME = strings[0];
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        String fi_codigo=strings[1];
        String identificacion=strings[2];


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        GregorianCalendar fecha = new GregorianCalendar();
        String anio = ""+fecha.get(Calendar.YEAR);
        String mes = ""+(fecha.get(Calendar.MONTH)+1);
        String dia =""+fecha.get(Calendar.DAY_OF_MONTH);
        String hora=""+fecha.get(Calendar.HOUR_OF_DAY);
        Log.e("HORAAAAAAAAAAAAAAAAAAA:","HORAAAAAAAAAAAAAAAAAA: "+hora);
        String fechaAsistencia= ""+anio+"/"+mes+"/"+dia;

        String horaAsistencia = ""+fecha.get(Calendar.HOUR_OF_DAY)+ fecha.get(Calendar.MINUTE);
        Log.e("HORAAAAAAAAAAAAAAAAAAA:","HORAAAAAAAAAAAAAAAAAA22222222222222: "+horaAsistencia);
        request.addProperty("fi_codigo", 123);
        request.addProperty("identificacion", "1");
        request.addProperty("hora", 700);
        request.addProperty("fecha", fechaAsistencia);

        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();
            Log.i("Mensaje", res);
            resul=res;
            if(res.equals("false"))
                resul = "false";
        }
        catch (Exception e)
        {
            resul = "false";
            Log.i("Error : ", e.toString());
        }

        return resul;



    }
}
