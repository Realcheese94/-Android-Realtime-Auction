package com.example.sj.gohigh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

public class chatting extends AppCompatActivity {
    ListView listView;
    Button sendButton;
    EditText editText;
    String productname,id;
    TextView textView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference("message");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        textView = (TextView)findViewById(R.id.textview);
        listView = (ListView)findViewById(R.id.chatlistview);
        sendButton = (Button)findViewById(R.id.sendbutton);
        editText = (EditText)findViewById(R.id.chatText);


        id=intent.getStringExtra("id");

        productname = intent.getStringExtra("pname");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatData chat = new ChatData(id,editText.getText().toString());
                databaseReference.child("chat").child(productname).push().setValue(chat);

                editText.setText("");

            }
        });
    }
    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatDTO = dataSnapshot.getValue(ChatData.class);
        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
        openChat(productname);
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatDTO = dataSnapshot.getValue(ChatData.class);
        adapter.remove(chatDTO.getUserName() + " : " +chatDTO.getMessage());
        openChat(productname);
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(productname).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);

                Log.e("LOG", "s:"+s);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Comment newComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();
                adapter.add(newComment.toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
