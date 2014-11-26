package app.rumus.ppdb;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.rumus.ppdb.lib.JSONParser;


public class MainActivity extends Activity {
	private Button btn1;
	private TextView txt1,txt2;
	private JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn1 = (Button)findViewById(R.id.scan_button);
        txt1 = (TextView)findViewById(R.id.scan_format);
        txt2 = (TextView)findViewById(R.id.scan_content);
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		});
    }
    
    

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 0){
			if(resultCode == RESULT_OK){
				String contents = data.getStringExtra("SCAN_RESULT");
				String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				txt1.setText(contents);
				txt2.setText(format);
			} else if(resultCode == RESULT_CANCELED){
				//CANCEL
			}
		}
	}



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    class sendData extends AsyncTask<Integer, String, Integer>{
    	String content;
    	String format;
    	int errorcode;
    	
    	public sendData(String content,String format) {
			// TODO Auto-generated constructor stub
    		this.content = content;
    		this.format = format;
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			if(content != null){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("idnya", content));
				
				JSONObject json = jsonParser.makeHttpRequest("http://localhost/asem/", "POST", params);
				
				try {
					errorcode = json.getInt("errorcode");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return errorcode;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
		    pDialog.setMessage("Attempting Login...");
		    pDialog.setIndeterminate(false);
		    pDialog.setCancelable(true);
		    pDialog.show();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			super.onPostExecute(result);
			if(errorcode != 0){
				Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Data telah masuk kedalam database", Toast.LENGTH_SHORT).show();
			}
		}
    	
    }
}
