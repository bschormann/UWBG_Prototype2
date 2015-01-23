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
import uw.edu.uwbg.helper.CustomFont;
import uw.edu.uwbg.helper.DownloadPDFTask;
import uw.edu.uwbg.sheymafragments.BookMarkListFragment;
import uw.edu.uwbg.sheymafragments.PlantLookUpFragment;
import uw.edu.uwbg.underconstruction.ArboretumEventsActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Invoked as the first activity after the splash screen.
 * Contains interface between buttons on the home screen
 * and the rest of the activities in the app.
 * 
 * @author 	Brett Schormann
 * @version 0.3 10/29/2014
 * 			Miscellaneous changes to support integration of Sheyma's code (BS)
 * 			0.4 10/30/2014
 * 			Changes to support integration of DownloadPDFTask (BS)
 * 			0.5 10/31/2014
 * 			Changes to support integration of FeaturedGardens (BS)
 * 			0.6 11/12/2014
 * 			Changed so that Capstone project format is used. (BS)
 * 			0.7 11/24/2014
 * 			Added some images from Tracy
 */
public class MainActivity extends Activity {
	
	private String TAG = MainActivity.this.getClass().getSimpleName();
	
	private final String[] mImages = {	
			"acer_palmatum_jeter.jpg", 												
			"arboretum15",										
			"autumngrape_howard",				
			//"clematis_viticella.jpg",				// bad	
			//"disanthus_cercidifolius_jeter.jpg",	// bad
			//"fothergilla_major_jeter.jpg",		// bad
			//"franklinia_alatamaha_jeter.jpg",		// bad
			//"gvc_east_patio_entry.jpg",			// bad							
			//"hydrangea_heteromalla.jpg",			// bad					
			"hydrangea_macrophylla_jeter",	
			"kidsfishing",												
			"maplemalk_jeter",									
			"pacconshelter_wott", 
			//"rhododendron_auriculatum_8_10_06_lookoutpond_wpa_sj.jpg",	// bad 	
			//"rhododendron_schlippenbachii_wpa_10_06_sj.jpg",				// bad
			//"s0484054.jpg",						// bad
			"sorbus_commixta_embley_jeter",
			"srija_chattapadyay2", 		
			"ubna_crabapple_howard", 							
			//"winter_garden1.jpg"					// bad
			};

	private final String WELCOME = "Welcome to the ...";
	private final String WPA = " ...Washington Park Arboretum";
	
	// TODO
	boolean home_flag=true;	//????
	PlantLookUpFragment plantlookupfrag;
	BookMarkListFragment bookmarkfrag;
	
	public static Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (bdh.isUseCapstoneProject()) {
    		setContentView(R.layout.activity_main_capstone);
    	} else {
    		setContentView(R.layout.activity_main);
    	}
        
        context = this;

