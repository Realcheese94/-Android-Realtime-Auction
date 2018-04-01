package com.example.sj.gohigh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class RegisterActivity extends AppCompatActivity {

    String userid,userpw,sendMsg, receiveMsg;
    EditText id,pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .permitDiskReads()
        .permitDiskWrites()
        .permitNetwork().build());
        setContentView(R.layout.activity_register1);


        Button data = (Button)findViewById(R.id.Signupbutton);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    id=(EditText)findViewById(R.id.idText);
                    userid=id.getText().toString();

                    pw=(EditText)findViewById(R.id.pwText);
                    userpw=pw.getText().toString();






                    String result;
                    CustomTask task = new CustomTask();
                    result = task.execute(userid,userpw).get();
                    Log.i("리턴 값",result);

                    if(receiveMsg.trim().equals("sc")) {
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if(receiveMsg.trim().equals("fail")){
                        Toast.makeText(getApplicationContext(), "아이디는 중복 될수없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    class CustomTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {

            try {

                String str;

                URL url = new URL("http://192.168.0.55:8181/test/login.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }


}
