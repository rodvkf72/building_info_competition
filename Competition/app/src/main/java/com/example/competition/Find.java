package com.example.competition;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.Arrays;

public class Find extends AppCompatActivity {

    ArrayList<String> GU_MENU;
    ArrayList<String> DONG_MENU;
    ArrayList<String> BUILDING_MENU = new ArrayList<String>();

    String building_Text = "";
    String building_name = "";
    String buildingpk = "";
    String strct = "";
    String purps = "";
    String roof = "";
    String heit = "";
    String grnd_flr_cnt = "";
    String ugrnd_flr_cnt = "";

    String gu_Text = "";
    String dong_Text = "";

    ListView gu_listview;
    ListView dong_listview;
    ListView building_listview;

    ImageView mapview;

    ArrayAdapter gu_adapter;
    ArrayAdapter dong_adapter;
    ArrayAdapter building_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);

        GU_MENU= new ArrayList<String>(Arrays.asList("마포구"));
        DONG_MENU = new ArrayList<String>(Arrays.asList("공덕동", "도화동", "마포동", "신공덕동", "염리동", "용강동"));

        gu_adapter = new ArrayAdapter(this, R.layout.simpleitem, GU_MENU);
        dong_adapter = new ArrayAdapter(this, R.layout.simpleitem, DONG_MENU);
        building_adapter = new ArrayAdapter(this, R.layout.simpleitem, BUILDING_MENU);

        gu_listview = (ListView) findViewById(R.id.find_gu);
        dong_listview = (ListView) findViewById(R.id.find_dong);
        building_listview = (ListView) findViewById(R.id.find_building);

        mapview = (ImageView) findViewById(R.id.find_map);
        mapview.setImageResource(R.drawable.greenmapoline);

        gu_listview.setAdapter(gu_adapter);

        gu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                gu_Text = (String) parent.getItemAtPosition(position);
                mapview.setImageResource(R.drawable.greenmapoline);
                dong_listview.setAdapter(dong_adapter);
            }
        });
        dong_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                dong_Text = (String) parent.getItemAtPosition(position);
                if (dong_Text.equals("공덕동")){
                    mapview.setImageResource(R.drawable.gongduk);
                }
                else if (dong_Text.equals("도화동")){
                    mapview.setImageResource(R.drawable.dohwa);
                }
                else if (dong_Text.equals("마포동")){
                    mapview.setImageResource(R.drawable.mapo);
                }
                else if (dong_Text.equals("신공덕동")){
                    mapview.setImageResource(R.drawable.singongduk);
                }
                else if (dong_Text.equals("염리동")){
                    mapview.setImageResource(R.drawable.yeomli);
                }
                else if (dong_Text.equals("용강동")){
                    mapview.setImageResource(R.drawable.yongkang);
                }
                else {
                    mapview.setImageResource(R.drawable.greenmapoline);
                }
                FindDB FDB = new FindDB();
                FDB.execute();
            }
        });
        building_listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                building_Text = (String) parent.getItemAtPosition(position);
                FindCheckDB FCDB = new FindCheckDB();
                FCDB.execute();
            }
        });
    }

    public class FindCheckDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_buildingname=" + building_Text + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.100/building_find.php");
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
                while ( ( line = in.readLine() ) != null )
                {
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

        @Override
        protected void onPostExecute(String data) {
            try{
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    buildingpk = content.getString("buildingpk");
                    strct = content.getString("etc_strct");
                    purps = content.getString("etc_purps");
                    roof = content.getString("etc_roof");
                    heit = content.getString("heit");
                    grnd_flr_cnt = content.getString("grnd_flr_cnt");
                    ugrnd_flr_cnt = content.getString("ugrnd_flr_cnt");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(Find.this, Buildingcontents.class);
            intent.putExtra("buildingname", building_Text);
            intent.putExtra("buildingpk", buildingpk);
            intent.putExtra("strct", strct);
            intent.putExtra("purps", purps);
            intent.putExtra("roof", roof);
            intent.putExtra("heit", heit);
            intent.putExtra("grnd_flr_cnt", grnd_flr_cnt);
            intent.putExtra("ugrnd_flr_cnt", ugrnd_flr_cnt);
            startActivity(intent);
            overridePendingTransition(R.anim.alphain_activity, R.anim.alphaout_activity);
        }
    }

    public class FindDB extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_dong=" + dong_Text + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.0.100/building_select.php");
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
                while ( ( line = in.readLine() ) != null )
                {
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

        @Override
        protected void onPostExecute(String data) {
            try{
                BUILDING_MENU.clear();
                JSONObject root = new JSONObject(data);
                JSONArray results = new JSONArray(root.getString("results"));
                for (int i = 0; i < results.length(); i++){
                    JSONObject content = results.getJSONObject(i);
                    building_name = content.getString("buildingname");

                    BUILDING_MENU.add(building_name);
                    //building_adapter.notifyDataSetChanged();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            building_listview.setAdapter(building_adapter);
        }
    }
}
