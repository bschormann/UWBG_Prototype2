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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * 
 * @author Brett Schormann
 * @version 0.2 10/27/2014
 * @since 0.1
 */
public class SplashActivity extends Activity {

	private ProgressDialog mProgressDialog; 
    private final static int SPLASH_TIME = 4000;	// divided by 5 equals seconds
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
 
    /**
     * 
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	// get update from app store
        new downloadData().execute();        
    }
    
    /**
     * Async Task to get update from app store
     */
    private class downloadData extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SplashActivity.this);
            mProgressDialog.setTitle("Contacting App Store");
            mProgressDialog.setMessage("Loading data...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();                   
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	// TODO should be request to app store
        	try {
        		Thread.sleep(SPLASH_TIME);       
        	} catch (InterruptedException e) {
        		
        	}
        	return null;	
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        	//Toast.makeText(getApplicationContext(), "Finished.", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
            Intent intent = new Intent(SplashActivity.this, 
            		MainActivity.class);
            startActivity(intent);
            // close this activity            
            finish();        
        }
    }
}
