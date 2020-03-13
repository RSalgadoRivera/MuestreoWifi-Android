package com.example.rabdos7.muestreowifi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.ScanResult;
import android.view.View;

import java.util.List;

/**
 * Created by rabdos7 on 24/11/18.
 */

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAccess(context);
        }return instance;

    }

    public void open(){
        this.database = openHelper.getWritableDatabase();
    }

    public void close(){
        if (database != null){
            this.database.close();
        }
    }

    public void insertMac(String mac){

        System.out.println(mac);

        Cursor cursor = null;

        try {
            String query = "SELECT mac FROM MACS WHERE mac=?";
            cursor = database.rawQuery(query, new String[] {mac + ""});
            if(cursor.getCount() == 0) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("mac",mac);

                long result = database.insert("MACS",null,contentValues);
                if (result==-1){
                    System.out.println("error insertar mac");
                }else {

                    System.out.println("ok insert");
                }
            }

        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println("fin de guardar macs");

        //extraer todas las macs
        /*try {
            String query = "SELECT * FROM MACS";
            cursor = database.rawQuery(query,null);
            System.out.println("num elementos "+cursor.getCount());
            while(cursor.moveToNext()){
                System.out.println(cursor.getString(cursor.getColumnIndex("mac")));
            }

        }catch (Exception e){
            System.out.println(e);
        }*/

    }

    public void insertValues(String patron, String clase){

        ContentValues contentValues = new ContentValues();
        contentValues.put("patron",patron);
        contentValues.put("clase",clase);

        long result = database.insert("RESULTS",null,contentValues);
        if (result==-1){
            System.out.println("error insertar resultados");
        }else {
            System.out.println(patron+clase);
            System.out.println("ok insert");
        }

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM RESULTS";
            cursor = database.rawQuery(query,null);
            System.out.println("num elementos "+cursor.getCount());
            while(cursor.moveToNext()){
                System.out.println(cursor.getString(cursor.getColumnIndex("patron"))+cursor.getString(cursor.getColumnIndex("clase")));
            }

        }catch (Exception e){
            System.out.println(e);
        }


    }

    public String extraerMacs(){

        Cursor cursor = null;
        String macs = "";

        try {
            String query = "SELECT * FROM MACS";
            cursor = database.rawQuery(query,null);
            System.out.println("num elementos "+cursor.getCount());
            while(cursor.moveToNext()){
                macs += cursor.getString(cursor.getColumnIndex("mac"))+"\n";
            }

        }catch (Exception e){
            System.out.println(e);
        }

        return macs;
    }

    public String  extraerPatrones(){

        String patrones = "";
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM RESULTS";
            cursor = database.rawQuery(query,null);
            while(cursor.moveToNext()){
                patrones += cursor.getString(cursor.getColumnIndex("patron"))+cursor.getString(cursor.getColumnIndex("clase"))+"\n";
            }

        }catch (Exception e){
            System.out.println(e);
        }

        return patrones;

    }

}
