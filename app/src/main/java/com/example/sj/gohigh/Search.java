package com.example.sj.gohigh;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Search extends AppCompatActivity {
    EditText Search;
    Button SearchButton;
    WebView SearchWebView;
    TextView average;
    String sendMsg,receiveMsg,str,result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Search = (EditText)findViewById(R.id.search_text);
        SearchButton = (Button)findViewById(R.id.search_button);
        SearchWebView = (WebView)findViewById(R.id.searchweb);
        average = (TextView)findViewById(R.id.averageprice);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchstr = Search.getText().toString();

                SearchWebView.setWebViewClient(new WebViewClient());

                try {
                    Search.CustomTask task = new Search.CustomTask();
                    result = task.execute(searchstr).get();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                WebSettings webSettings = SearchWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                SearchWebView.loadUrl("http://shopping.naver.com/search/all.nhn?query=" + searchstr + "&cat_id=&frm=NVSHATC");

                average.setText(receiveMsg+"원");

            }
        });

    }
    class CustomTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {

            try {



                URL url = new URL("http://192.168.0.55:8181/test/Naverprice.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "searchstr="+strings[0];
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
