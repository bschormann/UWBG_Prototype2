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

import uw.edu.uwbg.helper.DownloadPDFTask;
import uw.edu.uwbg.model.CustomListVisitorInformation;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Displays the links associated with visitor information
 * 
 * @author 	Brett Schormann
 * @version 0.2 10/27/2014
 * 			0.3 11/3/2014
 * 			Miscellaneous edits. (BS)
 * 			0.4 11/17/2014
 * 			Changes to use linear layout at bottom of screen (BS)
 * 			0.5 11/21/2014
 * 			Completed code. (BS)
 * 
 * Uses adapter to set visitor information icons and text.
 * 
 */public class VisitorInformationActivity extends Activity {		
	 public static Context context;
	
	 ListView list;
	 // TODO Decide whether to delete this array or keep it.
	 	 
	 Integer[] leftIconId = {
			 R.drawable.hours,
			 R.drawable.call,
			 R.drawable.directions,
			 R.drawable.compass_colored,
			 R.drawable.restroom_large,
			 R.drawable.books
	 };

	 String[] text = {
			 "\nThe Washington Park Arboretum (Graham Visitor Center)\n" + 
					 "\t2300 Arboretum Drive E\n" +
					 "\tSeattle WA 98112\n",
			 "\n(206) 543-8616\n",
			 "\nDirections, Parking, Restrooms\n",
			 "Tours",
			 "\nThe Center for Urban Horticulture(Elizabeth C. Miller Library, Otis Douglas Hyde Herbarium)\n" +
					 "\t3501 NE 41st St\n" +
					 "\tSeattle, 98105\n",
	 } ;
	 
	 Integer[] rightIconId = {
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
			 R.drawable.greater_than_symbol,
	  };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_visitor_information);
		
		CustomListVisitorInformation adapter = new
				CustomListVisitorInformation(VisitorInformationActivity.this, leftIconId, text, rightIconId);
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(VisitorInformationActivity.this, "You clicked at " + text[position], Toast.LENGTH_SHORT).show();
                showWebsiteInformation(position);
            }
        });
	}
	
	/**
	 * Uses position to transfer to appropriate window in website or start phone call.
	 * @param position
	 */
	private void showWebsiteInformation(int position) {
		String urlString = null;
		switch (position) {
			case 0: {	// Washington Park Arboretum
				urlString = "http://depts.washington.edu/uwbg/visit_hours.shtml#hours";
				break;
			}
			case 1: {	// call
				// add PhoneStateListener
				PhoneCallListener phoneListener = new PhoneCallListener();
				TelephonyManager telephonyManager = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
				telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:2065438616"));
				startActivity(callIntent); 
				break;
			}
			case 2: {	// directions, parking, restrooms	
        		DownloadPDFTask downloadPDFTask = new DownloadPDFTask(context);
        		downloadPDFTask.execute("http://depts.washington.edu/uwbg/docs/TrailMap.pdf");
				break;
			}
			case 3: {	// tours	
				urlString = "http://depts.washington.edu/uwbg/visit/tours.shtml";				
				break;
			}
			case 4: {	// Center for Urban Horticulture
				urlString = "http://depts.washington.edu/uwbg/visit/cuh.php";
				break;
			}
			default: {
                Toast.makeText(VisitorInformationActivity.this, "An app error has occurred " + position, Toast.LENGTH_SHORT).show();
				break;
			}
		}
    	if (urlString != null)  {
    		SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	    	SharedPreferences.Editor editor = sharedPreferences.edit();
	    	editor.putString("url", urlString);
	    	editor.commit();
	    	Intent intent = new Intent(VisitorInformationActivity.this, WebsiteActivity.class);
	    	startActivity(intent);
	    	finish();
    	}
 	}
		
	/**
	 * 
	 * @author http://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
	 *
	 * Monitors phone call activities
	 */
	private class PhoneCallListener extends PhoneStateListener {
 
		private boolean isPhoneCalling = false;
 
		String LOG_TAG = "LOGGING Arboretum";
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
 
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");
 
				isPhoneCalling = true;
			}
 
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
 
				if (isPhoneCalling) {
 
					Log.i(LOG_TAG, "restart app");
 
					// restart app
//					Intent i = getBaseContext().getPackageManager()
//							.getLaunchIntentForPackage(getBaseContext().getPackageName());
//					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(i);
					finish();
 
					isPhoneCalling = false;
				} 
			}
		}
	}
}
