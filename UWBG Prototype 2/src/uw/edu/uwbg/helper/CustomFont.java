package uw.edu.uwbg.helper;

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

import java.util.Locale;

import uw.edu.uwbg.MainActivity;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

/**
 * A Singleton class to use the custom fonts for the arboretum
 * 
 * @author 	Brett Schormann
 * @version 0.2 10/28/2014
 * 			Added line to set text color black
 * 			Corrected error in getTypeface()
 * 			Added method setTextViewParameters(Fragment fragment,...
 * 			0.3 10/31/2014
 * 			Added option to setTextViewParameters() to use html
 * @since 	0.1
 */
public class CustomFont {
	
//	private String[] FONT = {
//	    "OpenSans-Bold",		"OpenSans-BoldItalic", 
//	    "OpenSans-ExtraBold", 	"OpenSans-ExtraBoldItalic",
//	    "OpenSans-Italic",		"OpenSans-Light", 
//	    "OpenSans-LightItalic",	"OpenSans-Regular",  
//	    "OpenSans-Semibold",	"OpenSans-SemiboldItalic"
//	};
	
	// TODO Color should be passed in as parameter
	
	private final float GIGANTIC_TEXT_SIZE = 22;
	private final float LARGEST_TEXT_SIZE = 16;
	private final float LARGE_TEXT_SIZE = 14;
	private final float MEDIUM_TEXT_SIZE = 12;
	private final float SMALL_TEXT_SIZE = 10;
	private final float SMALLER_TEXT_SIZE = 8;
	private final float TINY_TEXT_SIZE = 6;
	private final float TINIER_TEXT_SIZE = 4;
	private final float TINIEST_TEXT_SIZE = 2;
	
	private static CustomFont singleton;
	
	private CustomFont() {}
	
	public static CustomFont getCustomFont() {
		if (singleton == null){
			singleton= new CustomFont(); //This only executes if singleton does not exist
		}
		return singleton;
	}

	/**
	 * 
	 * @param activity
	 * @param btn
	 * @param font
	 * @param textSize
	 */
	public void setButtonParameters(Activity activity, Button btn, String font, String textSize)  {	
    	Typeface typeface = getTypeface(activity, font); 
    	float txtSize = getTextSize(textSize);
        btn.setTextSize(txtSize);
        btn.setTypeface(typeface);    	
        btn.setTextColor(Color.BLACK);
	}
	
	/**
	 * 
	 * @param activity
	 * @param tv
	 * @param font
	 * @param textSize
	 * @param txt
	 * @param isHtmlText	true if txt source is html
	 */
	public void setTextViewParameters(Activity activity, TextView tv, String font, String textSize, String txt, boolean isHtmlText)  {	
    	Typeface typeface = getTypeface(activity, font); 
    	float txtSize = getTextSize(textSize);
        tv.setTextSize(txtSize);
        tv.setTypeface(typeface);
        tv.setTextColor(Color.BLACK);
        tv.setTextColor(Color.BLACK);
        if (isHtmlText)
        	tv.setText(Html.fromHtml(txt));
        else
        	tv.setText(txt);
	}
	
	/**
	 * 
	 * @param fragment
	 * @param tv
	 * @param font
	 * @param textSize
	 * @param txt
	 */
	public void setTextViewParameters(Fragment fragment, TextView tv, String font, String textSize, String txt)  {	
    	Typeface typeface = getTypeface(MainActivity.context, font); 
    	float txtSize = getTextSize(textSize);
        tv.setTextSize(txtSize);
        tv.setTypeface(typeface);
        tv.setTextColor(Color.BLACK);
        tv.setText(txt);
	}
		
	/**
	 * 
	 * @param context
	 * @param font
	 * @return typface
	 */
	public Typeface getTypeface(Context context, String font) {
    	Typeface typeface; 
		
    	if (font.toLowerCase(Locale.getDefault()).equals("opensans-bold"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-bolditalic"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-BoldItalic.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-extrabold"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBold.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-extrabolditalic"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-italic"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-light"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-lightitalic"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-LightItalic.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-regular"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-semibold"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
    	else if (font.toLowerCase(Locale.getDefault()).equals("opensans-semibolditalic"))
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
    	else 
    		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
    	return typeface;
 	}
	
	/**
	 * 
	 * @param textSize 
	 * @return
	 */
	private float getTextSize(String textSize) {
		float txtSize;
		
		if (textSize.toUpperCase(Locale.getDefault()).equals("GIGANTIC_TEXT_SIZE"))
    		txtSize = GIGANTIC_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("LARGEST_TEXT_SIZE"))
    		txtSize = LARGEST_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("LARGE_TEXT_SIZE"))
    		txtSize = LARGE_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("MEDIUM_TEXT_SIZE"))
    		txtSize = MEDIUM_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("SMALL_TEXT_SIZE"))
    		txtSize = SMALL_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("SMALLER_TEXT_SIZE"))
    		txtSize = SMALLER_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("TINY_TEXT_SIZE"))
    		txtSize = TINY_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("TINIER_TEXT_SIZE"))
    		txtSize = TINIER_TEXT_SIZE;		
		else if (textSize.toUpperCase(Locale.getDefault()).equals("TINIEST_TEXT_SIZE"))
    		txtSize = TINIEST_TEXT_SIZE;		
		else 
    		txtSize = MEDIUM_TEXT_SIZE;				
		return txtSize;
	}


}
