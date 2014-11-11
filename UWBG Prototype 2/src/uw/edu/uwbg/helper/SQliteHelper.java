package uw.edu.uwbg.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * @author Shima Akhavanfarid
 *
 */
public class SQliteHelper extends SQLiteOpenHelper{
	Context mContext;

	public SQliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, "UWArboretumDB", factory, version);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table bookmarkplants(accession varchar(50),name varchar(50),family varchar(50)," +
				" familycommon varchar(50), specificepithet varchar(50),genus varchar(50), " +
				"condition varchar(50), grid varchar(50), scientificname varchar(50), sourcecity varchar(50), commonname varchar(50), x double, y double)");

		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS bookmarkplants");
        onCreate(db);
		
	}	

}
