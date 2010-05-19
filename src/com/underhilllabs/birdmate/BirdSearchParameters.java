package com.underhilllabs.birdmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class BirdSearchParameters extends Activity {
	private Button searchButton;
	private RadioGroup rg_all_or_notable;
	private EditText tvMaxResults;
	private EditText tvDistance;
	private EditText tvDaysBack;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_search_parameters);
        searchButton = (Button)findViewById(R.id.searchButton);
        rg_all_or_notable = (RadioGroup)findViewById(R.id.rg_all_notable);
        tvMaxResults = (EditText)findViewById(R.id.etMaxResults);
        tvDistance = (EditText)findViewById(R.id.etDistance);
        tvDaysBack = (EditText)findViewById(R.id.etDaysBack);
        
    
        searchButton.setOnClickListener(new Button.OnClickListener() {
          String all_notable = "";
          public void onClick(View v) {
            // Perform action on clicks
            switch (rg_all_or_notable.getCheckedRadioButtonId()) {
            case R.id.rb_notable:
                all_notable = "notable";
                break;
            case R.id.rb_all:
                all_notable = "obs";
                break;
            }
            
            Intent i = new Intent(getApplicationContext(),NearbyNotableBirdsActivity.class);
            i.putExtra("distance",Integer.parseInt(tvDistance.getText().toString()));
            i.putExtra("max_results",Integer.parseInt(tvMaxResults.getText().toString()));
            i.putExtra("days_back",Integer.parseInt(tvDaysBack.getText().toString()));
            i.putExtra("all_notable", all_notable);
            startActivity(i);
            

        }
      });
    }

}
