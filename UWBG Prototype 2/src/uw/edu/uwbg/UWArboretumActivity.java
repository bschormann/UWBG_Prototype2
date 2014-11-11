package uw.edu.uwbg;

/*used following samples from ARCGIS website:
query cloud feature service sample
query feature service table
Highlight feature sample*/


//do to: incerease speed, load all field for plants, show other layers info on the map
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import uw.edu.uwbg.helper.AddBookmarkAsync;
import uw.edu.uwbg.model.Plant;


/**
 * @author Shima Akhavanfarid
 *
 */

/**
 * @author Shima
 *
 */
public class UWArboretumActivity extends Activity  implements OnQueryTextListener,OnStatusChangedListener{
	
	MapView mMapView;
	SearchView searchView;
	Envelope env;
	boolean m_isMapLoaded;
	Graphic m_identifiedGraphic;
	private Callout m_callout;
	private int m_calloutStyle;
	ProgressBar progBar;
	ProgressDialog ProgDialog;
	ProgressDialog prog;
	private ViewGroup calloutContent;
    private Point mapPoint;
    Graphic grSymbol; 
    Graphic[] highlightGraphics;
    GraphicsLayer grLayer;
    int gID=-1;
    int layerCount=0;
    Envelope en;
    Point mLocation;
    ArcGISFeatureLayer gardenLayer;
    ArcGISFeatureLayer plantMassLayer;
    ArcGISDynamicMapServiceLayer allLayers;
    ArcGISDynamicMapServiceLayer basemapLayer;
    ArcGISDynamicMapServiceLayer streetLayer;
    boolean alllayers_loaded,plantgraphicon=false;
    boolean dialog_dissmised=true;
    Plant plant=null;
    GestureDetector gd;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);     
        getActionBar().setDisplayShowTitleEnabled(true);
		//mMapView = new MapView(this); 
        if(!isNetworkAvailable()){
        Toast.makeText(this, "Please connect your device to the Internet", Toast.LENGTH_LONG).show();
        }
		mMapView=(MapView) findViewById(R.id.map);
		ProgDialog=ProgressDialog.show(this, "Loading the Map!", getResources().getString(R.string.Dialog_text));
		ProgDialog.setProgressStyle(R.style.dialoge_style);
		ProgDialog.show();
		
		Handler han=new Handler();
		Runnable r=new Runnable() {
			
			public void run() {
				
				if (alllayers_loaded) ProgDialog.dismiss();	
				else dialog_dissmised=false;
			}
		};
		han.postDelayed(r, 15000);
		
		
		//getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
		 
        LocationDisplayManager ls = mMapView.getLocationDisplayManager();
        
		ls.setLocationListener(new MyLocationListener());
		ls.start();
		ls.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
        
		m_callout=mMapView.getCallout();
		calloutContent=(ViewGroup) getLayoutInflater().inflate(R.layout.identify_callout_content, null);
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
				AddBookmarkAsync addBookmarkAsyncTask=new AddBookmarkAsync(UWArboretumActivity.this);
				addBookmarkAsyncTask.execute(plant);
				Toast.makeText(UWArboretumActivity.this, "Added to Bookmarks", Toast.LENGTH_SHORT).show();				
			}
		});
		
		m_callout.setContent(calloutContent);
		
		m_calloutStyle = R.xml.identify_calloutstyle;
		
		//adjust to UWBG map extents
		
		//env=new Envelope(-1.3296373526814876E7, 3930962.41823043, -1.2807176545789773E7, 4201243.7502468005);
		//env=new Envelope(1277698.7250303626, 231387.09607818723,1282477.4996485263, 244562.67943646014);
		env=new Envelope(1279108.190939039, 231920.20797632635, 1280635.8646959215, 237807.50744993985);	
		
		
		basemapLayer=new ArcGISDynamicMapServiceLayer( //Add aerial photography layer as a Dynamic layer
				"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/Basemaps/MapServer");
		mMapView.addLayer(basemapLayer);
		
		
		/*streetLayer=new ArcGISDynamicMapServiceLayer(//Add transportation layer as a Dynamic layer
				"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/TransportationBasemap/MapServer");		
	    mMapView.addLayer(streetLayer);*/
	   // streetLayer.setOnStatusChangedListener(this);   
	    
       /* allLayers=new ArcGISDynamicMapServiceLayer( //the address for the all the layers except basemap 
                "http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/UWBG/MapServer/");
	    mMapView.addLayer(allLayers);*/
	  /*  allLayers=new ArcGISDynamicMapServiceLayer( //the address for the all the layers except basemap 
                "http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/");
	    mMapView.addLayer(allLayers);
	    */
	   

			
	   mMapView.addLayer(new ArcGISFeatureLayer(//walks
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/0",ArcGISFeatureLayer.MODE.SNAPSHOT));	   
	   gardenLayer=new ArcGISFeatureLayer(//gardens
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/1",ArcGISFeatureLayer.MODE.SNAPSHOT); 
		mMapView.addLayer(gardenLayer);	   
		  
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
	 	/*mMapView.addLayer(new ArcGISFeatureLayer(//squares
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/15",ArcGISFeatureLayer.MODE.SNAPSHOT));
		
		mMapView.addLayer(new ArcGISFeatureLayer(//roads
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/8",ArcGISFeatureLayer.MODE.ONDEMAND));
		mMapView.addLayer(new ArcGISFeatureLayer(//water
	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/10",ArcGISFeatureLayer.MODE.ONDEMAND));
		*/
		final ArcGISFeatureLayer plantLayer=new ArcGISFeatureLayer(//plants
 	    		"http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/MapServer/3",ArcGISFeatureLayer.MODE.ONDEMAND);
		plantLayer.setVisible(true);
		//plantLayer.setMinScale(0);
//	plantLayer.setMaxScale(2000);
		//plantLayer.setBufferFactor(50);
		//plantLayer.setConstraintFactor(0.03);		
		//plantLayer.setOnStatusChangedListener(this);
		//plantLayer.setSelectionSymbol(new PictureFillSymbol(getResources().getDrawable(R.drawable.plantsymbole)));
		//Log.i("test","maxmin "+plantMassLayer.getMaxScale()+" "+plantMassLayer.getMinScale());

		plantLayer.setDefinitionExpression("(LS <> 'Y' OR LS IS NULL) AND (A = 'A' OR A IS NULL)");
		mMapView.addLayer(plantLayer);
	   
		grLayer=new GraphicsLayer();
		mMapView.addLayer(grLayer);
	

      
		  mMapView.setOnZoomListener(new OnZoomListener() {
				
				/**
			 * 
			 */
			private static final long serialVersionUID = 5493360546145356626L;

				public void preAction(float pivotX, float pivotY, double factor) {
					// TODO Auto-generated method stub
					
				}
				
				public void postAction(float pivotX, float pivotY, double factor) {
					Geometry zoomgeom=mMapView.getExtent();
					Log.i("testpost",factor+" "+pivotX+" "+pivotY+" "+mMapView.getExtent().calculateArea2D());
					if(mMapView.getExtent().calculateArea2D()>-5500) {
						//if( !plantgraphicon){
						plantgraphicon=true;
						Log.i("test","zoomed enough");
						MyIdentifyTask mIdenitfy = new MyIdentifyTask();
			        	
	                    // Set parameters for identify task
	                    IdentifyParameters inputParameters = new IdentifyParameters();
	                    inputParameters.setGeometry(zoomgeom);
	                    inputParameters
	                            .setLayers(new int[] {1});
	                    Envelope env1 = new Envelope();
	                    mMapView.getExtent().queryEnvelope(env1);
	                    inputParameters.setSpatialReference(mMapView
	                            .getSpatialReference());
	                    inputParameters.setMapExtent(env1);
	                    inputParameters.setDPI(96);
	                    inputParameters.setMapHeight(mMapView.getHeight());
	                    inputParameters.setMapWidth(mMapView.getWidth());
	                    inputParameters.setTolerance(10);

	                    // Execute identify task
	                    mIdenitfy.execute(inputParameters);
						//}
					}
					else{
						//plantgraphicon=false;
						grLayer.removeAll();
					}
					
				}
			});
		  
	
		
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener(){       
			
			private static final long serialVersionUID = 1L;

			public void onStatusChanged(Object source, STATUS status) {			
		
				if (STATUS.LAYER_LOADED==status) layerCount++;
				if (layerCount==13){
					Log.i("test","max scale: "+ plantLayer.getMaxScale()+" "+plantLayer.getMinScale());
					Log.i("test","all layers loaded");
					alllayers_loaded=true;
					if (!dialog_dissmised) ProgDialog.dismiss(); 
					layerCount++;
				}
				
				 if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
			          // Set the flag to true					 
			 			mMapView.setExtent(env);
			 			//mMapView.zoomTo(new Point(1280131.4888656288, 236022.7787359506),(float) 0.5);
			 			//mMapView.zoomToResolution(new Point(1280131.4888656288, 236022.7787359506),20.0);
			          m_isMapLoaded = true;
			          
			        }		
			} 
			
		});
		
		
		
		 mMapView.setOnSingleTapListener(new OnSingleTapListener() {

		      private static final long serialVersionUID = 1L;

		   
		      public void onSingleTap(float x, float y) {

		        if (m_isMapLoaded) {
		          // If map is initialized and Single tap is registered on
		          // screen
		          // identify the location selected
		        	Log.i("test","Single tap");
		         
		        	
		       /* 	MyIdentifyTask mIdenitfy = new MyIdentifyTask();
		        	Handler h=new Handler();
		        	h.postDelayed(new Runnable() {
						
						public void run() {
							if(progBar!=null && progBar.getVisibility()==View.VISIBLE) {progBar.setVisibility(View.GONE);return;}
				        	if(ProgDialog!=null && ProgDialog.isShowing()) {
				        		Log.i("test","dialog is running");
				        		ProgDialog.dismiss();
				        		return;
				        	}						
						}
					}, 20000);
		        	
                   
                    // Get the point user clicked on
                    Point pointClicked = mMapView.toMapPoint(x, y);

                    // Set parameters for identify task
                    IdentifyParameters inputParameters = new IdentifyParameters();
                    inputParameters.setGeometry(pointClicked);
                    inputParameters
                            .setLayers(new int[] {0,11,12,7,9,12,13,1});
                    Envelope env1 = new Envelope();
                    mMapView.getExtent().queryEnvelope(env1);
                    inputParameters.setSpatialReference(mMapView
                            .getSpatialReference());
                    inputParameters.setMapExtent(env1);
                    inputParameters.setDPI(96);
                    inputParameters.setMapHeight(mMapView.getHeight());
                    inputParameters.setMapWidth(mMapView.getWidth());
                    inputParameters.setTolerance(10);

                    // Execute identify task
                    mIdenitfy.execute(inputParameters);*/
                    
                    identifyLocation(x, y);

		        }
		      }
		    });
    }
    
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
        mapPoint = mMapView.toMapPoint(x, y);
        Log.i("test","x,y:"+mapPoint.getX()+" "+mapPoint.getY());
        if (mapPoint != null) {
 
          for (Layer layer : mMapView.getLayers()) {
            if (layer == null)
              continue;

            if (layer instanceof ArcGISFeatureLayer) {
            	//Log.i("test","feature layer");
              ArcGISFeatureLayer fLayer = (ArcGISFeatureLayer) layer;
              
              if(layer.getUrl().equals("http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/" +
              		"MapServer/3")){
            	  //Log.i("test","it is a plant layer!");
              //GetFeature(fLayer, x, y);
              QueryAsyncTask qas=new QueryAsyncTask(fLayer, x, y);
              qas.execute();
              }
              //else Log.i("test","it is nottttt a plant layer!");
            } /*else
              continue;*/
            //****************************************************
            if (layer instanceof FeatureLayer) {
            	Log.i("test","newwwwwwwwwwwwwwwwwww");
              ArcGISFeatureLayer fLayer = (ArcGISFeatureLayer) layer;
              
              if(layer.getUrl().equals("http://uwbgmaps.cfr.washington.edu/arcgis/rest/services/PublicFeatures/" +
              		"MapServer/3")){
            	 // Log.i("test","it is a plant layer!");
              //GetFeature(fLayer, x, y);
              QueryAsyncTask qas=new QueryAsyncTask(fLayer, x, y);
              qas.execute();
              }
              //else Log.i("test","it is nottttt a plant layer!");
            } else
              continue;
            //******************
          }
        }
      }
 
      private void GetFeature(ArcGISFeatureLayer fLayer, float x, float y) {
    	 // Point mapaPoint = mMapView.toMapPoint(x, y);
    	  //Log.i("test","point"+x+" "+y+" "+mapPoint.getX()+" "+mapPoint.getY());
    	  Query q=new Query();    	  
    	  en=new Envelope(mapPoint , 5, 5);
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
            	Graphic res=gs[0];
          	  name=(String) gs[0].getAttributeValue("Plants.Name");
          	  Log.i("test","at call back and graphic not null"+name);
          	  for(Graphic g:gs){
          		  Point p=(Point) g.getGeometry();
          		  if((Math.pow(p.getX()-mapPoint.getX(),2)+Math.pow(p.getY()-mapPoint.getY(),2))<
          				  (Math.pow(((Point)res.getGeometry()).getX()-mapPoint.getX(),2)+Math.pow(((Point)res.getGeometry()).getY()-mapPoint.getY(),2))) res=g;
          		 
          	  }
            
           /* for(String t:gs[0].getAttributeNames()){
            	Log.i("test","attribute name: "+t+" ");
            }*/
          	  
            showCallout(m_callout, res);
            
            }
          }
          public void onError(Throwable arg0) {
              
          }
      };
      
      private void showCallout(Callout calloutView, Graphic graphic) {

    	    // Get the values of attributes for the Graphic
    	  
    	  plant=new Plant();
          plant.setAttributes(graphic);
    	    // Set callout properties
    	  calloutView.setCoordinates((Point)graphic.getGeometry());
    	  calloutView.setStyle(m_calloutStyle);
    	  calloutView.setMaxWidth(600); 
    	  calloutView.setMaxHeight(500);
    	            
    	  TextView planttv=(TextView) calloutContent.findViewById(R.id.plantname);
    	  TextView familytv=(TextView) calloutContent.findViewById(R.id.family);
    	  TextView famcomm=(TextView) calloutContent.findViewById(R.id.family_common);
    	  TextView genus=(TextView) calloutContent.findViewById(R.id.genus);
    	  TextView lastre=(TextView) calloutContent.findViewById(R.id.report);
    	  TextView mapGrid=(TextView) calloutContent.findViewById(R.id.map_grid);
    	  TextView scientificN=(TextView) calloutContent.findViewById(R.id.scientificn);
    	  TextView source=(TextView) calloutContent.findViewById(R.id.source);
    	  TextView Accession=(TextView) calloutContent.findViewById(R.id.accession);
    	  TextView epithet=(TextView) calloutContent.findViewById(R.id.specific_epi);
    	  TextView commonN=(TextView) calloutContent.findViewById(R.id.common_name);
    	  
    	  planttv.setText(plant.plantName);
    	  familytv.setText(plant.family);
    	  famcomm.setText(plant.familyCommonName);
    	  genus.setText(plant.genus);
    	  lastre.setText(plant.lastReportedCondition);
    	  mapGrid.setText(plant.mapGrid);
    	  scientificN.setText(plant.scientificName);
    	  source.setText(plant.source);
    	  Accession.setText(plant.UWBGAccession);
    	  epithet.setText(plant.epithet);
    	  commonN.setText(plant.commonName);
    	  calloutView.setContent(calloutContent);
    	  calloutView.show();
          mMapView.requestFocus();
    	 
    	  //mMapView.invalidate();
    	  Log.i("test","just showed callout");
    	  /*SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
          grSymbol=new Graphic(mapPoint, sms);         
          gID=grLayer.addGraphic(grSymbol);*/
    	  
          

    	  }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
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
	
	private class MyLocationListener implements LocationListener {
		 
		public MyLocationListener() {
			super();
		}
	 
		public void onLocationChanged(Location loc) {
			if (loc == null)
				return;
			//Log.i("test","at location "+loc.getLatitude()+" "+loc.getLongitude());
			boolean zoomToMe = (mLocation == null) ? true : false;
			mLocation = new Point(loc.getLongitude(), loc.getLatitude());
			if (zoomToMe) {
				Point p = (Point) GeometryEngine.project(mLocation, SpatialReference.create(4326),
                        mMapView.getSpatialReference()); 
				
				 /*SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
				 grSymbol=new Graphic(p, sms);
				grLayer.addGraphic(grSymbol);*/
				 Log.i("test","GPSLOC: "+p.getX()+" "+p.getY());
				//mMapView.zoomToResolution(p, 20.0);
			} 
		}
	 
			public void onProviderDisabled(String provider) {
				Toast.makeText(getApplicationContext(), "GPS Disabled. To see your location on the map please enble the GPS",
						Toast.LENGTH_SHORT).show();
			}
	 
			public void onProviderEnabled(String provider) {
				Toast.makeText(getApplicationContext(), "GPS Enabled",
						Toast.LENGTH_SHORT).show();
			}
	 
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		    Log.i("test","at create option menu");
		    
	 		getMenuInflater().inflate(R.menu.second_menue, menu);
	 		searchView= (SearchView) menu.findItem(R.id.search).getActionView();
	 		Log.i("test",Build.VERSION.SDK_INT+"");
	 		searchView.setOnQueryTextListener(this);
	 		ImageView v=(ImageView) searchView.findViewById(getResources().getIdentifier("android:id/search_button", null, null));
	 		v.setImageResource(R.drawable.glass);
	 	    //searchView.setIconifiedByDefault(true);
	 	    searchView.setQueryHint("Plant LookUp");
	 	  
	 	    
	 		return true;
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId()){
	    case R.id.home:
	    	finish();
	    	break;
	    }
		return true;
	}

	public boolean onQueryTextSubmit(String query) {
		
		Handler h=new Handler();
    	h.postDelayed(new Runnable() {
			
			public void run() {
				Log.i("test","handlere executed");
	        	if(prog!=null && prog.isShowing()) {
	        		Log.i("test","dialog is running");
	        		prog.dismiss();
	        		return;
	        	}						
			}
		}, 20000);
    	
		
		SearchPlantsAsyncTask spat=new SearchPlantsAsyncTask();
		spat.execute(query);
		Log.i("test","at query");
		
		return true;
	}

	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
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
		
	}
	
	class SearchPlantsAsyncTask extends AsyncTask<String, Void, FeatureResult>{
        
		@Override
		protected FeatureResult doInBackground(String... params) {
			Log.i("test","getting feature from search query");
			//String whereSt="Plants.Name like '%"+params[0]+" %' or BGBaseData.Family like '%"+params[0]+" %'";
			
			String wordU=new String(params[0].charAt(0)+"").toUpperCase()+params[0].substring(1);
			String wordL=new String(params[0].charAt(0)+"").toLowerCase()+params[0].substring(1);
			
			String whereSt="Plants.Name like '% "+wordL+"%' or Plants.Name like '"+wordU+"%'"+" or" +
					" BGBaseData.Family like '% "+wordL+"%' or BGBaseData.Family like '"+wordU+"%' or " +
					"BGBaseData.FamilyCommonName like '% "+wordL+"%' or BGBaseData.FamilyCommonName like '"+
					wordU+"%'"+" or " +"BGBaseData.CommonName like '% "+wordL+"%' or " +
							"BGBaseData.CommonName like '"+wordU+"%'";
			/*String whereSt="Plants.Name like '%"+params[0]+" %' or BGBaseData.Family like '%"+params[0]+" %' or " +
			"BGBaseData.FamilyCommonName like '%"+params[0]+" %'";*/
			
			// Define a new query and set parameters
		      QueryParameters mParams = new QueryParameters();
		      mParams.setWhere(whereSt);
		     // mParams.setOutFields(new String[]{"*"});
		      mParams.setReturnGeometry(true);
              Log.i("test",whereSt);
		      // Define the new instance of QueryTask
		      QueryTask qTask = new QueryTask("http://uwbgmaps.cfr.washington.edu/arcgis/rest/services" +
		      		"/PublicFeatures/MapServer/3");
		      FeatureResult results;

		      try {
		        // run the querytask
		    	  
		        results = qTask.execute(mParams);
		        return results;
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		      return null;

		}//doInBackground

		@Override
		protected void onPreExecute() {
			grLayer.removeAll();
			prog=ProgressDialog.show(UWArboretumActivity.this, "", "Please Wait");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(FeatureResult result) {
			Log.i("test","search ended");
			// Remove the result from previously run query task
		      grLayer.removeAll();

		      // Define a new marker symbol for the result graphics
		      SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND);
             //PictureMarkerSymbol sms=new PictureMarkerSymbol(UWArboretumActivity.this, getResources().getDrawable(R.drawable.plantsymbole));
		     
             // Envelope to focus on the map extent on the results
		      Envelope extent = new Envelope();
              if(result == null) {
            	  Log.i("test","result null");
            	  return;
              }
              int i=0;
		      // iterate through results
		      for (Object element : result) {
		        // if object is feature cast to feature
		        if (element instanceof Feature) {
		        	i++;
		          Feature feature = (Feature) element;
		          Log.i("test","it is a feature"+feature.getAttributeValue("Plants.Accession"));
		          // convert feature to graphic
		          Graphic graphic = new Graphic(feature.getGeometry(), sms, feature.getAttributes());
		          // merge extent with point
		          extent.merge((Point)graphic.getGeometry());
		          // add it to the layer
		          grLayer.addGraphic(graphic);
		        }
		      }
		      Log.i("test","search res: "+i);

		      // Set the map extent to the envelope containing the result graphics
		      //map.setExtent(extent, 100);
		      
		      prog.dismiss();


			super.onPostExecute(result);
		}
		
		
		
	}

	public void onStatusChanged(Object source, STATUS status) {	
		//ProgDialog.dismiss();
		Log.i("testiiiing",source.toString());
		//progBar.setVisibility(View.GONE);
				
	}
	
	/*private class MyIdentifyTask extends
    AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
    	IdentifyTask mIdentifyTask;
    	
    	@Override
    	protected void onPreExecute() {
    		// create dialog while working off UI thread
            ProgDialog = ProgressDialog.show(UWArboretumActivity.this,
            "Please Wait!", "Fetching Information");

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

             IdentifyResult result_1=null;

             // dismiss dialog
             if (ProgDialog.isShowing()) {
                 ProgDialog.dismiss();
             }

             if (results != null && results.length > 0) {

                 highlightGraphics = new Graphic[results.length];

                 Toast toast = Toast.makeText(getApplicationContext(),
                         results.length + " features identified\n",
                         Toast.LENGTH_LONG);
                 
                 toast.show();
                 String featureName="";

                 // Highlight all features that match with results
                 for (int i = 0; i < results.length; i++) {
                     Geometry geom = results[i].getGeometry();
                     String typeName = geom.getType().name();
                     result_1 = results[i];
                     //Random r=new Random();
                     int color = Color.RED; 
                     Log.i("test","layer name: "+result_1.getLayerName());
                     Object o=result_1.getAttributes().get("Name");                     
                    if(o!=null) featureName = o.toString();
                    
                    if( o==null || featureName.equals("Null")){
                    	 Log.i("test","at second"); 
                    	 o=result_1.getAttributes()
                                 .get("Bed_Name");
                         if(o!=null) featureName = o.toString();                         
                     }
                    if( featureName.equals("Null")) featureName="Native Plant Mass"; 
                     
                     if (typeName.equalsIgnoreCase("point")) {
                         SimpleMarkerSymbol sms = new SimpleMarkerSymbol(
                                 color, 20, STYLE.SQUARE);
                         highlightGraphics[i] = new Graphic(geom, sms);

                     } else if (typeName.equalsIgnoreCase("polyline")) {
                         SimpleLineSymbol sls = new SimpleLineSymbol(color,
                                 5);
                         highlightGraphics[i] = new Graphic(geom, sls);

                     } else if (typeName.equalsIgnoreCase("polygon")) {
                         SimpleFillSymbol sfs = new SimpleFillSymbol(color);
                         sfs.setAlpha(75);  
                         highlightGraphics[i] = new Graphic(geom, sfs);
                     }
                     Log.i("test","layer name: "+featureName);
                     // set the Graphic's geometry, add it to GraphicLayer
                     // and
                     // refresh the Graphic Layer
                     grLayer.addGraphic(highlightGraphics[i]);
                     break;
                 }
                 Toast toast=null;
                 if(result_1!=null) {
                	 
                	if(result_1.getAttributes().get("Garden")!=null && 
                			!result_1.getAttributes().get("Garden").equals("null") )
                		toast = Toast.makeText(getApplicationContext(),
                     result_1.getAttributes().get("Garden")+" "+featureName , Toast.LENGTH_SHORT);
                	
                	else    toast = Toast.makeText(getApplicationContext(),featureName , Toast.LENGTH_SHORT);
                 }
                
                 toast.setGravity(Gravity.BOTTOM, 0, 0);
                 toast.show();
                 

         }
             
             else {
            	 Log.i("test","Noooo Garden");
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "No features identified.", Toast.LENGTH_SHORT);
                 toast.show();
             }
             
             
      }//end of postExecute
    
    }//end of MyIdentifyTask
*/	
	
	
	private class MyIdentifyTask extends
    AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {
    	IdentifyTask mIdentifyTask;
    	
    	@Override
    	protected void onPreExecute() {
    		grLayer.removeAll();
    		// create dialog while working off UI thread
           /* ProgDialog = ProgressDialog.show(UWArboretumActivity.this,
            "Please Wait!", "Fetching Information");*/
    		Log.i("test","at preex");

            mIdentifyTask = new IdentifyTask(getString(R.string.BGUrl));
            
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
    		 Log.i("test","at postex ");
             IdentifyResult result_1=null;

             // dismiss dialog
            /* if (ProgDialog.isShowing()) {
                 ProgDialog.dismiss();
             }*/

             if (results != null && results.length > 0) {

                 highlightGraphics = new Graphic[results.length];

                
                 String featureName="";

                 // Highlight all features that match with results
                 for (int i = 0; i < results.length; i++) {
                     Geometry geom = results[i].getGeometry();
                     String typeName = geom.getType().name();
                     result_1 = results[i];
                     //Random r=new Random();
                     int color = Color.GREEN; 
                     Log.i("test","layer name: "+result_1.getLayerName());
                     Object o=result_1.getAttributes().get("Name");                     
                    if(o!=null) featureName = o.toString();
                    
                    if( o==null || featureName.equals("Null")){
                    	 Log.i("test","at second"); 
                    	 o=result_1.getAttributes()
                                 .get("Bed_Name");
                         if(o!=null) featureName = o.toString();                         
                     }
                    if( featureName.equals("Null")) featureName="Native Plant Mass"; 
                     
                     if (typeName.equalsIgnoreCase("point")) {
                         SimpleMarkerSymbol sms = new SimpleMarkerSymbol(
                                 color, 15, STYLE.CROSS);
                         highlightGraphics[i] = new Graphic(geom, sms);

                     }
                     Log.i("test","layer name: "+featureName);
                                       
                 }
                 grLayer.addGraphics(highlightGraphics);                   

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


    
}//end of activity