package uw.edu.uwbg.sheymafragments;

import uw.edu.uwbg.NearbyActivity;
import uw.edu.uwbg.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class PlantLookUpFragment extends Fragment{

   View activityContent;
	public PlantLookUpFragment(View activityContent) {
		super();
		this.activityContent=activityContent;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.plantlookup_layout, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		SearchView searchbox=(SearchView) getView().findViewById(R.id.searchBox);
		searchbox.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String query) {
				
				getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,
						new PlantListFragment(query)).commit();
				//getFragmentManager().executePendingTransactions();
				return true;
			}
			
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		Button nearbyButton=(Button) getView().findViewById(R.id.nearbyButton);
		nearbyButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),NearbyActivity.class));
				
			}
		});
	}
	
	
	
	
	

}
