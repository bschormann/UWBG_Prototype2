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

//import jbs.bg.washingtonparkarboretumoriginal.R;

import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Displays web site specified in Shared Preferences.
 * @author 	Brett Schormann
 * @version 0.3 Added code to display .pdf documents (BS)
 * @since 	0.1
 */
public class WebsiteActivity extends Activity {
	// TODO Needs to be changed for localization.
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_website);
		
		// get url from SharedPreferences
		SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
		String urlString = sharedPreferences.getString("url", "http://depts.washington.edu/uwbg/index.php");
		
		// need to use google docs for .pdf
		if (urlString.toLowerCase().endsWith(".pdf")) {
			urlString = "http://docs.google.com/gview?embedded=true&url=" + urlString;
		}
		
		webView = (WebView) findViewById(R.id.webview);
		// allow the user to navigate backward and forward through their web page history - maintained by WebView.
		webView.setWebViewClient(new webClient());		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(urlString);		
    }
 
    public class webClient extends WebViewClient  {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true; 
        }
    }
 
    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
    		webView.goBack();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }		
}
