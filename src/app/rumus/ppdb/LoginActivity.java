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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.rumus.ppdb.lib.JSONParser;
import app.rumus.ppdb.lib.SessionHandler;

public class LoginActivity extends Activity {
	private JSONParser jsonParser = new JSONParser();
	private JSONObject json;
	private ProgressDialog pDialog;
	private SessionHandler sh;
	private EditText textEmail, textPassword;
	private Button btnLogin;
	private String session, nama, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		textEmail = (EditText) findViewById(R.id.textEmail);
		textPassword = (EditText) findViewById(R.id.textPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		sh = new SessionHandler(getApplicationContext());
		
		if(sh.isLoggedIn()){
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		}

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (textEmail.getText().toString().isEmpty()
						&& textPassword.getText().toString().isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Email dan Password belum diisi!",
							Toast.LENGTH_SHORT).show();
				} else {
					new ayoLogin().execute();
				}
			}
		});
	}

	class ayoLogin extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... args0) {
			// TODO Auto-generated method stub

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", textEmail.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", textPassword
					.getText().toString()));

			json = jsonParser
					.makeHttpRequest(
							"http://project.ramazeta.web.id/ppdb/api/index.php?act=login",
							"POST", params);

			try {
				JSONObject jsObj = json.getJSONObject("admin");
				session = jsObj.getString("session");
				email = jsObj.getString("email");
				nama = jsObj.getString("nama");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return session;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Attempting Login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if (result.equalsIgnoreCase("")) {
				Toast.makeText(getApplicationContext(), "Something Wrong",
						Toast.LENGTH_SHORT).show();
			} else {
				sh.createLoginSession(result, email, nama);
				Intent i = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}

	}

}
