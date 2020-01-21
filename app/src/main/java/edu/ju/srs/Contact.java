package edu.ju.srs;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Contact extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText feedback,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        name=findViewById(R.id.name);
        feedback=findViewById(R.id.feed);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
    }
    public void SEND(View view) {
        if(name.getText().toString().length()>0 && feedback.getText().toString().length()>0){
            if(!MainActivity.CheckConnectivity(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"No connection, Check your network connectivity!!",Toast.LENGTH_SHORT).show();
                return;
            }

        Map<String,String> use=new HashMap<>();
        database.goOnline();
        String uid=myRef.child("FEEDBACK").push().getKey();
        use.put("feed",feedback.getText().toString());
        use.put("name",name.getText().toString());
        if(uid==null)return;
            myRef.child("FEEDBACK").child(uid).setValue(use, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Toast.makeText(getApplicationContext(),"Thank you, Your feedback is successfully sent",Toast.LENGTH_SHORT).show();
                    feedback.setText("");
                    name.setText("");
                    if(getSharedPreferences("SRS",0).getBoolean("OK",false))
                        database.goOffline();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unable to send, Check your network connectivity!!",Toast.LENGTH_SHORT).show(); }
            }
        });
            name.setText("");
            feedback.setText("");
        }
        else Toast.makeText(getApplicationContext(),"Empty field",Toast.LENGTH_SHORT).show();
    }
}
