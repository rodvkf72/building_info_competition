package com.example.competition;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Insert extends AppCompatActivity {

    EditText et1, et2, et3, et4, et5;
    Button btn;

    String pk = "";
    String dong = "";
    String main = "";
    String sub = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert);

        et1 = (EditText)findViewById(R.id.et_buildingpk);
        et2 = (EditText)findViewById(R.id.et_dong);
        et3 = (EditText)findViewById(R.id.et_main);
        et4 = (EditText)findViewById(R.id.et_sub);
        et5 = (EditText)findViewById(R.id.et_buildingname);

        btn = (Button)findViewById(R.id.name_insert);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pk = et1.getText().toString();
                dong = et2.getText().toString();
                main = et3.getText().toString();
                sub = et4.getText().toString();
                name = et5.getText().toString();

                InsertDB IDB = new InsertDB();
                IDB.execute();
            }
        });
    }

    public class InsertDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_buildingpk=" + pk + "" + "&u_dong=" + dong + "" + "&u_main=" + main + "" + "&u_sub=" + sub + "" + "&u_name=" + name + "";
            Log.e("POST", param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.100/building_name_insert.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                // 안드로이드 -> 서버 파라메터값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                // 서버 -> 안드로이드 파라메터값 전달
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                // 서버에서 응답
                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String data) {
            if(data.equals("insert")) {
                Toast.makeText(getApplicationContext(), "데이터 삽입 완료.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "삽입 데이터 확인 및 데이터 중복을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
