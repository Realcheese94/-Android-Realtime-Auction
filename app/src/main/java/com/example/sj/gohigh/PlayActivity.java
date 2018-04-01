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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class PlayActivity extends AppCompatActivity {
    ListView mListView;
    String receiveMsg,result;
    Button button,searchbutton;
    String str,searchstr;
    String strr[];
    EditText searchtext;
    String id;
    URL url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        button = (Button)findViewById(R.id.button2);
        mListView = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlayActivity.CustomTask customTask = new PlayActivity.CustomTask();
                    result = customTask.execute().get();
                    str=receiveMsg.trim();
                    strr=str.split(";");
                    dataSetting();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });   //물건이름, 작성자,설명,이미지->
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
                Toast toast = Toast.makeText(getApplicationContext()," 물건이름"+strr[i*5]+"이랑 작성자"+strr[i*5+1],Toast.LENGTH_SHORT);
                toast.show();
                Intent intent2 = new Intent(PlayActivity.this,ShowDetailProduct.class);
                intent2.putExtra("pname",strr[i*5]);
                intent2.putExtra("name",strr[i*5+1]);
                intent2.putExtra("desc",strr[i*5+2]);
                intent2.putExtra("image",strr[i*5+3]);
                intent2.putExtra("price",strr[i*5+4]);
                intent2.putExtra("id",id);
                startActivity(intent2);
            }
        });


    }



    class CustomTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            try {



                url = new URL("http://192.168.0.55:8181/test/ShowProduct.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
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
    private  void dataSetting()
    {

        MyAdapter mMyAdapter = new MyAdapter();
        for(int j=0;j<strr.length;j+=5){
            mMyAdapter.addItem(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_menu_camera),"물건이름_"+strr[j],"작성자_"+strr[j+1]);
        }
        mListView.setAdapter(mMyAdapter);
    }



}