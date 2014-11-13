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

import uw.edu.uwbg.helper.BackDoorHelper;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Invoked from long press on About button.
 * 
 * @author Brett Schormann
 * @version 0.1 10/24/2014
 * 			0.2 11/12/2014
 * 			Changed so that Capstone project format is used. (BS)
 * @since 0.2
 */
public class BackDoorHelperActivity extends Activity {
	// TODO Use array adapter
	private String TAG = BackDoorHelperActivity.this.getClass().getSimpleName();

	private CheckBox map, help, visitorInformation, featuredGardens, arboretumEvents, trails, plantLookup, 
					 bookmarks, parkHistory, capstoneProject;
    private Button saveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_door_helper);
		
		// TODO persist values
		
		map 				= (CheckBox) findViewById(R.id.map);
        help      			= (CheckBox) findViewById(R.id.help);
        visitorInformation  = (CheckBox) findViewById(R.id.visitor_information);
        featuredGardens 	= (CheckBox) findViewById(R.id.featured_gardens); 
        arboretumEvents  	= (CheckBox) findViewById(R.id.arboretum_events); 
        trails				= (CheckBox) findViewById(R.id.trails);
        plantLookup			= (CheckBox) findViewById(R.id.plant_lookup);
        bookmarks			= (CheckBox) findViewById(R.id.bookmarks);
        parkHistory			= (CheckBox) findViewById(R.id.park_history);
        capstoneProject		= (CheckBox) findViewById(R.id.use_capstone_format);
        
		// get date from BackDoorHelper singleton
		final BackDoorHelper backDoorHelper = BackDoorHelper.getBackDoorHelper();
		
		// set data into check boxes
		map.setChecked(backDoorHelper.isUseProductionMap());
		help.setChecked(backDoorHelper.isUseProductionHelp());
		visitorInformation.setChecked(backDoorHelper.isUseProductionVisitorInformation());
		featuredGardens.setChecked(backDoorHelper.isUseProductionFeaturedGardens());
		arboretumEvents.setChecked(backDoorHelper.isUseProductionArboretumEvents());
		trails.setChecked(backDoorHelper.isUseProductionTrails());
		plantLookup.setChecked(backDoorHelper.isUseProductionPlantLookup());
		bookmarks.setChecked(backDoorHelper.isUseProductionBookmarks());
		parkHistory.setChecked(backDoorHelper.isUseProductionParkHistory());
		capstoneProject.setChecked(backDoorHelper.isUseCapstoneProject());

        saveButton = (Button) findViewById(R.id.Save);;
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set values in singleton to new state
                backDoorHelper.setUseProductionMap(map.isChecked());
                backDoorHelper.setUseProductionHelp(help.isChecked());
                backDoorHelper.setUseProductionVisitorInformation(visitorInformation.isChecked());
                backDoorHelper.setUseProductionFeaturedGardens(featuredGardens.isChecked());
                backDoorHelper.setUseProductionArboretumEvents(arboretumEvents.isChecked());
                backDoorHelper.setUseProductionTrails(trails.isChecked());
                backDoorHelper.setUseProductionPlantLookup(plantLookup.isChecked());
                backDoorHelper.setUseProductionBookmarks(bookmarks.isChecked());
                backDoorHelper.setUseProductionParkHistory(parkHistory.isChecked());
                backDoorHelper.setUseCapstoneProject(capstoneProject.isChecked());
                
                Context context = getApplicationContext();
                CharSequence text = "BackDoor data saved!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                }
        });
    }
}
         
         
