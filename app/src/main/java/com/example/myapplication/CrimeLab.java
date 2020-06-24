package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.database.CrimeBaseHelper;
import com.example.myapplication.database.CrimeCursorWrapper;
import com.example.myapplication.database.CrimeDbSchema;
import com.example.myapplication.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private  static  CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;



    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private  CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes = new ArrayList<>();

    }

    public void addCrime(Crime crime){
        ContentValues contentValues = getcontentvalues(crime);
        //adding rows
        mDatabase.insert(CrimeTable.NAME,null,contentValues);
    }

    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getcrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?",new String[]{id.toString()});
        try {
            if(crimeCursorWrapper.getCount()==0){
                return null;
            }

            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getcrime();

        }finally {
            crimeCursorWrapper.close();
        }
    }

    public void updateCrime(Crime crime){
        String UUIDstring = crime.getId().toString();
        ContentValues contentValues = getcontentvalues(crime);

        mDatabase.update(CrimeTable.NAME,contentValues,CrimeTable.Cols.UUID+" = ?",new String[]{UUIDstring});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,//null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }

    private static ContentValues getcontentvalues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID,crime.getId().toString());
        contentValues.put(CrimeTable.Cols.DATE,crime.getDate().toString());
        contentValues.put(CrimeTable.Cols.TITLE,crime.getTitle());
        contentValues.put(CrimeTable.Cols.SOLVED,crime.isSolved());
        return contentValues;
    }

}


