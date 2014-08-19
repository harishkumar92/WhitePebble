package com.blackrock.whitepebbleconsumer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private CharSequence[] tickers;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("blah2", "Just started app!");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fixedmenu);
		
		ImageView perfChart = (ImageView) findViewById(R.id.perfchart);
		perfChart.setImageResource(R.drawable.performance);
		ImageView sectorChart = (ImageView) findViewById(R.id.sector_weights_imgview);
		sectorChart.setImageResource(R.drawable.sector_weightage);
		
		TableLayout holdings = (TableLayout) findViewById(R.id.holdings);
		int num_rows = holdings.getChildCount();
		TextView gain, ticker;
		tickers = new String[num_rows / 2];
		for (int i=2;i<num_rows;i+=2) {
			TableRow row = (TableRow) holdings.getChildAt(i);
			gain = (TextView) row.getChildAt(2);
			ticker = (TextView) (row.getChildAt(0));
			tickers[(i / 2) - 1] = ticker.getText().toString();

			if (gain.getText().toString().contains("-")) {
				gain.setBackgroundColor(Color.RED);
			} else {
				gain.setBackgroundColor(Color.GREEN);
			}	
		}
		
		ImageButton RSSButton  = (ImageButton) findViewById(R.id.recentNewsButton);
		RSSButton.setOnClickListener(new View.OnClickListener() {
			 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), RSSDisplay.class); 
                nextScreen.putExtra("holdings", tickers);
 
                Log.e("RSSOnClick", "swtiching activity");
                startActivity(nextScreen);
 
            }
        });
		
	}

	
	
	
	
	
	public void displayPerformance(View view) {

		
	}
	
	
	public void displayRSS(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), RSSDisplay.class); 
		nextScreen.putExtra("holdings", tickers);
		startActivity(nextScreen);
	}
	
	public void displayTeam(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), TeamActivity.class);
		nextScreen.putExtra("holdings", tickers);
		startActivity(nextScreen);		
	}
	
	public void displayOutlook(View view) {
		Intent nextScreeen = new Intent(getApplicationContext(), EconomicOutlookActivity.class);
		nextScreeen.putExtra("holdings", tickers);
		startActivity(nextScreeen);
		
	}
	
}
