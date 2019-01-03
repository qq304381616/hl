package com.hl.greendao.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseOpenHelper extends SQLiteOpenHelper {

    private final Context context;
    private final String name;
    private final int version;

    private EncryptedHelper encryptedHelper;
    private boolean loadSQLCipherNativeLibs = true;

    public DatabaseOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        this.name = name;
        this.version = version;
    }

    protected Database wrap(SQLiteDatabase db) {
        return new StandardDatabase(db);
    }

    public Database getWritableDb(){
        return wrap(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreate(wrap(db));
    }

    public void onCreate(Database db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(wrap(db), oldVersion, newVersion);
    }

    public void onUpgrade(Database db, int oldVersion, int newVersion) {

    }

    // TODO
    private class EncryptedHelper {

    }
}
