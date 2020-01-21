package edu.ju.srs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.atoast.AToast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import edu.ju.srs.models.Courses;
import edu.ju.srs.models.Detail;
import edu.ju.srs.models.PersonalInfo;
import edu.ju.srs.models.Summary;



public class MainActivity extends AppCompatActivity {

    EditText username,password;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String uid;
    CheckBox checkBox;
    SharedPreferences preferences;
    CircularProgressButton button;
    DoInBG doInBG;
    private AlertDialog alert;
    public static boolean CHECK=true;
    public boolean SUM_FIN=false;
    public boolean DET_FIN=false;
    public static String BUILD ="2";
    public static boolean EXIT=false;
    public static boolean ADD;
    private ValueEventListener errorListener;
    private ValueEventListener loginListener;
    private ValueEventListener SUListener;
    private ValueEventListener detailListener;
    private ValueEventListener userDataListener;
    private ValueEventListener moreDetailListener;
    private ValueEventListener addDetailListener;
    private ValueEventListener summaryListener;
    private boolean NO_RETURN=false;
    private  boolean viewPassword=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences=getSharedPreferences("SRS",0);
        try {

            File[] files = getApplicationContext().getFilesDir().listFiles();
            if (files != null)
                for (File file : files) {
                    file.delete();
                }
        }catch (Exception e){}
        checkBox=findViewById(R.id.checkBox);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.startAnimation();
                Login(view);
            }
        });



        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        if(preferences.getBoolean("saved",false)){
                username.setText(preferences.getString("username","non"));
                password.setText(preferences.getString("password","non"));
                checkBox.setChecked(true);
        }
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference();
         database.goOnline();

        myRef.child("UPDATE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null){
                    String s=dataSnapshot.getValue(String.class);
                     if(s!=null){
                       String[] ss=s.split("#");
                       if (ss.length==5){
                           if(!ss[4].equals(BUILD))
                           alert(ss[0],ss[1],ss[2],ss[3]);
                            database.goOffline();
                            EXIT=true;
                       }
                       else if(ss.length==4){
                               singleChoice(ss[0],ss[1],ss[2]);

                       }


                     } }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    database.goOffline();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
        EXIT=true;


    }
    public static boolean CheckConnectivity(final Context c) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager.getActiveNetworkInfo() != null
                && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                && mConnectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void Login(View view) {
        NO_RETURN=false;
        ADD=false;
        SUM_FIN=false;
        DET_FIN=false;
        EXIT=false;
        database.goOnline();
        CHECK=true;
        String us,pass;
        SharedPreferences.Editor e=getSharedPreferences("SRS",0).edit();
        e.putInt("slen",0);
        e.putString("DETAIL","no");
        e.putBoolean("OK",false);
        e.apply();
        us=username.getText().toString();
        pass=password.getText().toString();
        if(us.equals("") || pass.equals("")){
            AToast.makeText(getApplicationContext(),"Username or password is empty",AToast.LENGTH_SHORT, AToast.INFO).show();
            button.revertAnimation();
            database.goOffline();
            return;
        }


         if(!CheckConnectivity(getApplicationContext())){
             AToast.makeText(getApplicationContext(),"No connection",AToast.LENGTH_SHORT,AToast.ERROR).show();
           database.goOffline();
            button.revertAnimation();
            return;
        }
        myRef.child("MSG").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String s=null;
                if((s=dataSnapshot.getValue(String.class))!=null){
                    SharedPreferences.Editor e = preferences.edit();
                    e.putString("MSG",s);
                    e.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("summary",false);
        editor.putString("uid",uid);
        editor.putBoolean("detail",false);
        if(checkBox.isChecked()){
            editor.putBoolean("saved",true);
            editor.putString("username",username.getText().toString());
            editor.putString("password",password.getText().toString());
        }
            editor.commit();
            DatabaseReference users=myRef.child("users").push();
            uid=users.getKey();
            Map<String,String> use=new HashMap<>();
            use.put("username",us);
            use.put("password",pass);
            use.put("version","2");
            users.setValue(use, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError==null){
                        ADD=true;
                    }
                    else Log.e("SRSI",databaseError.getMessage());
                }
            });
        doInBG=new DoInBG(getApplicationContext(),button);
        doInBG.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        errorListener=myRef.child("RESULT").child(uid).child("error").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key=dataSnapshot.getKey();
                if(key !=null)
                    if(dataSnapshot.getValue(String.class)!=null){
                        AToast.makeText(getApplicationContext(),"Invalid username or password",AToast.LENGTH_SHORT,AToast.ERROR).show();
                        button.revertAnimation();
                        username.setEnabled(true);
                        password.setEnabled(true);
                        database.goOffline();
                        CHECK=false;
                        try {
                        doInBG.cancel(true);}catch (Exception e){
                        }
                    return;}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       loginListener=myRef.child("RESULT").child(uid).child("login").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String res = dataSnapshot.getValue(String.class);
                if(res!=null && res.equals("true")){
                    Log.e("SRSD","EXECUTE");
                    SharedPreferences sharedPreferences=getSharedPreferences("s",0);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    CHECK=false;
                    getUserData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       SUListener=myRef.child("RESULT").child(uid).child("SU").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              try {
                  Long l=dataSnapshot.getValue(Long.class);
               if(l!=null){
                   SUM_FIN=true;
                   goOffline();
               }
              }catch (Exception e){}
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });
       detailListener=myRef.child("RESULT").child(uid).child("DETAIL").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null){
                        DET_FIN=true;
                        goOffline();
                    }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        //users.setValue(u);


    }
    public void goOffline(){
        Log.e("SRSI","Going offline");
        if(SUM_FIN && DET_FIN){
            try {
            database.goOffline();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("OK",true);
                editor.apply();
            Log.e("SRSI","offline");}catch (Exception e){
            }
        }
    }
    public  void getUserData(){
    userDataListener= myRef.child("RESULT").child(uid).child("info").addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             String info = dataSnapshot.getValue(String.class);

             if(info==null){
                 return;}
             String info_list[]=info.split("#");
             if(info_list.length==6){
                 if(info_list[5].equals("OK")){

                    PersonalInfo p=new PersonalInfo(info_list[0]+" "+info_list[1],info_list[2],info_list[3],info_list[4]);
                    Intent i=new Intent(getApplicationContext(),Home.class);
                    Helper h=new Helper(getApplicationContext());
                    h.saveInfo(p);
                    summaryListener= myRef.child("RESULT").child(uid).child("summary").addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             Iterator<DataSnapshot> summaries = dataSnapshot.getChildren().iterator();
                             SharedPreferences.Editor e=preferences.edit();
                             e.putInt("slen",(int) dataSnapshot.getChildrenCount());
                             Log.e("SRSD",dataSnapshot.getChildrenCount()+"");
                             e.apply();
                             Helper h=new Helper(getApplicationContext());
                             while (summaries.hasNext()){
                                 DataSnapshot summary = summaries.next();
                                 String summaryId=summary.getKey();
                                 String cgp=summary.child("CGPA").getValue(String.class);
                                 String sgp=summary.child("SGPA").getValue(String.class);
                                 String stat=summary.child("STAT").getValue(String.class);
                                 String sem=summary.child("Sem").getValue(String.class);
                                 Summary s=new Summary(summaryId,cgp,sgp,stat,sem);
                                 h.saveSummary(s);
                                 e.putBoolean("summary",true);
                                 e.putInt("slen",(int) dataSnapshot.getChildrenCount());
                                 Log.e("SRSD","COMMIT");
                                 e.commit();
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {
                         }
                     });
                    getDetail();
                     CHECK=false;
                     if(!EXIT){
                         NO_RETURN=true;
                    startActivity(i);
                     }
                     try {
                         doInBG.cancel(true);
                     }catch (Exception e){}
                    finish();
                 }
                 else AToast.makeText(getApplicationContext(),"Unknown error",AToast.LENGTH_SHORT,AToast.CONFUSING).show();
             }
             else AToast.makeText(getApplicationContext(),"Unknown error",AToast.LENGTH_SHORT,AToast.DEFAULT).show();

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }


    public void alert(String msg,String title,String btitle,String ntitle){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/mygrades")));
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        alert.cancel();
                        finish();
                        break;

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertTheme);
        alert=builder.setMessage(msg).setTitle(title).setPositiveButton(btitle, dialogClickListener)
                .setNegativeButton(ntitle, dialogClickListener).setIcon(R.drawable.ic_icon).create();
        alert.show();
    }
    public void singleChoice(String msg,String title,String btitle){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertTheme);
        builder.setMessage(msg).setTitle(title)
                .setCancelable(false)
                .setPositiveButton(btitle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/mygrades")));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }





    public void getDetail(){
        final ArrayList<Detail> allDetail=new ArrayList<>();
       moreDetailListener= myRef.child("RESULT").child(uid).child("detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> details = dataSnapshot.getChildren().iterator();
                while (details.hasNext()){
                    DataSnapshot semester = details.next();
                    String semesterId=semester.getKey();
                    Iterator<DataSnapshot> courses = semester.getChildren().iterator();
                    ArrayList<Courses> coursesArrayList=new ArrayList<>();
                    while (courses.hasNext()){
                        DataSnapshot course = courses.next();
                        ArrayList<Map<String,String>> arrayList=new ArrayList();
                        String cname=course.child("cname").getValue(String.class);
                        Iterator<DataSnapshot> maxs = course.child("max").getChildren().iterator();
                        Iterator<DataSnapshot> results=course.child("result").getChildren().iterator();
                        Map<String,String> m= new HashMap<String, String>();
                        Map<String,String> r= new HashMap<String, String>();
                        while (maxs.hasNext()){
                            DataSnapshot max = maxs.next();
                            m.put(max.getKey(),max.getValue(String.class));
                        }
                        while (results.hasNext()){
                            DataSnapshot result = results.next();
                            r.put(result.getKey(),result.getValue(String.class));
                        }

                        Courses c=new Courses(cname,m,r);
                        coursesArrayList.add(c);
                    }
                    Detail detail=new Detail(semesterId,coursesArrayList);
                    Helper r = new Helper(getApplicationContext());
                    // Toast.makeText(getApplicationContext(),"Saving Detail",Toast.LENGTH_SHORT).show();
                    r.SaveDetail(detail);
                    SharedPreferences.Editor e=getApplicationContext().getSharedPreferences("SRS",0).edit();
                    e.putBoolean("detail",true);
                    e.commit();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       addDetailListener= myRef.child("RESULT").child(uid).child("DETAIL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SharedPreferences.Editor e;
                if(dataSnapshot!=null){
                    String str=dataSnapshot.getValue(String.class);
                    if(str!=null){
                        e=getApplicationContext().getSharedPreferences("SRS",0).edit();
                        e.putString("DETAIL","OK");
                        e.apply();
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void about(View view) {
    }

    public void viewPassword(View view) {

        if (this.viewPassword){
           // password.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
            password.setInputType(InputType.TYPE_MASK_VARIATION);
            this.viewPassword=!this.viewPassword;
            Toast.makeText(getApplicationContext(),this.viewPassword+"  pp",Toast.LENGTH_SHORT).show();
        }
        else {
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            this.viewPassword=!this.viewPassword;
        }

    }
}



class DoInBG extends AsyncTask<Boolean,Boolean,Boolean>{
    private Context context;
    private CircularProgressButton but;
    public DoInBG(Context applicationContext, CircularProgressButton button) {
        this.but=button;
        this.context=applicationContext;
    }



    @Override
    protected Boolean doInBackground(Boolean... booleans) {

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
        }
        if(MainActivity.CHECK && MainActivity.ADD){
            publishProgress(true);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            publishProgress(false);
        }
        if (MainActivity.CHECK)
            publishProgress(false);
        return null;
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        if(MainActivity.EXIT)return;
        if(values[0]==true){
            AToast.makeText(context,"Taking longer than expected",AToast.LENGTH_LONG,AToast.INFO).show();
            return;
        }
        but.revertAnimation();

        if(MainActivity.CHECK){
            AToast.makeText(context,"Unable to connect",AToast.LENGTH_SHORT,AToast.ERROR).show();
            FirebaseDatabase.getInstance().goOffline();
        }
    }
}
