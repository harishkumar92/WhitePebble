package com.blackrock.whitepebbleconsumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TeamActivity extends Activity {

	private String[] tickers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.team_layout);
		
		Intent intent = getIntent();
		String[] tickers = (String[]) intent.getExtras().getCharSequenceArray("holdings");
		this.tickers = tickers;
	}
	
	
	public void displayPerformance(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(nextScreen);
	}
	
	
	public void displayRSS(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), RSSDisplay.class); 
		nextScreen.putExtra("holdings", tickers);
		startActivity(nextScreen);
	}
	
	public void displayTeam(View view) {
		
	}
	
	public void displayOutlook(View view) {
		
	}
	
	
}
