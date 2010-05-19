package com.underhilllabs.birdmate;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BirdSearchActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText etBirdName;
	private Button searchButton;
	private DbAdapter bdb;
	private Cursor cur;
	private String [] com_names;
	private String [] sci_names;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_search);
        etBirdName = (EditText)findViewById(R.id.etBirdName);
        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            String all_notable = "";
            public void onClick(View v) {
              // Perform action on clicks
              //Intent i = new Intent(BirdSearchActivity.this, BirdSearchResults.class);
              //i.putExtra("search")
		      //startActivity(i);
              bdb= new DbAdapter(BirdSearchActivity.this);
              bdb.open();
              String search_str = etBirdName.getText().toString();
              Log.i("BirdMate","sent: "+search_str);
              Cursor cur = bdb.searchBird(search_str);
              //String [] results = cur.
              int cnt = cur.getCount();
   
              com_names = new String[cnt];
              sci_names = new String[cnt];
              int i = 0;
              if(cnt > 0) {
                do {
            	  Log.i("BirdMate","returned: "+cur.getString(1)+" "+cur.getString(2));
            	  com_names[i] = cur.getString(1);
            	  sci_names[i] = cur.getString(2);
            	  i++;
                } while (cur.moveToNext());
              
                AlertDialog.Builder builder = new AlertDialog.Builder(BirdSearchActivity.this);
                builder.setTitle("Select Bird to Locate");
                builder.setItems(com_names, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int item) {
                      Toast.makeText(getApplicationContext(), sci_names[item], Toast.LENGTH_SHORT).show();
                      Intent i = new Intent(BirdSearchActivity.this, NearbyNotableBirdsActivity.class);
                      i.putExtra("sci_name",sci_names[item]);
                      startActivity(i);
                  }
                });
                AlertDialog alert = builder.create();
                alert.show();
              } else {
            	  Toast.makeText(getApplicationContext(), "No results for "+search_str+". Try again.", Toast.LENGTH_SHORT).show();
            	  		
              }
    		}
        });
    }
    
    
}
