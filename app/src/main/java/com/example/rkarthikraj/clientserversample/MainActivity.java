package com.example.rkarthikraj.clientserversample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText getID, getName, getPlace;
    String ID, Name, Place;
    TextView putName,putPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getID = (EditText) findViewById(R.id.getID);
        getName = (EditText) findViewById(R.id.getName);
        getPlace = (EditText) findViewById(R.id.getPlace);
        putName = (TextView) findViewById(R.id.putName);
        putPlace = (TextView) findViewById(R.id.putPlace);
    }

    public void onClickInsert(View v) {

        Name = getName.getText().toString();
        Place = getPlace.getText().toString();
        AsyncTaskDB runnerINS = new AsyncTaskDB();
        runnerINS.execute("", Name, Place, "ins");
    }

    public void onClickUpdate(View v) {
        ID = getID.getText().toString();
        Name = getName.getText().toString();
        Place = getPlace.getText().toString();
        AsyncTaskDB runnerUPD = new AsyncTaskDB();
        runnerUPD.execute(ID, Name, Place,"upd");

    }

    public void onClickDelete(View v) {
        ID = getID.getText().toString();
        AsyncTaskDB runnerDEL = new AsyncTaskDB();
        runnerDEL.execute(ID,"","","del");
    }

    public void onClickSelect(View v) {
        ID = getID.getText().toString();
        AsyncTaskDB runnerSEL = new AsyncTaskDB();
        runnerSEL.execute(ID,"","","sel");
    }

    public void onClickClear(View v) {
        getID.setText("");
        getName.setText("");
        getPlace.setText("");
    }

//ASYNC TASK

    class AsyncTaskDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String ID = params[0];
            String NAME = params[1];
            String PLACE = params[2];
            String TYPE = params[3];
            int tmp;

            try {
                String data="";
                URL url = new URL("http://192.168.1.213/androphp.php/?id=" + ID + "&name=" + NAME + "&place=" + PLACE + "&type=" + TYPE);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write("".getBytes());
                InputStream is = httpURLConnection.getInputStream();
                os.flush();
                os.close();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, "Operation Successful.", Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String name = jsonobject.getString("name");
                    String place = jsonobject.getString("place");
                    putName.setText(name);
                    putPlace.setText(place);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}