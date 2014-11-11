package uw.edu.uwbg.helper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Downloads a pdf and displays it.
 * @see <a href="http://stackoverflow.com/questions/10520009/android-load-pdf-inside-webview">http://stackoverflow.com/questions/10520009/android-load-pdf-inside-webview></a>
 * 
 * @author 	Brett Schormann
 * @version 0.2 10/30/2014
 * 			Changes to make task work.
 * @since 	0.2
 */
public class DownloadPDFTask extends AsyncTask<String, Void, Integer>  {
	
    protected ProgressDialog mWorkingDialog;    // progress dialog
    protected String mFileName;         		// downloaded file
    protected String mError;            		// for errors

    private Context mContext;
    
    public DownloadPDFTask(Context _mContext) {
    	this.mContext = _mContext;
    }
    
    @Override
    protected Integer doInBackground(String... urls)  {

    try      {
    	byte[] dataBuffer = new byte[4096];
    	int nRead = 0;
        // set local filename to last part of URL
        String[] strURLParts = urls[0].split("/");
        if (strURLParts.length > 0)
            mFileName = strURLParts[strURLParts.length - 1];
        else
            mFileName = "REPORT.pdf";

        // download URL and store to strFileName

        // connection to url
        java.net.URL urlReport = new java.net.URL(urls[0]);
        URLConnection urlConn = urlReport.openConnection();
        InputStream streamInput = urlConn.getInputStream();
        BufferedInputStream bufferedStreamInput = new BufferedInputStream(streamInput);
//        FileOutputStream outputStream = MainActivity.openFileOutput(mFileName, Context.MODE_WORLD_READABLE); // must be world readable so external Intent can open!
        FileOutputStream outputStream = mContext.openFileOutput(mFileName, Context.MODE_WORLD_READABLE);
        while ((nRead = bufferedStreamInput.read(dataBuffer)) > 0)
        	outputStream.write(dataBuffer, 0, nRead);
        streamInput.close();
        outputStream.close();
    } catch (Exception e) {
    	Log.e("DownLoadPDF Task", e.getMessage());
    	mError = e.getMessage();
    	return (1);
    }
    return (0);
    }

    //-------------------------------------------------------------------------
    // PreExecute - UI thread setup
    //-------------------------------------------------------------------------

    @Override
    protected void onPreExecute() {
    	// show "Downloading, Please Wait" dialog
    	mWorkingDialog = ProgressDialog.show(mContext, "", "Downloading PDF Document, Please Wait...", true);
    	return;
    }

    //-------------------------------------------------------------------------
    // PostExecute - UI thread finish
    //-------------------------------------------------------------------------

    @Override
    protected void onPostExecute (Integer result)     {
    	if (mWorkingDialog != null)  {
    		mWorkingDialog.dismiss();
    		mWorkingDialog = null;
    	}

        switch (result)  {
        case 0:	 // a URL
            // Intent to view download PDF
            Uri uri  = Uri.fromFile(mContext.getFileStreamPath(mFileName));

            try  {
                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                intentUrl.setDataAndType(uri, "application/pdf");
                intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentUrl);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
            }
            break;

        case 1:	 // Error
            Toast.makeText(mContext, mError, Toast.LENGTH_LONG).show();
            break;
        }
    }
}

