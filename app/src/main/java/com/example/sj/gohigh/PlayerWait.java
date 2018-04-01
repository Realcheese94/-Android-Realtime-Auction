package com.example.sj.gohigh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerWait extends AppCompatActivity {


    String id,sendMsg,str,receiveMsg;
    TextView idtv;
    Button playbutton,pagebutton,searchbutton,mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_wait);
        Intent intent = getIntent();

        playbutton = (Button)findViewById(R.id.playbutton);
        pagebutton = (Button)findViewById(R.id.pagebutton);
        searchbutton = (Button)findViewById(R.id.search);
        idtv=(TextView)findViewById(R.id.idtextview) ;
        mypage= (Button)findViewById(R.id.mypage);
        id=intent.getStringExtra("id");
        idtv.setText("안녕하세요  "+id+"  님 원하는 메뉴로 이동해 주세요 ");




        playbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2= new Intent(PlayerWait.this,PlayActivity.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
            }
        });
        pagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3= new Intent(PlayerWait.this,uploadpractice.class);
                intent3.putExtra("id",id);
                startActivity(intent3);
            }
        });
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4= new Intent(PlayerWait.this,Search.class);
                intent4.putExtra("id",id);
                startActivity(intent4);
            }
        });
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5= new Intent(PlayerWait.this,MyPage.class);
                intent5.putExtra("id",id);
                startActivity(intent5);
            }
        });

    }

    }



