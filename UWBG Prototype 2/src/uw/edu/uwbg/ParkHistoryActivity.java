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

import uw.edu.uwbg.helper.CustomFont;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays the park hisitory
 * 
 * @author Brett Schormann
 * @version 0.2 10/27/2014
 * 			0.3 10/31/2014
 * 			Used CustomFont to set up text. (BS)
 * 			0.4 11/18/2014
 * 			Changes to use linear layout at bottom of screen (BS)
 * @since 0.1
 */
public class ParkHistoryActivity extends Activity {	
	// TODO Take out link
	private final String htmlText = 
			"<p>The <strong>Washington Park Arboretum</strong> has had a glorious eighty-year " +
			"history as one of the most loved educational and cultural resources in the Pacific Northwest. " +
			"The <strong>Arboretum</strong>, then known as the <strong>University of Washington " +
			"Arboretum</strong>, began " +
			"officially in 1934 when an agreement was signed by the University of Washington " +
			"and the City of Seattle, allowing the University to develop and manage an " + 
			"arboretum and botanical garden in Washington Park. This agreement continues " +
			"to serve the park's and arboretum's mission of service to the people of the " +
			"Northwest, through education, conservation, and recreation.</p>" +
			
			"<p>The <strong>Washington Park Arboretum</strong> is a living plant museum emphasizing trees and " +
			"shrubs hardy in the maritime Pacific Northwest. Plant collections are selected " +
			"and arranged to display beauty and function in urban landscapes, to demonstrate " +
			"their natural ecology and diversity, and to conserve important species and cultivated " +
			"varieties for the future. The <strong>Arboretum</strong> serves the public, students at " +
			"all levels, naturalists, gardeners, and nursery and landscape professionals with " +
			"its collections, educational programs, interpretation, and recreational opportunities.</p>." +
			
			"<p>(To Be completed) <strong><i>Looking Back: A History of the Washington Park " +
			"Arboretum<i><strong> (http://depts.washington.edu/hortlib/wpa_history/intro.shtml).</p>";
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park_history);
        TextView htmlTextView = (TextView)findViewById(R.id.park_history);
		
        CustomFont customFont = CustomFont.getCustomFont();
		customFont.setTextViewParameters(this, htmlTextView, "OpenSans-Regular", "MEDIUM_TEXT_SIZE", htmlText, true);	
		
    	Button btn = (Button) this.findViewById(R.id.trails);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) this.findViewById(R.id.plant_lookup);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
       	btn = (Button) this.findViewById(R.id.map);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) this.findViewById(R.id.bookmarks);
    	customFont.setButtonParameters(this, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
	}
}
