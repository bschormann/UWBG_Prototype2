package uw.edu.uwbg.sheymafragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import uw.edu.uwbg.R;
import uw.edu.uwbg.helper.PlantListAdapter;
import uw.edu.uwbg.model.Plant;

public class PlantListFragment extends Fragment{
	
	String query;
	ArrayList<Plant> plants;
	TextView plantcount;
	ListView lv;
	PlantListAdapter pladapter;

	public PlantListFragment(String q) {
		super();
		query=q;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		plants=new ArrayList<Plant>();
		return inflater.inflate(R.layout.plantlist_layout, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		plantcount=(TextView) getView().findViewById(R.id.numberofplants);
		//if(savedInstanceState!=null && savedInstanceState.getParcelableArrayList("plants")!=null);
		 lv=(ListView) getView().findViewById(R.id.plant_list);
		 
		 lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getFragmentManager().beginTransaction().
				replace(R.id.fragmentcontainer, new PlantFullFragment(plants.get(position))).commit();
				
			}
		});
		
		SearchPlantsAsyncTask spa=new SearchPlantsAsyncTask();
		spa.execute(query);
		
	}
	
	class SearchPlantsAsyncTask extends AsyncTask<String, Void, FeatureResult>{
        ProgressDialog prog;
		@Override
		protected FeatureResult doInBackground(String... params) {
			Log.i("test","getting feature from search query");
			
			if(!isNetworkAvailable()){
				Toast.makeText(getActivity(), "Please connect your device to the Internet", Toast.LENGTH_LONG).show();
				return null;
			}
			
		/*	String whereSt="Plants.Name like '%"+params[0]+" %' or BGBaseData.Family like '%"+params[0]+" %' or " +
					"BGBaseData.FamilyCommonName like '%"+params[0]+" %'";*/
			String wordU=new String(params[0].charAt(0)+"").toUpperCase()+params[0].substring(1);
			String wordL=new String(params[0].charAt(0)+"").toLowerCase()+params[0].substring(1);
			
			String whereSt="Plants.Name like '% "+wordL+"%' or Plants.Name like '"+wordU+"%'"+" or" +
					" BGBaseData.Family like '% "+wordL+"%' or BGBaseData.Family like '"+wordU+"%' or " +
					"BGBaseData.FamilyCommonName like '% "+wordL+"%' or BGBaseData.FamilyCommonName like '"+
					wordU+"%'"+" or " +"BGBaseData.CommonName like '% "+wordL+"%' or " +
							"BGBaseData.CommonName like '"+wordU+"%'";

			//String whereSt="Plants.Name= 'Platanus x acerifolia'";
			// Define a new query and set parameters
		      QueryParameters mParams = new QueryParameters();
		      mParams.setWhere(whereSt);
		      mParams.setReturnGeometry(true);
		      mParams.setOutFields(new String[]{"*"});
		      // Define the new instance of QueryTask
		      QueryTask qTask = new QueryTask("http://uwbgmaps.cfr.washington.edu/arcgis/rest/services" +
		      		"/PublicFeatures/MapServer/3");
		      FeatureResult results;

		      try {
		        // run the querytask
		        results = qTask.execute(mParams);
		        return results;
		      } catch (Exception e) {
		    	  
		        e.printStackTrace();
		        prog.dismiss();
		        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
		      }
		      return null;

		}//doInBackground

		@Override
		protected void onPreExecute() {
			prog=ProgressDialog.show(getActivity(), "", "Please Wait");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(FeatureResult result) {
		
			 prog.dismiss();
			 plants=new ArrayList<Plant>();
             
              if(result == null) {
            	  Log.i("test","result null");
            	  return;
              }
             // plants=new ArrayList<Plant>();
		      // iterate through results
		      for (Object element : result) {
		        // if object is feature cast to feature
		        if (element instanceof Feature) {
		        	
		          Feature feature = (Feature) element;
		          Plant plant=new Plant();
		          plant.setAttributesmap(feature.getAttributes(),feature.getGeometry());
		          //plant.setGeometry(feature.getGeometry());
		          plants.add(plant);
		        }
		        
		      }
		     if(plants.size()>0) Log.i("test+++",plants.get(0).commonName+"");
		      plantcount.setText(""+plants.size());
		      plantcount.invalidate();
		      pladapter=new PlantListAdapter(getActivity(),android.R.layout.simple_list_item_1,plants,false);
		      lv.setAdapter(pladapter);
		      
		      //pladapter.notifyDataSetChanged();
		      //super.onPostExecute(result);
		}
		
		
		
	}
	
	 public boolean isNetworkAvailable() {
	        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	        // if no network is available networkInfo will be null
	        // otherwise check if we are connected
	        if (networkInfo != null && networkInfo.isConnected()) {
	            return true;
	        }
	        return false;
	    }

}
