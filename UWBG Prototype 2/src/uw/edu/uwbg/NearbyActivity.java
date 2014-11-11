package uw.edu.uwbg;

/**
Copyright © <2014> <University of Washington>

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
//
//u
import java.util.List;

import uw.edu.uwbg.helper.AddBookmarkAsync;
import uw.edu.uwbg.model.Plant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

//used nearby sample from ArcGis Samples
/**
 * @author Shima Akhavanfarid
 *
 */


public class NearbyActivity extends Activity {
	
	MapView mMapView;
	ArcGISFeatureLayer gardenLayer;
	ArcGISFeatureLayer plantMassLayer;
	ArcGISDynamicMapServiceLayer basemapLayer;
	GraphicsLayer grLayer;
    Envelope zoomExtent;
	
    boolean m_isMapLoaded = false;
    LocationDisplayManager lDisplayManager;
    int SEARCH_RADIUS=6;//plants within 6 meters ~ 20 feet 
    Graphic[] highlightGraphics;
    int layerCount=0;
    Point mapPoint,mPoint;
    Callout m_callout;
    private ViewGroup calloutContent;
    private int m_calloutStyle;
    int gID=-1;
    ProgressDialog prDialog;
    Plant plant=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		if(!isNetworkAvailable()){
	        Toast.makeText(this, "Please connect your device to the Internet", Toast.LENGTH_LONG).show();
	        }
		
		mMapView=(MapView) findViewById(R.id.nearby_map);
		
