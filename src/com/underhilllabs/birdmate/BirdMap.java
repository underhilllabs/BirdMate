package com.underhilllabs.birdmate;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class BirdMap extends MapActivity {
	private MapView mapView;
	private MapController mapController;
	private String [] markerTitles;
	private String [] snippets;
	private double [] lats;
	private double [] lngs;
	List<Overlay> mapOverlays;
	Drawable drawable;
	MyItemizedOverlay itemizedOverlay;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_map);
		mapView = (MapView) findViewById(R.id.my_map);
		mapController = mapView.getController();
	   
		Bundle extras = getIntent().getExtras();
		if(!extras.isEmpty()) {
			lats = extras.getDoubleArray("lats");
			lngs = extras.getDoubleArray("lngs");
			markerTitles = extras.getStringArray("markerTitles");
			snippets = extras.getStringArray("snippets");
		}
		
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.emo_im_wtf);
		itemizedOverlay = new MyItemizedOverlay(drawable,BirdMap.this);
		
		populateMarkers(lats, lngs, markerTitles, snippets);
		Log.d("BirdMap","number of markers: "+itemizedOverlay.size());
		mapView.setBuiltInZoomControls(true);
		//mapController.setZoom(15);
		//mapController.animateTo(point)
	}
	private void populateMarkers(double []lats, double[] lngs, String[] markerTitles, String[] snippets) {
		GeoPoint point = null;
		List<GeoPoint> points = new ArrayList<GeoPoint>();
		for (int i=0; i < lats.length; i++) {
			if(lats[i]<1) {
				//
			} else {
				//Log.d("BirdMap","com_names: "+markerTitles[i]);
				Double mylat = lats[i]*1E6;
				Double mylng = lngs[i]*1E6;
				point = new GeoPoint(mylat.intValue(), mylng.intValue());
				//OverlayItem overlayitem = new OverlayItem(point, markerTitles[i], "");
				itemizedOverlay.addNewItem(point,markerTitles[i],snippets[i]);
				mapOverlays.add(itemizedOverlay);
				points.add(point);
				
			}
		}
		
		// this section sets the map to zoom in or out to fit all markers inside
		// it also centers the map in the center of all the markers
		int minLat = points.get(0).getLatitudeE6();
		int minLong = points.get(0).getLongitudeE6();
		int maxLat = points.get(0).getLatitudeE6();
		int maxLong = points.get(0).getLongitudeE6();
		for (GeoPoint l: points) {
		    minLat  = Math.min( l.getLatitudeE6(), minLat );
		    minLong = Math.min( l.getLongitudeE6(), minLong);
		    maxLat  = Math.max( l.getLatitudeE6(), maxLat );
		    maxLong = Math.max( l.getLongitudeE6(), maxLong );
		}
		
		//mapController.animateTo(point);
        mapController.animateTo(new GeoPoint(
                (maxLat + minLat)/2,
                (maxLong + minLong)/2 )); 
		//mapController.setZoom(14);
		mapController.zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
		//mapController.setZoom(14);
		
	}
	
	
}
