package uw.edu.uwbg.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import uw.edu.uwbg.model.Plant;

public class AddBookmarkAsync extends AsyncTask<Plant, Void, Void>{
	Context mContext;

	public AddBookmarkAsync(Context c) {
		super();
		mContext=c;
	}
	
	@Override
	protected Void doInBackground(Plant... params) {
		if(!isNetworkAvailable()){
			
			Void v = null;
			return v;
		}
		SQliteHelper sqlh=new SQliteHelper(mContext, "", null, 4);
		SQLiteDatabase db=sqlh.getWritableDatabase();
		synchronized(db){
			Cursor c=db.rawQuery("select name from bookmarkplants where accession='"+params[0].UWBGAccession+"'", null);
			
			if(c.moveToFirst()) return null; //already in favorite list
			
			ContentValues cv=new ContentValues();
			cv.put("name", params[0].plantName);
			cv.put("accession", params[0].UWBGAccession);
			cv.put("family", params[0].family);
			cv.put("familycommon", params[0].familyCommonName);
			cv.put("specificepithet", params[0].epithet);
			cv.put("genus", params[0].genus);
			cv.put("sourcecity", params[0].source);
			cv.put("commonname", params[0].commonName);
			cv.put("condition", params[0].lastReportedCondition);
			cv.put("grid", params[0].mapGrid);
			cv.put("scientificname", params[0].scientificName);
			if(params[0].geom!=null){
			cv.put("x", params[0].geom.getX());
			cv.put("y", params[0].geom.getY());
			}
			Log.i("test","added a plant " +db.insert("bookmarkplants", null, cv));
			c.close();
			db.close();
		}
		sqlh.close();
		return null;
	}
	
	 public boolean isNetworkAvailable() {
	        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	        // if no network is available networkInfo will be null
	        // otherwise check if we are connected
	        if (networkInfo != null && networkInfo.isConnected()) {
	            return true;
	        }
	        return false;
	    }

}
