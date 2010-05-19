package com.underhilllabs.birdmate;



import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class birdMateActivity extends ListActivity {
	public final String TAG = "BirdMate";
	private String [] appnames=  {
			"Find Nearby Birds",
			"Notable Sightings",
			"Locate a Bird",
			"Nearby Birding Hotspots"
			};
	private DbAdapter db;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        db = new DbAdapter(this);
        db.open();
        
        ListView lv = getListView();
        lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, appnames ));
        lv.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> av, View v, int position,
    				long id) {

				Intent i = null;
				if(position == 0)
					i = new Intent (birdMateActivity.this,NearbyBirdsActivity.class);
				else if (position == 1)
					i = new Intent (birdMateActivity.this,NearbyNotableBirdsActivity.class);
				else if (position == 2)
					i = new Intent (birdMateActivity.this,BirdSearchActivity.class);
				else if (position == 3)
					i = new Intent (birdMateActivity.this,NearbyBirdsActivity.class);
				
				//Intent i = new Intent(birdMateActivity.this, myClass);
		        startActivity(i);
    	 		 	
    		}
           });
           
        
    }



}

