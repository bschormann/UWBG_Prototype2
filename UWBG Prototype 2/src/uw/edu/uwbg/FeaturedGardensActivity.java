package uw.edu.uwbg;

import uw.edu.uwbg.model.CustomListFeaturedGardens;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Used to create GridView for FeaturedGardens
 * @author 	Brett Schormann
 * @version 0.1 10/31/2014
 * 			0.2 11/3/2014
 * 			Miscellaneous edits. (BS)
 * 
 * Uses adapter to set garden images and text.
 */
public class FeaturedGardensActivity extends Activity {
	
	GridView list;
	
	String[] titles;
	// TODO Images need to be replaced by images from appropriate garden
	Integer[] images = {R.drawable.a0soest_signage,
						R.drawable.a1fgentry,
						R.drawable.a2johnwottphotos026,
						R.drawable.a3wpacouplewalking_loudon,
						R.drawable.a4ubnatrail,
						R.drawable.a5augustthirtyfirst_47_300x200,
						R.drawable.a6pacconsign_l_thornberg,
						R.drawable.a7soest6_howard,
						R.drawable.a8arboretumwetlands_welty,
						R.drawable.a9arboretum15 };				

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_featured_gardens);
		
		Resources res = getResources();
		titles = res.getStringArray(R.array.featured_gardens_titles);

		CustomListFeaturedGardens adapter = new CustomListFeaturedGardens(FeaturedGardensActivity.this, images, titles);
		
		list = (GridView) findViewById(R.id.gridView);		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            Toast.makeText(FeaturedGardensActivity.this, "You clicked at " + titles[position], Toast.LENGTH_SHORT).show();
	        }
	    });
	}
}
