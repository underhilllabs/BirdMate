package com.underhilllabs.birdmate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NearbyBirdsActivity extends ListActivity {
	String[] birdnames; 
	Location loc;
	LocationManager lm;
	String strJson;
	int maxResults;
	// the arrays for Bird Sightings
	//String [] com_names;
	List <String> com_names;
	//String [] sci_names;
	double [] lats;
	double [] lngs;
	//List <Double> lats;
	//List <Double> lngs;
	//String [] dates;
	//String [] locNames;
	String [] snippets;
	List<BirdSighting> bs_list;
	private ArrayAdapter<BirdSighting> aa;
	public final String TAG = "BirdMate";
	public final int MAP_ID = Menu.FIRST;
	public final int HOME_ID = Menu.FIRST+1;
	public final int MAX_RESULTS=15;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_sightings_list);
        bs_list = new ArrayList<BirdSighting>();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        maxResults = 10;
        lats = new double[MAX_RESULTS];
        lngs = new double[MAX_RESULTS];
        //lats = new ArrayList<Double>();
        //lngs = new ArrayList<Double>();
        //com_names = new String[MAX_RESULTS];
        com_names = new ArrayList<String>();
        snippets = new String[MAX_RESULTS];
    	ListView lv = getListView();
    	lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				 BirdSighting bs = bs_list.get(position);
  				 Log.d("birdmate","com name "+bs.com_name);
				 Intent i = new Intent(NearbyBirdsActivity.this, BirdSightingView.class);
		         i.putExtra("com_name",bs.com_name);
		         i.putExtra("sci_name",bs.sci_name);
		         i.putExtra("date",bs.date);
		         i.putExtra("locId",bs.locId);
		         i.putExtra("lat",bs.latitude);
		         i.putExtra("lng",bs.longitude);
		         startActivity(i);
			}
         });

    	String restUrl = "http://ebird.org/ws1.1/data/obs/geo/recent?lng="+loc.getLongitude()+"&lat="+loc.getLatitude()+"&dist=10&back=5&maxResults="+MAX_RESULTS+"&locale=en_US&fmt=json"; 
    	new DownloadDataTask().execute(restUrl);
        
    }
    private void updateList() {
    	if(bs_list.isEmpty()) {
    		new AlertDialog.Builder(NearbyBirdsActivity.this).setMessage("No bird sightings found!").setTitle("Bird Mate").show();
    	}
    	aa = new ArrayAdapter<BirdSighting>(this, R.layout.bird_sightings_list_row,R.id.tvBirdSighting, bs_list);
    	setListAdapter(aa);    
    }
    private void getRestStuff (String myUrl) {
        String result = queryRESTurl(myUrl);

        
        try {
        	JSONArray json_arr= new JSONArray(result);
        	if (MAX_RESULTS > json_arr.length()) {
        		maxResults = json_arr.length();
        	} else {
        		maxResults = MAX_RESULTS;
        	}
        	for(int i = 0; i < maxResults; i++) {
        		JSONObject json = json_arr.getJSONObject(i);
        		//BirdSighting bs =  );
        		if (json != null ) {
        			BirdSighting bs = new BirdSighting(
        					json.getString("comName"), 
        					json.getString("sciName"), 
        					json.getString("obsDt"),
        					json.getString("locName"), 
        					json.getDouble("lat"),
        					json.getDouble("lng")	
        			);
        			
        			if (bs != null) 
        				bs_list.add( bs );
        			lats[i] = json.getDouble("lat");
        			lngs[i]  = json.getDouble("lng");
        			//lats.add(json.getDouble("lat"));
        			//lngs.add(json.getDouble("lng"));
        			//com_names[i] = json.getString("comName");
        			com_names.add(json.getString("comName"));
        			snippets[i] = json.getString("comName") + " spotted " + json.getString("obsDt") + " at " + json.getString("locName");			
        		}
        	}

        } catch (JSONException e) {
        	Log.e("JSON", "There was an error parsing the JSON", e);
        }
    	
    }
    
    private class DownloadDataTask extends AsyncTask<String, Void, Void> {
    	private final ProgressDialog dialog = new ProgressDialog(NearbyBirdsActivity.this);

    	// can use UI thread here
    	protected void onPreExecute() {
    		this.dialog.setMessage("Downloading bird sightings...");
    		this.dialog.show();
    	}

    	// automatically done on worker thread (separate from UI thread)
    	protected Void doInBackground(final String... args) {
    		// do JSON stuff
    		getRestStuff(args[0]);
    		return null;
    	}

    	// can use UI thread here
    	protected void onPostExecute(final Void unused) {
    		if (this.dialog.isShowing()) {
    			this.dialog.dismiss();
    			updateList();
    		}
    	}
    }

    private String queryRESTurl(String url) {
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpget = new HttpGet(url);
    	HttpResponse response;
    	
    	try {
    		response = httpclient.execute(httpget);
    		Log.i(TAG, "Status:[" + response.getStatusLine().toString() + "]");
    		HttpEntity entity = response.getEntity();
    		
    		if (entity != null) {
    			
    			InputStream instream = entity.getContent();
    			String result = RestClient.convertStreamToString(instream);
    			Log.i(TAG, "Result of converstion: [" + result + "]");
    			
    			instream.close();
    			return result;
    		}
    	} catch (ClientProtocolException e) {
    		Log.e("REST", "There was a protocol based error", e);
    	} catch (IOException e) {
    		Log.e("REST", "There was an IO Stream related error", e);
    	}
    	
    	return null;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem mapm = menu.add(0, MAP_ID, 0, "View Map");
        mapm.setIcon(android.R.drawable.ic_menu_mapmode);
        MenuItem homem = menu.add(0, HOME_ID, 0, "Main");
        homem.setIcon(R.drawable.ic_menu_home);
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MAP_ID:
           	Intent i = new Intent(this, BirdMap.class);
        	i.putExtra("lats",lats);
        	i.putExtra("lngs",lngs);
        	i.putExtra("markerTitles", com_names.toArray(new String[0]) );
        	i.putExtra("snippets", snippets);
 			startActivity(i);
            return true;
         case HOME_ID:
			Intent i2 = new Intent(this, birdMateActivity.class);
			startActivity(i2);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void redoMap(String newUrl) {
    	new DownloadDataTask().execute(newUrl);
    }
}

