package uw.edu.uwbg.model;

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
import uw.edu.uwbg.helper.CustomFont;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Used to create ListView for VisitorInformation
 * @author Brett Schormann
 * @version 0.1
 * 			0.2 11/17/2014
 * 			Changes to use linear layout at bottom of screen (BS)
 * 			0.3 11/18/2014
 * 			Changes to icons so that the screen layout follows Capstone wire raps. (BS)
 */
public class CustomListVisitorInformation  extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] text;
	private final Integer[] leftIconId;
	private final Integer[] rightIconId;
	private CustomFont customFont;
	
	/**
	 * 
	 * @param context
	 * @param leftIconId
	 * @param text
	 * @param rightIconId
	 */
	public CustomListVisitorInformation(Activity context, Integer[] leftIconId, String[] text, Integer[] rightIconId) {
		super(context, R.layout.list_single_row_visitor_information, text);
		this.context = context;
		this.leftIconId = leftIconId;
		this.text = text;
		this.rightIconId = rightIconId;
		this.customFont = CustomFont.getCustomFont();
	}
	
	/**
	 * Formats a single row of the list view
	 * @param 	position
	 * @param 	view
	 * @param 	parent
	 * @return rowView = row of the list view denoted by position
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.list_single_row_visitor_information, null, true);

		ImageView imageView = (ImageView) rowView.findViewById(R.id.lefticonimg);
		imageView.setImageResource(leftIconId[position]);

		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		customFont.setTextViewParameters(context, txtTitle, "OpenSans-BoldItalic", "MEDIUM_TEXT_SIZE", text[position], false);
		
		imageView = (ImageView) rowView.findViewById(R.id.righticonimg);
		imageView.setImageResource(rightIconId[position]);

		Button btn = (Button) context.findViewById(R.id.trails);
    	customFont.setButtonParameters(context, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) context.findViewById(R.id.plant_lookup);
    	customFont.setButtonParameters(context, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
       	btn = (Button) context.findViewById(R.id.map);
    	customFont.setButtonParameters(context, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 
    	btn = (Button) context.findViewById(R.id.bookmarks);
    	customFont.setButtonParameters(context, btn, "OpenSans-ExtraBoldItalic", "MEDIUM_TEXT_SIZE"); 

		return rowView;
	}	
}