        plantlookupfrag=new PlantLookUpFragment(findViewById(android.R.id.content));
 		bookmarkfrag=new BookMarkListFragment(); 
    }	
	
    /**
     * 
     */
    @Override
    protected void onResume() {
    	super.onResume();
 		//adjustText(plantlookupfrag);
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (bdh.isUseCapstoneProject()) {
    		setContentView(R.layout.activity_main_capstone);
    	} else {
    		setContentView(R.layout.activity_main);
    	}
        setupButtons();
        adjustTextOnHomeScreen();
        setupButtonListeners();       
        
    	animate((ImageView)findViewById(R.id.HomepageImageView), mImages, 0, true);
    }
    
    /**
     * 
     */
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    }
    
    /**
     * A recursive algorithm to smooth transitions between images.
     * 
     * @author Brett Schormann
     * @version 0.2 10/27/2014
     * 
     * @see <a href="http://stackoverflow.com/questions/17587152/how-to-do-smooth-transition-from-one-image-to-another">http://stackoverflow.com/questions/17587152/how-to-do-smooth-transition-from-one-image-to-another></a>
     * @param imageView		The View which displays the images
     * @param images		A string array of image names to display
     * @param imageIndex	Index of the first image to show in images[]
     * @param forever		If equals true then after the last image it starts all over again with the 
     * 						first image resulting in an infinite loop. You have been warned.
     */
	private void animate(final ImageView imageView, final String images[], final int imageIndex, final boolean forever) {

    	int fadeInDuration = 500; // Configure time values here
    	int timeBetween = 4000;
    	int fadeOutDuration = 1000;

    	imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
	
    	// translates the file name to resource Id
		int id = getResources().getIdentifier(images[imageIndex], "drawable", MainActivity.this.getPackageName());
		imageView.setImageResource(id);
		
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
		fadeIn.setDuration(fadeInDuration);
	
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setStartOffset(fadeInDuration + timeBetween);
		fadeOut.setDuration(fadeOutDuration);

		AnimationSet animation = new AnimationSet(false); // change to false
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);
		animation.setRepeatCount(1);
		imageView.setAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				if (images.length - 1 > imageIndex) {
					animate(imageView, images, imageIndex + 1, forever); //Calls itself until it gets to the end of the array
				}
				else if (forever == true){
						animate(imageView, images, 0, forever);  //Calls itself to start the animation all over again in a loop if forever = true
				}
			}
			
			public void onAnimationRepeat(Animation animation) {
			    // TODO Auto-generated method stub
			}
			public void onAnimationStart(Animation animation) {
			    // TODO Auto-generated method stub
		        }
		    });
		}
        
    /**
     * Uses the typeface and text sizes in CustomFont.java
     * to set up buttons.
     * 
     * @author Brett Schormann
     * @version 0.1 10/2014
     */
    private void setupButtons() {
    	CustomFont customFont = CustomFont.getCustomFont();
    	Button btn = null;
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (!bdh.isUseCapstoneProject()) {
    		btn = (Button) findViewById(R.id.about);
	    	customFont.setButtonParameters(this, btn, "OpenSans-BoldItalic", "MEDIUM_TEXT_SIZE"); 
	    	btn = (Button) findViewById(R.id.www);
	    	customFont.setButtonParameters(this, btn, "OpenSans-BoldItalic", "MEDIUM_TEXT_SIZE"); 
	    	btn = (Button) findViewById(R.id.help);
	    	customFont.setButtonParameters(this, btn, "OpenSans-BoldItalic", "MEDIUM_TEXT_SIZE"); 
	    	btn = (Button) findViewById(R.id.provide_feedback);
	    	customFont.setButtonParameters(this, btn, "OpenSans-BoldItalic", "MEDIUM_TEXT_SIZE"); 
    	}
    	btn = (Button) findViewById(R.id.featured_gardens);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "LARGE_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.visitor_information);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "LARGE_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.arboretum_events);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "LARGE_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.park_history);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "LARGE_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.trails);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.plant_lookup);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
       	btn = (Button) findViewById(R.id.map);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) findViewById(R.id.bookmarks);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
     }

    /**
     * Adjusts the font and alignment on the home screen text fields
     * That is: Welcome to the ... Washington Park Arboretum
     *     
     * @author 	Brett Schormann
     * @version 0.1 10/2014
     * 			0.2 11/13/2014 Changed to inclue Capstone project format. (BS)
     * 			0.3 11/16/2014 Changed font to OpenSans-BoldItalic from OpenSans-LightItalic
     */
    private void adjustTextOnHomeScreen() {
    	
    	CustomFont customFont = CustomFont.getCustomFont();
    	
    	// Welcome text not in Capstone project
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (!bdh.isUseCapstoneProject()) {
    		TextView  tv = (TextView) findViewById(R.id.text_welcome);
    		customFont.setTextViewParameters(this, tv, "OpenSans-BoldItalic", "GIGANTIC_TEXT_SIZE", WELCOME, false);
    	
    		tv = (TextView) findViewById(R.id.text_tothe);
    		customFont.setTextViewParameters(this, tv, "OpenSans-BoldItalic", "GIGANTIC_TEXT_SIZE", WPA, false);
    		tv.setGravity(Gravity.RIGHT);
    	}
    }

    /**
     * Adds listeners to the buttons
     *     
     * @author 	Brett Schormann
     * @version 0.2 10/28/2014
     * 			Completed adding listeners to buttons.
     * 			0.3 10/30/2014
     * 			Added code to use DownloadPDFTask (BS)
     * 			0.4 11/16/2014
     * 			Changed backdoor from About to Visitor Information
     */
    private void setupButtonListeners() {
    	Button button = null;
    	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
    	if (!bdh.isUseCapstoneProject()) {
	        // About
	    	button = (Button) findViewById(R.id.about);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent(MainActivity.this, AboutActivity.class);
	                startActivity(intent);
	            }
	        });
	        
	        // Website
	        button = (Button) findViewById(R.id.www);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	// this activity will be used by other activities
	            	String urlString = "http://depts.washington.edu/uwbg/index.php";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	                startActivity(intent);                
	            }
	        });
	
	        // Help
	        button = (Button) findViewById(R.id.help);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
	            	if (bdh.isUseProductionHelp()) {
		            	Intent intent = new Intent(MainActivity.this, HelpActivity.class);
		                startActivity(intent); 
	            	} else {
		            	Intent intent = new Intent(MainActivity.this, HelpActivity.class);
		                startActivity(intent); 
	            	}
	            }
	        });

	        // Feedback
	        button = (Button) findViewById(R.id.provide_feedback);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                sendEmail();
	            }
	        });
    	}

        // Featured Gardens
        button = (Button) findViewById(R.id.featured_gardens);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionFeaturedGardens()) {
        	    	Intent intent=new Intent(MainActivity.this, FeaturedGardensActivity.class);
        	    	startActivity(intent);
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/gardens.shtml";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);
            	}
            }
        });

        // Visitor Information
        button = (Button) findViewById(R.id.visitor_information);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionFeaturedGardens()) {
            		Intent intent = new Intent(MainActivity.this, VisitorInformationActivity.class);
            		startActivity(intent);  
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/visit_hours.shtml";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);                	                		
            	}
            }
        });
 
        button.setOnLongClickListener(new View.OnLongClickListener() { 
            public boolean onLongClick(View v) {
            	Intent intent = new Intent(MainActivity.this, BackDoorHelperActivity.class);
                startActivity(intent);                
                return true;
            }
        });

       // Arboretum Events
        button = (Button) findViewById(R.id.arboretum_events);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionArboretumEvents()) {
            		Intent intent = new Intent(MainActivity.this, ArboretumEventsActivity.class);
        	    	startActivity(intent);
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/visit/calendar.shtml";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);
            	}
            }      
        });

        // Park History
        button = (Button) findViewById(R.id.park_history);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MainActivity.this, ParkHistoryActivity.class);
                startActivity(intent);                
            }
        });

        // Trails
        button = (Button) findViewById(R.id.trails);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionTrails()) {
            		DownloadPDFTask downloadPDFTask = new DownloadPDFTask(context);
            		downloadPDFTask.execute("http://depts.washington.edu/uwbg/docs/TrailMap.pdf");
            		
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/docs/TrailMap.pdf";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);
            	}
            }      
        });

        // Plant Lookup
        button = (Button) findViewById(R.id.plant_lookup);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionPlantLookup()) {
            		/*setContentView(R.layout.activity_first_);
	    	    	getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, plantlookupfrag).
	    	    	commit();*/
	    	    	Intent i=new Intent(MainActivity.this,FragmentActivity.class);
	            	i.putExtra("code", 2);
	            	startActivity(i);
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/gardens/map.shtml";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);            		
            	}
            }
        });

        // Map
        button = (Button) findViewById(R.id.map);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	BackDoorHelper bdh = BackDoorHelper.getBackDoorHelper();
            	if (bdh.isUseProductionMap()) {
        	    	Intent intent = new Intent(MainActivity.this, UWArboretumActivity.class);
        	    	startActivity(intent);
            	} else {
            		String urlString = "http://depts.washington.edu/uwbg/gardens/map.shtml";
	            	SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
	            	SharedPreferences.Editor editor = sharedPreferences.edit();
	            	editor.putString("url", urlString);
	            	editor.commit();
	            	Intent intent = new Intent(MainActivity.this, WebsiteActivity.class);
	            	startActivity(intent);
            	}
            }
        });

        // Bookmarks
        button = (Button) findViewById(R.id.bookmarks);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		/*setContentView(R.layout.activity_first_);
    	    	getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, bookmarkfrag).
    	    	commit();*/
            	Intent i=new Intent(MainActivity.this,FragmentActivity.class);
            	i.putExtra("code", 1);
            	startActivity(i);
            }
        });
    }
    
    /**
     * Helper method to setup and send email
     *     
     * @author 	Brett Schormann
     * @version 0.2 10/28/2014
     * 			Added email addresses. (BS)
     */
    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, 		new String[]{"tmehlin@uw.edu"});
        i.putExtra(Intent.EXTRA_CC, 		new String[]{
        		"bschormann@jbschormann.com",
        		"bschormann@comcast.net",
        		"sheyma61@yahoo.com",
        		"davidc23@uw.edu"
        		});
        i.putExtra(Intent.EXTRA_SUBJECT, 	"Feedback from Botanical Garden App User");
        i.putExtra(Intent.EXTRA_TEXT   , 	"Please add your messge below. Thank you.\n");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
