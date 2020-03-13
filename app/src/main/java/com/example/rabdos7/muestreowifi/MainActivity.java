package com.example.rabdos7.muestreowifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest.permission;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int PERMISSION_ALL = 10;
    String [] PERMISSIONS ={permission.ACCESS_COARSE_LOCATION, permission.WRITE_EXTERNAL_STORAGE};

    public DatabaseAccess databaseAccess;
    public Button muestra;
    public Button guardar;
    public EditText X;
    public EditText Y;
    public EditText Z;
    public EditText area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (ContextCompat.checkSelfPermission(MainActivity.this,
                permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //guardarTodo();
        }else {

            RequestStoragePermission();

        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //validar();
        }else {

            RequestLocationPermission();

        }*/

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }



        muestra = (Button)findViewById(R.id.btn_Muestra);
        guardar = (Button)findViewById(R.id.btn_Guardar);
        X = (EditText)findViewById(R.id.etxt_X);
        Y = (EditText)findViewById(R.id.etxt_Y);
        Z = (EditText)findViewById(R.id.etxt_Z);
        area = (EditText)findViewById(R.id.etxt_Area);


        muestra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                validar();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                guardarTodo();
            }
        });

    }

    private void guardarTodo(){
        AlertDialog.Builder saveAll = new AlertDialog.Builder(this);
        saveAll.setMessage("Guardar datos?");
        saveAll.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface saveAll, int id) {

                guardarMacs();
                guardarPatrones();
                Toast.makeText(getApplicationContext(),"Se guardaron los datos de la base de datos",Toast.LENGTH_SHORT).show();

            }
        });
        saveAll.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface saveAll, int id) {

                saveAll.dismiss();

            }
        });
        AlertDialog alertDiag = saveAll.create();
        alertDiag.show();

    }

    private void muestreo(){

        System.out.println("inicia muestreo");
        String clase = X.getText().toString()+","+Y.getText().toString()+","+Z.getText().toString()+","+area.getText().toString();
        String patron = "";
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
        databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
        databaseAccess.open();
        System.out.println(wifiManager.isScanAlwaysAvailable());
        System.out.println(wifiScanResultList.size()+" redes disponibles");
        for (int i =0;i< wifiScanResultList.size();i++){

            System.out.println("red "+i);
            System.out.println(wifiScanResultList.get(i).BSSID.toUpperCase()+","+wifiScanResultList.get(i).level);
            databaseAccess.insertMac(wifiScanResultList.get(i).BSSID.toUpperCase().toString());
            patron += wifiScanResultList.get(i).BSSID.toUpperCase()+","+wifiScanResultList.get(i).level+",";
        }
        System.out.println("termina muestreo");
        databaseAccess.insertValues(patron,clase);
        databaseAccess.close();

        X.setText("");
        Y.setText("");
        Z.setText("");
        area.setText("");

    }

    private void validar(){

        AlertDialog.Builder verificar = new AlertDialog.Builder(this);

        if (X.length()==0){

            verificar.setMessage("Falta valor de eje X");
            verificar.setCancelable(true);
            verificar.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (Y.length()==0){

            verificar.setMessage("Falta valor de eje Y");
            verificar.setCancelable(true);
            verificar.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if(Z.length()==0){

            verificar.setMessage("Falta valor de eje Z");
            verificar.setCancelable(true);
            verificar.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        }else if (area.length()==0){

            verificar.setMessage("Falta valor de Area");
            verificar.setCancelable(true);
            verificar.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();

        } else {

            verificar.setMessage("Tomar muestra?");
            verificar.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface verificar, int id) {
                    muestreo();
                }
            });
            verificar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface verificar, int id) {
                    verificar.dismiss();
                }
            });
            AlertDialog alertDiag = verificar.create();
            alertDiag.show();
        }

    }

    private void guardarMacs(){

        databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
        databaseAccess.open();

        String macs = databaseAccess.extraerMacs();

        FileWriter fWriter;
        File sdCardFile = new File(Environment.getExternalStorageDirectory().getPath() + "/macs.txt");
        Log.d("TAG", sdCardFile.getPath());
        try{
            fWriter = new FileWriter(sdCardFile, false);
            fWriter.write(macs);
            fWriter.flush();
            fWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        databaseAccess.close();

    }

    private void guardarPatrones(){

        databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
        databaseAccess.open();

        String patrones = databaseAccess.extraerPatrones();

        FileWriter fWriter;
        File sdCardFile = new File(Environment.getExternalStorageDirectory().getPath() + "/patrones.txt");
        Log.d("TAG", sdCardFile.getPath());
        try{
            fWriter = new FileWriter(sdCardFile, false);
            fWriter.write(patrones);
            fWriter.flush();
            fWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        databaseAccess.close();



    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
