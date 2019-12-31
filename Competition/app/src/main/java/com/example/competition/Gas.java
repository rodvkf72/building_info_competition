package com.example.competition;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

public class Gas extends Fragment {
    View view;

    String bpk = "";
    String fname = "";
    String add = "";
    String lchk = "";
    String useyn = "";

    TextView tv1, tv2, tv3, tv4;

    String param1 = "";
    String param2 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.gas, container, false);

        param1 = getArguments().getString("param1");
        param2 = getArguments().getString("param2");

        tv1 = (TextView) view.findViewById(R.id.gas_textView1);
        tv2 = (TextView) view.findViewById(R.id.gas_textView2);
        tv3 = (TextView) view.findViewById(R.id.gas_textView3);
        tv4 = (TextView) view.findViewById(R.id.gas_textView4);

        Gas.ContentDB CDB = new Gas.ContentDB();
        CDB.execute();
        return view;
    }

    public class ContentDB extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            // 인풋 파라메터값 생성
            String param = "u_buildingpk=" + param2 + "";
            Log.e("POST",param);
            try {
                // 서버연결
                URL url = new URL("http://192.168.35.199/building_gas_chk.php");
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
                    bpk = content.getString("buildingpk");
                    fname = content.getString("fclty_nm");
                    add = content.getString("addr");
                    lchk = content.getString("last_inspct_day");
                    useyn = content.getString("use_yn");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            tv1.setText(fname);
            tv2.setText(add);
            tv3.setText(lchk);
            tv4.setText(useyn);
        }
    }
}
