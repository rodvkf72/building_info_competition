package com.example.competition;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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


public class Contents extends AppCompatActivity implements View.OnClickListener{
    TextView tvname, tvgrade;

    String bname = "";
    String buildingpk = "";
    String bgrade = "";

    FragmentManager fm;
    FragmentTransaction tran;

    Gas fr1;
    Electric fr2;
    Lift fr3;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents);

        Intent intent = getIntent();
        bname = intent.getStringExtra("buildingname");
        buildingpk = intent.getStringExtra("buildingpk");

        GradeDB GDB = new GradeDB();
        GDB.execute();

        tvname = (TextView)findViewById(R.id.textView);
        tvname.setText(bname);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);

        ImageView iv = (ImageView) findViewById(R.id.contents_home);
        iv.setImageResource(R.mipmap.home);

        fr1 = new Gas();
        fr2 = new Electric();
        fr3 = new Lift();

        bundle = new Bundle();

        setFrag(0);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contents.this, Choose.class);
                startActivity(intent);
                overridePendingTransition(R.anim.topin_activity, R.anim.bottomout_activity);
            }
        });
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.button1:
                setFrag(0);
                break;
            case R.id.button2:
                setFrag(1);
                break;
            case R.id.button3:
                setFrag(2);
                break;
        }
    }
    public void setFrag(int n){
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        switch(n){
            case 0:
                bundle.putString("param1", bname);
                bundle.putString("param2", buildingpk);
                fr1.setArguments(bundle);
                tran.replace(R.id.frame, fr1);
                tran.commit();
                break;
            case 1:
                bundle.putString("param1", bname);
                bundle.putString("param2", buildingpk);
                fr2.setArguments(bundle);
                tran.replace(R.id.frame, fr2);
                tran.commit();
                break;
            case 2:
                bundle.putString("param1", bname);
                bundle.putString("param2", buildingpk);
                fr3.setArguments(bundle);
                tran.replace(R.id.frame, fr3);
                tran.commit();
                break;
        }
    }
    public class GradeDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_buildingpk=" + buildingpk + "";
            Log.e("POST", param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.100/building_grade.php");
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
            try{
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    bgrade = content.getString("grp_grade");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            tvgrade = (TextView)findViewById(R.id.buildinggrade);
            tvgrade.setText(bgrade);
        }
    }
}