		m_callout=mMapView.getCallout();
		calloutContent=(ViewGroup) getLayoutInflater().inflate(R.layout.nearby_callout, null);
		TextView cl=(TextView) calloutContent.findViewById(R.id.close_callout);
		cl.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if (m_callout.isShowing()) {
			          m_callout.hide();
			        }
			}
		});
		TextView addbookmarks=(TextView) calloutContent.findViewById(R.id.addbookmarks_callout);
		addbookmarks.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				if(plant==null) return;
				AddBookmarkAsync addBookmarkAsyncTask=new AddBookmarkAsync(NearbyActivity.this);
				addBookmarkAsyncTask.execute(plant);
				Toast.makeText(NearbyActivity.this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();				
			}
		});
		
		//addbookmarks.setText("Added to Bookmarks");
		m_callout.setContent(calloutContent);
		
		m_calloutStyle = R.xml.identify_calloutstyle;
	    
		prDialog=ProgressDialog.show(this, "Please Wait", "Plants within twenty feet "+
		"of your current location. Tap on nearby plants to add them to Bookmarks");
		
		basemapLayer=new ArcGISDynamicMapServiceLayer( //Add aerial photography layer as a Dynamic layer
				"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/Basemaps/MapServer");
		mMapView.addLayer(basemapLayer);			
	   mMapView.addLayer(new ArcGISFeatureLayer(//walks
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/0",ArcGISFeatureLayer.MODE.SNAPSHOT));	   
	   gardenLayer=new ArcGISFeatureLayer(//gardens
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/1",ArcGISFeatureLayer.MODE.SNAPSHOT); 
		mMapView.addLayer(gardenLayer);	   
		  // gardenLayer.setOnStatusChangedListener(this);
		mMapView.addLayer(new ArcGISFeatureLayer(//trails
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/9",ArcGISFeatureLayer.MODE.SNAPSHOT));
		mMapView.addLayer(new ArcGISFeatureLayer(//beds
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/13",ArcGISFeatureLayer.MODE.SNAPSHOT));
		mMapView.addLayer(new ArcGISFeatureLayer(//mass
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/11",ArcGISFeatureLayer.MODE.SNAPSHOT));	   
		plantMassLayer= new ArcGISFeatureLayer(//native plant masses
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/12",ArcGISFeatureLayer.MODE.SNAPSHOT);
		mMapView.addLayer(plantMassLayer);
		mMapView.addLayer(new ArcGISFeatureLayer(//parking lot
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/7",ArcGISFeatureLayer.MODE.SNAPSHOT));
		
		
	    mMapView.addLayer(new ArcGISFeatureLayer(//survey
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/2",ArcGISFeatureLayer.MODE.SNAPSHOT));	   
	    
	    mMapView.addLayer(new ArcGISFeatureLayer(//landmarks
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/6",ArcGISFeatureLayer.MODE.SNAPSHOT));	   
		
	 	mMapView.addLayer(new ArcGISFeatureLayer(//boundry
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/14",ArcGISFeatureLayer.MODE.SNAPSHOT));
	 
		final ArcGISFeatureLayer plantLayer=new ArcGISFeatureLayer(//plants
 	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/3",ArcGISFeatureLayer.MODE.ONDEMAND);
		plantLayer.setVisible(true);
	/*	plantLayer.setMinScale(800);
		plantLayer.setMaxScale(10000);*/
		plantLayer.setDefinitionExpression("(LS <> 'Y' OR LS IS NULL) AND (A = 'A' OR A IS NULL)");
		mMapView.addLayer(plantLayer);
	   
		grLayer=new GraphicsLayer();
		mMapView.addLayer(grLayer);
		
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener(){

			
			private static final long serialVersionUID = 1L;
			

			public void onStatusChanged(Object source, STATUS status) {
				if (STATUS.LAYER_LOADED==status) layerCount++;
				if (layerCount==13){
					Log.i("test","all layers loaded");
					/*alllayers_loaded=true;
					if (!dialog_dissmised) ProgDialog.dismiss(); */
					MyIdentifyTask midTask=new MyIdentifyTask();
					IdentifyParameters inputParameters= setIdentifyParams();
					midTask.execute(inputParameters);
					
					layerCount++;
				}
				
				 if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
			          // Set the flag to true					 
			 			//mMapView.setExtent(env);
			 			//mMapView.zoomTo(new Point(1280131.4888656288, 236022.7787359506),(float) 0.5);
			 			//mMapView.zoomToResolution(new Point(1280131.4888656288, 236022.7787359506),20.0);
			          m_isMapLoaded = true;
			          
			          lDisplayManager = mMapView.getLocationDisplayManager();
	                  lDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
			          lDisplayManager.setLocationListener(new LocationListener() {

	                        boolean locationChanged = false;

	                        // Zooms to the current location when first GPS fix arrives.
	                        public void onLocationChanged(Location loc) {
	                            if (!locationChanged) {
	                                locationChanged = true;
	                                double locy = loc.getLatitude();
	                                double locx = loc.getLongitude();
	                                Point wgspoint = new Point(locx, locy);
	                                mapPoint = (Point) GeometryEngine
	                                        .project(wgspoint,
	                                                SpatialReference.create(4326),
	                                                mMapView.getSpatialReference());
                                   // mapPoint=new Point(1279914.6482796266, 236139.52101435696);//*****************
	                                Unit mapUnit = mMapView.getSpatialReference()
	                                        .getUnit();
	                                double zoomWidth = Unit.convertUnits(
	                                        SEARCH_RADIUS,
	                                        Unit.create(LinearUnit.Code.METER),
	                                        mapUnit);
	                                zoomExtent = new Envelope(mapPoint,
	                                        zoomWidth, zoomWidth);
	                                mMapView.setExtent(zoomExtent);
	                                /*SimpleMarkerSymbol sms = new SimpleMarkerSymbol(
	                                        Color.RED, 20, STYLE.DIAMOND);
	                                grLayer.addGraphic(new Graphic(mapPoint, sms));*/

	                            }

	                        }

	                        public void onProviderDisabled(String provider) {
	            				Toast.makeText(getApplicationContext(), "GPS Disabled. To see your location on the map please enble the GPS",
	            						Toast.LENGTH_LONG).show();
	            			}
	            	 
	            			public void onProviderEnabled(String provider) {
	            				Toast.makeText(getApplicationContext(), "GPS Enabled",
	            						Toast.LENGTH_SHORT).show();
	            			}

	                        public void onStatusChanged(String arg0, int arg1,
	                                Bundle arg2) {

	                        }
	                    });
	                    lDisplayManager.start();

	                }

	            }
	        });//new onstatus..	   
        
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {

		      private static final long serialVersionUID = 1L;

		   
		      public void onSingleTap(float x, float y) {

		        if (m_isMapLoaded) {
		          // If map is initialized and Single tap is registered on
		          // screen
		          // identify the location selected
		        	Log.i("test","Single tap");
		         
		       identifyLocation(x, y);

		        }
		      }
		    });
       
		
	}//on create
	
	 void identifyLocation(float x, float y) {

	        // Hide the callout, if the callout from previous tap is still showing
	        // on map
	        if (m_callout.isShowing()) {
	          m_callout.hide();
	        }  
	        if(grLayer.getGraphic(gID)!=null) grLayer.removeGraphic(gID);

	        // Find out if the user tapped on a feature
	        SearchForFeature(x, y);

	      }

	   
	      private void SearchForFeature(float x, float y) {
	    	  Log.i("test","search for");
	        mPoint = mMapView.toMapPoint(x, y);
	       // Log.i("test","x,y:"+mPoint.getX()+" "+mPoint.getY());
	        if (mPoint != null) {
	 
	          for (Layer layer : mMapView.getLayers()) {
	            if (layer == null)
	              continue;

	            if (layer instanceof ArcGISFeatureLayer && layer.getUrl().equals("http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/" +
	              		"MapServer/3")) {
	            	//Log.i("test","feature layer");
	              ArcGISFeatureLayer fLayer = (ArcGISFeatureLayer) layer;
	              
	              QueryAsyncTask qas=new QueryAsyncTask(fLayer, x, y);
	              qas.execute();
	              
	              //else Log.i("test","it is nottttt a plant layer!");
	            } else
	              continue;
	          }
	        }
	      }
	 
	      private void GetFeature(ArcGISFeatureLayer fLayer, float x, float y) {
	    	 // Point mapaPoint = mMapView.toMapPoint(x, y);
	    	  //Log.i("test","point"+x+" "+y+" "+mapPoint.getX()+" "+mapPoint.getY());
	    	  Query q=new Query();    	  
	    	  Envelope en=new Envelope(mPoint , 5, 5);
	    	  //q.setGeometry(new Point(1279818.7759083658, 235077.4850228727));
	    	  q.setGeometry(en);
	          fLayer.queryFeatures(q, callback);
	       
	        return;
	      }
	      
	      CallbackListener<FeatureSet> callback = new CallbackListener<FeatureSet>() {

	          public void onCallback(FeatureSet fSet) {           
	            Log.i("test","at call back");
	            Graphic[] gs=fSet.getGraphics();
	            Integer[] ids=fSet.getObjectIds();
	            List<Field> flist=fSet.getFields();
	            String name="init";
	            if(ids!=null) Log.i("testid",ids[0]+"");
	            if(gs!=null){
	          	  name=(String) gs[0].getAttributeValue("Plants.Name");
	          	  Log.i("test","at call back and graphic not null"+name);  
	          	  
	          	  // Get the values of attributes for the Graphic
		    	  
		    	  plant=new Plant();
		          plant.setAttributes(gs[0]);
	          	 
	              /*AddBookmarkAsync addBookmarkAsyncTask=new AddBookmarkAsync(NearbyActivity.this);
				  addBookmarkAsyncTask.execute(plant);
				  Log.i("test","added to bookmarks");*/
	          	 
	              showCallout(m_callout, gs[0]);		              
	            
	            }
	          }
	          public void onError(Throwable arg0) {
	              
	          }
	      };
	      
	      private void showCallout(Callout calloutView, Graphic graphic) {

	    	           
	          // Set callout properties	          	 
	    	  calloutView.setCoordinates((Point)graphic.getGeometry());
	    	  calloutView.setStyle(m_calloutStyle);
	    	  calloutView.setMaxWidth(600); 
	    	  calloutView.setMaxHeight(500);
	    	            
	    	  //TextView planttv=(TextView) calloutContent.findViewById(R.id.plantname);
	    	  TextView familytv=(TextView) calloutContent.findViewById(R.id.family);
	    	  TextView famcomm=(TextView) calloutContent.findViewById(R.id.family_common);
	    	  //TextView genus=(TextView) calloutContent.findViewById(R.id.genus);
	    	  //TextView lastre=(TextView) calloutContent.findViewById(R.id.report);
	    	  //TextView mapGrid=(TextView) calloutContent.findViewById(R.id.map_grid);
	    	  TextView scientificN=(TextView) calloutContent.findViewById(R.id.scientificn);
	    	  //TextView source=(TextView) calloutContent.findViewById(R.id.source);
	    	  TextView Accession=(TextView) calloutContent.findViewById(R.id.accession);
	    	  //TextView epithet=(TextView) calloutContent.findViewById(R.id.specific_epi);
	    	  //TextView commonN=(TextView) calloutContent.findViewById(R.id.common_name);
	    	  
	    	 // planttv.setText(plant.plantName);
	    	  familytv.setText(plant.family);
	    	  famcomm.setText(plant.familyCommonName);
	    	  //genus.setText(plant.genus);
	    	  //lastre.setText(plant.lastReportedCondition);
	    	  //mapGrid.setText(plant.mapGrid);
	    	  scientificN.setText(plant.scientificName);
	    	  //source.setText(plant.source);
	    	  Accession.setText(plant.UWBGAccession);
	    	 // epithet.setText(plant.epithet);
	    	  //commonN.setText(plant.commonName);
	    	  calloutView.setContent(calloutContent);
	    	  calloutView.refresh();
	    	  calloutView.show();	    	  
	    	  //mMapView.invalidate();
	    	  Log.i("test","just showed callout");
	    	  /*SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
	          grSymbol=new Graphic(mapPoint, sms);         
	          gID=grLayer.addGraphic(grSymbol);*/
	    	  
	          

	    	  }
	
	 class MyIdentifyTask extends
     AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
     	IdentifyTask mIdentifyTask;
     	
     	@Override
     	protected void onPreExecute() {
     		Log.i("test","MyIdentifyTask is running");
     		mIdentifyTask = new IdentifyTask(getString(R.string.mapUrl));
             
        }
     	
    
     	 @Override
          protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
              IdentifyResult[] mResult = null;
              if (params != null && params.length > 0) {
                  IdentifyParameters mParams = params[0];
                  try {
                      mResult = mIdentifyTask.execute(mParams);
                  } catch (Exception e) {
                      e.printStackTrace();
                  }

              }
              return mResult;
          }
     	 
     	 @Override
          protected void onPostExecute(IdentifyResult[] results) {
              
     		 prDialog.dismiss();
              IdentifyResult result_1=null;  
              Log.i("test","MyIdentifyTask at post execute");

              if (results != null && results.length > 0) {
            	  Log.i("test","found nearby plants");

                  highlightGraphics = new Graphic[results.length];

                  String featureName="";

                  // Highlight all features that match with results
                  for (int i = 0; i < results.length; i++) {
                	  
                      Geometry geom = results[i].getGeometry();
                      String typeName = geom.getType().name();
                      result_1 = results[i];
                      if(!result_1.getLayerName().equalsIgnoreCase("Plants")) continue;
                      //Random r=new Random();
                      int color = Color.GREEN; 
                      Log.i("test","layer name: "+result_1.getLayerName());
                      Object o=result_1.getValue();//getAttributes().get("Plants.Name");  
                      if(o!=null){
                          featureName=o.toString();
                          }
                      else featureName="X";
                    
                      if (typeName.equalsIgnoreCase("point")) {
                          SimpleMarkerSymbol sms1 = new SimpleMarkerSymbol(color, 20, STYLE.DIAMOND);
                          
                    	  TextSymbol  sms=new TextSymbol(10, featureName, Color.RED);
                      //  PictureFillSymbol sms=new PictureFillSymbol(getResources().getDrawable(R.drawable.plantsymb));

                    	  grLayer.addGraphic(new Graphic(geom, sms1));
                    	  highlightGraphics[i] = new Graphic(geom, sms);
                          
                      }
                      
                      /*else if (typeName.equalsIgnoreCase("polyline")) {
                          SimpleLineSymbol sls = new SimpleLineSymbol(color,
                                  5);
                          PictureFillSymbol sls=new PictureFillSymbol(getResources().getDrawable(R.drawable.plantsymbole));

                          highlightGraphics[i] = new Graphic(geom, sls);                          

                      } else if (typeName.equalsIgnoreCase("polygon")) {
                          //SimpleFillSymbol sfs = new SimpleFillSymbol(color);
                          PictureFillSymbol sfs=new PictureFillSymbol(getResources().getDrawable(R.drawable.plantsymbole));
                          sfs.setAlpha(75);  
                          highlightGraphics[i] = new Graphic(geom, sfs);
                      }*/
                      
                  }
                  
                  grLayer.addGraphics(highlightGraphics);
                 

          }
              
              else {
             	 Log.i("test","Noooo plant");
                  Toast toast = Toast.makeText(getApplicationContext(),
                          "No features identified.", Toast.LENGTH_SHORT);
                  toast.show();
              }
              
              
       }//end of postExecute
     
     }//end of MyIdentifyTask
 	
     public boolean isNetworkAvailable() {
         ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = cm.getActiveNetworkInfo();
         // if no network is available networkInfo will be null
         // otherwise check if we are connected
         if (networkInfo != null && networkInfo.isConnected()) {
             return true;
         }
         return false;
     }
     
     private IdentifyParameters setIdentifyParams(){
    	 
    	 IdentifyParameters inputParameters=new IdentifyParameters();
    	 inputParameters.setGeometry(zoomExtent);//************************************
    	 
         inputParameters.setLayers(new int[] {3});
         Envelope env1 = new Envelope();
         mMapView.getExtent().queryEnvelope(env1);
         inputParameters.setSpatialReference(mMapView
                 .getSpatialReference());
         inputParameters.setMapExtent(env1);
         inputParameters.setDPI(96);
         inputParameters.setMapHeight(mMapView.getHeight());
         inputParameters.setMapWidth(mMapView.getWidth());
         inputParameters.setTolerance(10);
         
         return inputParameters;
     }
     
     class QueryAsyncTask extends AsyncTask<Void, Void, Void>{
 		ArcGISFeatureLayer fLayer;
 		float x,y;
 		public QueryAsyncTask(ArcGISFeatureLayer fLayer, float x, float y){
 			this.fLayer=fLayer;
 			this.x=x;
 			this.y=y;
 		}

 		@Override
 		protected Void doInBackground(Void... params) {
 			GetFeature(fLayer, x, y);
 			return null;
 		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
		}
 		
 		
 	}

 	@Override
 	public boolean onCreateOptionsMenu(Menu menu) {
 		    Log.i("test","at create option menu");		    
 	 		getMenuInflater().inflate(R.menu.third_menu, menu);	 	    
 	 		return true;
 	}
 	
 	
 	

 	@Override
 	public boolean onOptionsItemSelected(MenuItem item) {
 	    switch(item.getItemId()){
 	    case R.id.home:
 	    	Intent i=new Intent(this,MainActivity.class);
 	    	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
 	    	startActivity(i);
 	    	finish();
 	    	break;
 	    }
 		return true;
 	}
 	
 	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}

}
