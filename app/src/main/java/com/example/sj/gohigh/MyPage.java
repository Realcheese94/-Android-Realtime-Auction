package com.example.sj.gohigh;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MyPage extends AppCompatActivity {
    ListView myListView;
    String receiveMsg,result,sendMsg;
    Button button;
    String str1;
    String strrr[];
    String id;
    URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        button = (Button)findViewById(R.id.MypageButton);
        myListView = (ListView) findViewById(R.id.mylistview);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   MyPage.CustomTask customTask = new MyPage.CustomTask();
                   result = customTask.execute(id).get();

                   str1=result.trim();
                    strrr=str1.split(";");

                    dataSetting();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });   //물건이름, 작성자,설명,이미지->
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l)
            {
                Toast toast = Toast.makeText(getApplicationContext()," 물건이름:"+strrr[i*5]+"이랑 작성자:"+strrr[i*5+1]+"채팅방에 입장합니다.....",Toast.LENGTH_SHORT);
                toast.show();
                Intent intent2 = new Intent(MyPage.this,ChatActivity.class);
                intent2.putExtra("pname",strrr[i*5]);
                intent2.putExtra("name",strrr[i*5+1]);
                intent2.putExtra("desc",strrr[i*5+2]);
                intent2.putExtra("image",strrr[i*5+3]);
                intent2.putExtra("price",strrr[i*5+4]);
                intent2.putExtra("id",id);
                startActivity(intent2);
            }
        });
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL("http://192.168.0.55:8181/test/Mypage.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str1 = reader.readLine()) != null) {
                        buffer.append(str1);
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
    private  void dataSetting()
    {
        MyPageAdapter mMyAdapter = new MyPageAdapter();
        for(int j=0;j<strrr.length;j+=5){
            mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu_camera),"물건이름_"+strrr[j],"작성자_"+strrr[j+1]);
        }
        myListView.setAdapter(mMyAdapter);
    }
}