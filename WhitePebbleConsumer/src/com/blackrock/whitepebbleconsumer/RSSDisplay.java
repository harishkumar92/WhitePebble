package com.blackrock.whitepebbleconsumer;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.shirwa.simplistic_rss.RssHandler;
import com.shirwa.simplistic_rss.RssItem;
import com.shirwa.simplistic_rss.RssReader;

public class RSSDisplay extends ActionBarActivity {
	private String rssURL = "http://news.google.com/news";
	//String rssURL = "http://news.google.com/news?q=;
	//q=MSFT+OR+GOOG+OR+AAPL

	ArrayAdapter<String> adapter;
	private ListView mList;

	private CharSequence[] holdings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("RSS", "just switched to RSSdisplay");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rssdisplay);
		Intent intent = getIntent();
		String query = "?q=";
		String[] tickers = (String[]) intent.getExtras().getCharSequenceArray("holdings");
		this.holdings = tickers;
		for (int i=0;i<tickers.length;i++) {
			if (i == (tickers.length - 1)) {
				query = query + tickers[i];
			} else {
				query = query + tickers[i] + "+OR+";
			}

		}
		Log.d("making custom query", rssURL +  query + "&output=RSS");
		
				
        mList = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
        new GetRssFeed().execute("http://news.google.com/news?q=Amazon+OR+Dillards+OR+DFS+DNKN+OR+GPS&output=RSS");
		
	}
	
	public void displayPerformance(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(nextScreen);
	}

	public void displayRSS(View view) {

	}
	

	
	public void displayTeam(View view) {
		Intent nextScreen = new Intent(getApplicationContext(), TeamActivity.class);
		nextScreen.putExtra("holdings", holdings);
		startActivity(nextScreen);		
	}
	
	public void displayOutlook(View view) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rssdisplay, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

    private class GetRssFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                RssReader rssReader = new RssReader(params[0]);
                for (RssItem item : rssReader.getItems()) {
                    adapter.add(item.getTitle());
                }

            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            mList.setAdapter(adapter);            
        }
        
    }
}
