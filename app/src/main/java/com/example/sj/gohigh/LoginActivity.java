package com.example.sj.gohigh;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
   String userid,userpw,checkpw,sendMsg, receiveMsg,str;
    EditText id,pw;
    TextView showtw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button data = (Button)findViewById(R.id.LoginButton);
        id=(EditText)findViewById(R.id.idText);
        pw=(EditText)findViewById(R.id.pwText);
        showtw=(TextView)findViewById(R.id.textView);

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    userid=id.getText().toString();
                    userpw=pw.getText().toString();


                    String result;
                    LoginActivity.CustomTask task = new LoginActivity.CustomTask();
                    result = task.execute(userid,userpw).get();
                    Log.i("리턴 값",result);



                    if(userpw.equalsIgnoreCase(checkpw)){
                        Intent intent= new Intent(LoginActivity.this,PlayerWait.class);
                        intent.putExtra("id",userid);
                        startActivity(intent);
                    }
                    if(!userpw.equalsIgnoreCase(checkpw))
                    {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호 올바르지않습니다.", Toast.LENGTH_SHORT).show();
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



                URL url = new URL("http://192.168.0.55:8181/test/NewFile.jsp");
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
                    checkpw=receiveMsg.trim();




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




    public void onRButtonClicked(View v){
        Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


}
