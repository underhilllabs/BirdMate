package com.underhilllabs.birdmate;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	private ArrayList<OverlayItem> items;
	private Context mContext;
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		items = new ArrayList<OverlayItem>();
		populate();
	}	

	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = items.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return items.get(i);
	}
	public void addNewItem(GeoPoint location, String markerText, String snippet) {
		items.add(new OverlayItem(location, markerText, snippet));
		populate();
	}

	public void removeItem(int index) {
		items.remove(index);
		populate();
	}

	@Override
	public int size() {
		return items.size();
	}

}
