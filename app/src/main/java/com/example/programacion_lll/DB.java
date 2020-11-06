package com.example.programacion_lll;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {

    static String nameDB = "db_tienda"; //declaracion de la instancia de la BD
    static String tblProducto = "CREATE TABLE productos(idProducto integer primary key autoincrement, codigo text, descripcion text, marca text, presentacion text, precio text, url text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nameDB, factory, version); //nameDB -> Creacion de la BD en SQLite...
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblProducto);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Cursor mantenimientoProducto(String accion, String[] data){
        SQLiteDatabase sqLiteDatabaseReadable=getReadableDatabase();
        SQLiteDatabase sqLiteDatabaseWritable=getWritableDatabase();
        Cursor cursor = null;
        switch (accion){
            case "consultar":
                cursor=sqLiteDatabaseReadable.rawQuery("SELECT * FROM productos ORDER BY codigo ASC", null);
                break;
            case "nuevo":
                sqLiteDatabaseWritable.execSQL("INSERT INTO productos (codigo,descripcion,marca,presentacion,precio,url) VALUES('"+ data[1] +"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"')");
                break;
            case "modificar":
                sqLiteDatabaseWritable.execSQL("UPDATE productos SET codigo='"+ data[1] +"',descripcion='"+data[2]+"',marca='"+data[3]+"',presentacion='"+data[4]+"', precio='"+data[5]+"', url='"+data[6]+"' WHERE idProducto='"+data[0]+"'");
                break;
            case "eliminar":
                sqLiteDatabaseWritable.execSQL("DELETE FROM productos WHERE idProducto='"+ data[0] +"'");
                break;
        }
        return cursor;
    }
}
