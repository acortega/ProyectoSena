package com.asistencia.sena.aprendiz.appasistir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.asistencia.sena.aprendiz.appasistir.ConnectionServer.Connection;
import com.asistencia.sena.aprendiz.appasistir.ConnectionServer.Constantes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class SplashInicio extends Activity {
    private static final long SPLASH_SCREEN_DELAY = 2000;
    String parasAsistir[];
    String ficha,id,tipoId,nombre;
    Connection connectionServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_inicio);

    }

    public void asistir(View view){
        IntentIntegrator integrator= new IntentIntegrator(this);
        integrator.setCaptureLayout(R.layout.custom_capture_layout);
        integrator.setLegacyCaptureLayout(R.layout.custom_capture_layout);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.autoWide();
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String datoInicial=result.getContents();
                Toast.makeText(this, "Scanned: " + datoInicial, Toast.LENGTH_LONG).show();
                String[] datos=datoInicial.split(Constantes.SEPARATOR);
                for(String dato:datos){
                    String clave[]=dato.split(Constantes.SEPARATOR_CLAVE);
                    switch (clave[0]){
                        case Constantes.FICHA:
                            ficha=clave[1];
                            break;
                        case Constantes.ID:
                            id=clave[1];
                            break;
                        case Constantes.TIPO_ID:
                            tipoId=clave[1];
                            break;
                        case Constantes.NOMBRE:
                            nombre=clave[1];
                            break;
                    }
                }
                sendAsistencia();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void sendAsistencia(){
        parasAsistir= new String[]{Constantes.METHOD_NAME_LISTADO_ASISTIR_APRENDIZ, ficha, id};
        connectionServer=new Connection();
        String msg="Aprendiz: "+nombre+"/n ha habido un problema ... intentalo nuevamente!!";
        try {
            String result=connectionServer.execute(parasAsistir).get();
            if(!result.equals("false")){
                msg="Aprendiz: "+nombre+"/n su asistencia ha sido registrada";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();

        }

        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("ASISTENCIA ...");
        alert.setIcon(R.drawable.ic_launcher);
        alert.setMessage(msg);
        alert.setPositiveButton("ACEPTAR",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
