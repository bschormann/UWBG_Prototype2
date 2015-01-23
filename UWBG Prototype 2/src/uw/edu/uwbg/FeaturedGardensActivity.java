package uw.edu.uwbg;

/**
Copyright � <2014> <University of Washington>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import uw.edu.uwbg.model.CustomListFeaturedGardens;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * 			0.3 11/22/14
 * 			Modified to include Tracy's direction (BS)
 * 
 * Uses adapter to set garden images and text.
 */
public class FeaturedGardensActivity extends Activity {
	
	GridView list;
	
	String[] titles, urls;
	// TODO Images need to be replaced by images from appropriate garden
	Integer[] images = {R.drawable.pacific_connections,
						R.drawable.wittwinter_howard,
						R.drawable.woodland,
						R.drawable.shoreline_welty,
						R.drawable.japanese_garden,
						};				

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_featured_gardens);
		
		Resources res = getResources();
		titles = res.getStringArray(R.array.featured_gardens_titles);
		urls = res.getStringArray(R.array.featured_gardens_urls);

		CustomListFeaturedGardens adapter = new CustomListFeaturedGardens(FeaturedGardensActivity.this, images, titles);
		
		list = (GridView) findViewById(R.id.gridView);		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(FeaturedGardensActivity.this, "You clicked at " + titles[position], Toast.LENGTH_SHORT).show();
				String urlString = urls[position];
	       	   	if (urlString != null)  {
	    	   		SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
    		    	SharedPreferences.Editor editor = sharedPreferences.edit();
    		    	editor.putString("url", urlString);
    		    	editor.commit();
    		    	Intent intent = new Intent(FeaturedGardensActivity.this, WebsiteActivity.class);
    		    	startActivity(intent);
    		    	finish();
	    	   	}
           }
       });
	}
}
