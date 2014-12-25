package app.rumus.ppdb;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.rumus.ppdb.lib.DatabaseHandler;
import app.rumus.ppdb.lib.JSONParser;
import app.rumus.ppdb.lib.ListCodeAdapter;
import app.rumus.ppdb.lib.SessionHandler;
import app.rumus.ppdb.model.CodeModel;

public class MainActivity extends Activity {
	private ImageButton btnScan;
	private TextView textNama,textEmail;
	private JSONParser jsonParser = new JSONParser();
	private DatabaseHandler dh;
	private ListCodeAdapter lca;
	private ProgressDialog pDialog;
	private SessionHandler sh;
	private ListView lvCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnScan = (ImageButton) findViewById(R.id.btnScan);
		textNama = (TextView)findViewById(R.id.textNama);
		lvCode = (ListView)findViewById(R.id.listView1);
		textEmail = (TextView)findViewById(R.id.textEmail);
		dh = new DatabaseHandler(getApplicationContext());
		
		lca = new ListCodeAdapter(this, dh.getAllCode());
		lvCode.setAdapter(lca);
		sh = new SessionHandler(getApplicationContext());

		// get user data from session
		HashMap<String, String> user = sh.getUserDetails();
		
		
		textNama.setText(user.get(SessionHandler.KEY_NAMA));
		textEmail.setText(user.get(SessionHandler.KEY_EMAIL));
		
		btnScan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				new sendData(contents).execute();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				dh.addCode(new CodeModel(contents,  dateFormat.format(date)));
				lca = new ListCodeAdapter(this, dh.getAllCode());
				lvCode.setAdapter(lca);
			} else if (resultCode == RESULT_CANCELED) {
				// CANCEL
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

	class sendData extends AsyncTask<Integer, String, Integer> {
		String content;
		int errorcode;

		public sendData(String content) {
			// TODO Auto-generated constructor stub
			this.content = content;
		}

		@Override
		protected Integer doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			if (content != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("idnya", content));

				JSONObject json = jsonParser.makeHttpRequest(
						"http://project.ramazeta.web.id/ppdb/api/index.php?act=check", "POST", params);

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
			if (errorcode != 0) {
				Toast.makeText(getApplicationContext(), "ERROR!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Data telah masuk kedalam database", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}
}
