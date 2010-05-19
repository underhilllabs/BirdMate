package com.underhilllabs.birdmate;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BirdSightingView extends Activity {
	private String com_name, sci_name, date, locId, snippet;
	private Double lat, lng;
	private TextView tvComName, tvSciName, tvDate, tvLocId, tvLat, tvLng;
	private static final int MAP_ID = Menu.FIRST;
	private static final int HOME_ID = Menu.FIRST+1;
	private static final int MAP2_ID = Menu.FIRST+2;
	private static final int ZOOM_LEVEL = 15;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_sighting_view);
        
        tvComName = (TextView) findViewById(R.id.com_name);
    	tvSciName = (TextView) findViewById(R.id.sci_name);
    	tvDate = (TextView) findViewById(R.id.date);
    	tvLocId = (TextView) findViewById(R.id.loc_id);
    	tvLat = (TextView) findViewById(R.id.latitude);
    	tvLng = (TextView) findViewById(R.id.lng);
    	
        Bundle extras = getIntent().getExtras();
        //com.underhilllabs.birdmate.com_name
       if(!extras.isEmpty()) {
        com_name = extras.getString("com_name");
        sci_name = extras.getString("sci_name");
        date = extras.getString("date");
        locId = extras.getString("locId");
        lat = extras.getDouble("lat");
        lng = extras.getDouble("lng");
        Log.d("birdmate bsview", "com name is "+com_name);
        snippet = com_name + "spotted at " + locId + ", " + date;
        //tvComName.setText("hello");
        tvComName.setText(com_name);
        tvSciName.setText(sci_name);
        tvDate.setText(date);
        tvLocId.setText(locId);
        tvLat.setText(lat.toString());
        tvLng.setText(lng.toString());
       }
        
       
    
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem mapm = menu.add(0, MAP_ID, 0, "View Map");
        mapm.setIcon(android.R.drawable.ic_menu_mapmode);
        MenuItem homem = menu.add(0, HOME_ID, 0, "Main");
        homem.setIcon(R.drawable.ic_menu_home);
        //MenuItem map2m = menu.add(0,MAP2_ID, 0, "View Markers");
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        //case MAP_ID:
        	//String myMapUri = "geo:"+lat+","+lng;
        	//Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:"+lat+","+lng+"?z="+ZOOM_LEVEL));
        	//startActivity(myIntent);
            //return true;
         case HOME_ID:
			Intent i2 = new Intent(this, birdMateActivity.class);
			startActivity(i2);
        	return true;
         case MAP_ID:
        	Intent i3 = new Intent(this, BirdMap.class);
        	i3.putExtra("lats",new double[] {lat});
        	i3.putExtra("lngs",new double[] {lng});
        	i3.putExtra("markerTitles", new String [] {com_name} );
        	i3.putExtra("snippets", new String[] {snippet});
 			startActivity(i3);
         	return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
