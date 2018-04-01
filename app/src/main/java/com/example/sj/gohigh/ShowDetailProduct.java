package com.example.sj.gohigh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowDetailProduct extends AppCompatActivity {
    String pname, name, receiveMsg, sendMsg, str, result,pricee;
    TextView title,desc,highprice;
    ImageView img;
    EditText price;
    Button buy,back;
    StorageReference mStorageRef;
    String id;
    String nowprice;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_product);
        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        img = (ImageView) findViewById(R.id.img);
        highprice = (TextView) findViewById(R.id.highprice);
        price = (EditText) findViewById(R.id.priceText);
        buy = (Button) findViewById(R.id.BuyButton);
        back = (Button) findViewById(R.id.BackButton);
        pname = intent.getStringExtra("pname"); //물건이름
        name = intent.getStringExtra("name");  //작성자
        String descsrc = intent.getStringExtra("desc");
        String imgsrc = intent.getStringExtra("image");
       id = intent.getStringExtra("id");
        pricee = intent.getStringExtra("price");
        title.setText(pname);
        desc.setText(descsrc);
        highprice.setText(pricee);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String filename = imgsrc+".png";
        String location = "gs://android-32da6.appspot.com";
        String storagePath = "images/"+filename;
        StorageReference imageRef = mStorageRef.child(storagePath);
        try {
            // Storage 에서 다운받아 저장시킬 임시파일
            final File imageFile = File.createTempFile("images", "jpg");
            imageRef.getFile(imageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Success Case
                    Bitmap bitmapImage = BitmapFactory.decodeFile(imageFile.getPath());
                    img.setImageBitmap(bitmapImage);
                    Toast.makeText(getApplicationContext(), "Success !!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail Case
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fail !!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        buy.setOnClickListener(new View.OnClickListener() {
              @Override
          public void onClick(View view) {
                  nowprice = price.getText().toString();
                  int userprice = Integer.parseInt(nowprice);
                  int highprice = Integer.parseInt(pricee);
                  if (userprice<=highprice){
                      Toast.makeText(getApplicationContext(),"현재 낙찰가 보다 높아야 살수 있습니다.",Toast.LENGTH_SHORT).show();

                  }
                  else{

                      try {
                          ShowDetailProduct.CustomTask task = new ShowDetailProduct.CustomTask();
                          task.execute(nowprice,pname,name,id).get();//낙찰가,title,작
                          Toast.makeText(getApplicationContext(),"정상적으로 등록되었습니다.",Toast.LENGTH_SHORT).show();
                          finish();
                      } catch (Exception e) {

                      }
                  }
              }
        });






    }
        class CustomTask extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... strings) {

                try {
                    URL url = new URL("http://192.168.0.55:8181/test/priceupdate.jsp");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "price="+strings[0]+"&pname="+strings[1]+"&name="+strings[2]+"&id="+strings[3];
                    osw.write(sendMsg);
                    osw.flush();
                    if (conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();
                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();


                    } else {
                        Log.i("통신 결과", conn.getResponseCode() + "에러");
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

