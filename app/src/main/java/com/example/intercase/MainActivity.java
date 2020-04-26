package com.example.intercase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  static  int MAX_MESSAGE_LENGTH = 100;

    FirebaseDatabase database = FirebaseDatabase.getInstance(); // db link
    DatabaseReference myRef = database.getReference("messages"); // message
    EditText mEditTextMessage;
    Button mSendButton;
    RecyclerView mMessagesRecycler;

    ArrayList<String> messages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendButton = findViewById(R.id.send_message);
        mEditTextMessage = findViewById(R.id.messege_input);
        mMessagesRecycler = findViewById(R.id.message_recycler);

        mMessagesRecycler.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this,messages);
        mMessagesRecycler.setAdapter(dataAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = mEditTextMessage.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(getApplicationContext(),"Введите сообшение!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(msg.length() > MAX_MESSAGE_LENGTH){
                    Toast.makeText(getApplicationContext(),"Слишком длинное сообщение", Toast.LENGTH_SHORT).show();
                    return;
                }

                myRef.push().setValue(msg);
                mEditTextMessage.setText("");
            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
