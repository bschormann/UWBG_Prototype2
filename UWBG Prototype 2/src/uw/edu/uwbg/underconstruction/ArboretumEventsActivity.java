package uw.edu.uwbg.underconstruction;

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

import uw.edu.uwbg.R;
import uw.edu.uwbg.helper.BackDoorHelper;
import uw.edu.uwbg.model.CustomListArboretumEvents;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
	// TODO Should link to web page and not be hardcoded.

/**
 * Should follow same pattern as in VisitorInformation activity
 * @author Brett Schormann
 * @version 0.2 10/27/2014
 * 			0.3 11/13/2014 Integrated Capstone project format. (BS)
 * @since 0.1
 */
public class ArboretumEventsActivity extends Activity {
	
	 ListView list;
	 
//	 Integer[] leftIconId = {
//			 R.drawable.hours,
//			 R.drawable.phone_booth,
//			 R.drawable.directions,
//			 R.drawable.parking_large,
//			 R.drawable.restroom_large,
//			 R.drawable.books			 
//	 };

	 Integer[] leftIconId = {
			 R.drawable.hours,
			 R.drawable.call,
			 R.drawable.compass_colored,
			 R.drawable.parking_large,
			 R.drawable.restroom_large,
			 R.drawable.books
	  };
	 
	 String[] text = {
			 "\nThe Washington Park Arboretum is free and open to the public daily from dawn to dusk.\n\n" +
					 "The Graham Visitor Center and public restrooms are open 9am - 5pm.\n",
			 "(206) 543-8800\n\n",
			 "Graham Visitor Center\n" +
					 "\t2300 Arboretum Drive E\n" +
					 "\tSeattle WA 98112\n",
			 "Find Parking\n",
			 "Park Restrooms\n",
			 "Center for Urban Horticulture\n" +
					 "\t3501 NE 41st St\n" +
					 "\tSeattle, 98105\n",
	 } ;
	 
//	 Integer[] rightIconId = {
//			 R.drawable.hours,
//			 R.drawable.call,
//			 R.drawable.compass_colored,
//			 R.drawable.parking_large,
//			 R.drawable.restroom_large,
//			 R.drawable.books
//	  };

	 Integer[] rightIconId = {
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol
	  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (bdh.isUseCapstoneProject()) {
    		setContentView(R.layout.activity_arboretum_events_capstone);
    	} else {
    		setContentView(R.layout.activity_arboretum_events);
    	}
		
		CustomListArboretumEvents adapter = new
				CustomListArboretumEvents(ArboretumEventsActivity.this, leftIconId, text, rightIconId);
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(ArboretumEventsActivity.this, "You clicked at " + text[position], Toast.LENGTH_SHORT).show();
           }
       });
	}
}
