package uw.edu.uwbg.sheymafragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.esri.core.geometry.Point;

import uw.edu.uwbg.R;
import uw.edu.uwbg.helper.PlantListAdapter;
import uw.edu.uwbg.helper.SQliteHelper;
import uw.edu.uwbg.model.Plant;


/**
 * @author Shima Akhavanfarid
 *
 */
public class BookMarkListFragment extends Fragment{
    ListView lv;
    ArrayList<Plant> plants=new ArrayList();
    PlantListAdapter adapter;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.bookmark_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lv=(ListView) getView().findViewById(R.id.bookmark_list);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Log.i("test","just clicked "+plants.size());
				getFragmentManager().beginTransaction().
				replace(R.id.fragmentcontainer, new PlantFullFragment(plants.get(position))).commit();
			}
		});
		Button addnewbookmarks= (Button) getView().findViewById(R.id.add_bookmark_new);
		addnewbookmarks.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				getFragmentManager().beginTransaction().
				replace(R.id.fragmentcontainer, new PlantLookUpFragment(null)).commit();
				
			}
		});
		
		
	}
	/*"create table bookmarkplants(accession varchar(50),name varchar(50),family varchar(50)," +
	" familycommon varchar(50), specificepithet varchar(50),genus varchar(50), " +
	"condition varchar(50), grid varchar(50), scientificname varchar(50), sourcecity varchar(50), commonname varchar(50))");
*/
	
	class GetBookMarksAsyncTask extends AsyncTask<Void, Void, Plant[]>{

		@Override
		protected Plant[] doInBackground(Void... params) {
			plants=new ArrayList<Plant>();
			SQliteHelper sqlh=new SQliteHelper(getActivity(),"",null,4);
			Cursor c;
			synchronized (sqlh) {
				SQLiteDatabase sq=sqlh.getReadableDatabase();
				 c=sq.rawQuery("select * from bookmarkplants", null);
				 
				 while(c.moveToNext()){
					 Plant p=new Plant();
					 p.UWBGAccession=c.getString(0);
					 p.plantName=c.getString(1);
					 p.family=c.getString(2);
					 p.familyCommonName=c.getString(3);
					 p.epithet=c.getString(4);
					 p.genus=c.getString(5);
					 p.lastReportedCondition=c.getString(6);
					 p.mapGrid=c.getString(7);
					 p.scientificName=c.getString(8);
					 p.source=c.getString(9);
					 p.commonName=c.getString(10);
					 p.geom=new Point(c.getDouble(11),c.getDouble(12));
					 
					 plants.add(p);
				 }
				// Log.i("test","size: "+plants.size()+" "+c.getCount());
				 c.close();
				 sq.close();				 
				sqlh.close();
				
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Plant[] result) {
			adapter=new PlantListAdapter(getActivity(), android.R.layout.simple_list_item_1, plants, true);
			lv.setAdapter(adapter);
			super.onPostExecute(result);
		}
		
		
	}

	@Override
	public void onResume() {
		
		GetBookMarksAsyncTask gba=new GetBookMarksAsyncTask();
		gba.execute();
		super.onResume();
	}
	
	

}
