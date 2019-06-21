package com.example.omarhossam.omar_al_odah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Omar Hossam on 21/10/2018.
 */

public class DB_sqlite extends SQLiteOpenHelper {
    public static final String DBname = "mdata.db";
    public DB_sqlite (Context context){
        super(context , DBname , null , 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mtable (id INTEGER PRIMARY KEY AUTOINCREMENT , username TEXT , password TEXT , savedata TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("username" , "null");
        contentValues.put("password" , "null");
        contentValues.put("savedata" , "0");

        db.insert("mtable" , null , contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS mdata");
        onCreate(db);

    }

    public String get_check(){
        String check ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select savedata from mtable" , null);
        cursor.moveToFirst();
        check = cursor.getString(cursor.getColumnIndex("savedata"));
        return check;



    }

    public String get_username(){
        String username ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select username from mtable" , null);
        cursor.moveToFirst();
        username = cursor.getString(cursor.getColumnIndex("username"));
        return username;


    }

    public String get_password (){
        String password ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select password from mtable" , null);
        cursor.moveToFirst();
        password = cursor.getString(cursor.getColumnIndex("password"));
        return password;
    }

   public boolean updatedate (String username , String password , String check_save_data){
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put("username" , username);
       contentValues.put("password" , password);
       contentValues.put("savedata" , check_save_data);
       db.update("mtable" , contentValues , "id=?" , new String[]{"1"});
       return true;
   }




}
