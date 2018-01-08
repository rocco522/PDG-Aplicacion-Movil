package sod.applc.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import sod.applc.modelo.Logs.Log;

/**
 * Created by Ricard on 07/12/2017.
 */

public class dbLogs extends SQLiteOpenHelper  {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LOGS.db";


    public dbLogs(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + Log.TABLE_NAME + " ("
                + Log.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Log.ACCION + " TEXT NOT NULL,"
                + Log.RESUL + " TEXT NOT NULL,"
                + Log.FECHA + " TEXT NOT NULL"+ ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
