package edu.ju.srs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.atoast.AToast;

import java.util.List;


public class Home extends AppCompatActivity {
    TextView name,id,dept;
    ImageView avatar;
    SharedPreferences preferences;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    private AlertDialog alert;
    public static String msg=null;
    public static int msg_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name=findViewById(R.id.name);
        progressBar=findViewById(R.id.progress);
        linearLayout=findViewById(R.id.menu);
        Background background=new Background(getApplicationContext(),progressBar,linearLayout,Home.this);
        background.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        id=findViewById(R.id.id);
        dept=findViewById(R.id.dept);
        avatar=findViewById(R.id.imageView);
        Intent i=getIntent();
        preferences=getSharedPreferences("SRS",0);
        String uid=preferences.getString("uid","");
        name.setText(preferences.getString("NAME","Not Set"));
        id.setText(preferences.getString("ID","Not set"));
        dept.setText(preferences.getString("DEPT","Not Set"));
        String sex=preferences.getString("SEX","U");
        if(sex.equals("M")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                avatar.setImageDrawable(getDrawable(R.drawable.ic_man_avatar));
            } else avatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_man_avatar));
        }
        else if(sex.equals("F")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                avatar.setImageDrawable(getDrawable(R.drawable.ic_woman_avatar));

            } else avatar.setImageDrawable(getResources().getDrawable(R.drawable.ic_woman_avatar));
        }
         msg= preferences.getString("MSG","");
        msg_num= preferences.getInt("msg_num",0);


    }



    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Home.this.finish();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        alert.dismiss();
                        break;

                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this,R.style.AlertTheme);
        alert=builder.setTitle("Are you sure you want to exit?").setPositiveButton("Yes", dialogClickListener).setCancelable(false)
                .setNegativeButton("No", dialogClickListener).setIcon(R.drawable.ic_exit_to_app_black_dp).create();
        alert.show();


    }

    public void logout(View view) {
        SharedPreferences preferences=getSharedPreferences("SRS",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("saved",false);
        editor.commit();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
    public void about(View view) {

        startActivity(new Intent(getApplicationContext(),About.class));
    }
    public void detail(View view) {

        startActivity(new Intent(getApplicationContext(),Details_Menu.class));
    }

    public void summary(View view) {
        Helper helper=new Helper(getApplicationContext());
        List<edu.ju.srs.models.Summary> s = helper.getSummary();
        if(helper.getSummaryLen()<=0)
        {
            AToast.makeText(getApplicationContext(),"Loading",AToast.LENGTH_SHORT, AToast.INFO).show();
            return;
        }

        startActivity(new Intent(getApplicationContext(),Summary.class));
    }


}
class Background extends AsyncTask<Void,Void,Void> {
    AlertDialog alert;
    Context c;
    LinearLayout layout;
    ProgressBar progressBar;
    Home home;

    public Background(Context applicationContext, ProgressBar progressBar, LinearLayout linearLayout, Home home) {
        this.c=applicationContext;
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.home= home;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (new Helper(c).getSummary().isEmpty()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("SRSD","ERRRO");
        }
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
        if(!Home.msg.equals("")){
            String s[]=Home.msg.split("#");
            if(s.length!=4)return;
            SharedPreferences.Editor e = c.getSharedPreferences("SRS",0).edit();
            e.putString("MSG","");
            try{
            int i=Integer.parseInt(s[3]);
            if(Home.msg_num>=i)
                return;
            e.putInt("msg_num",i);
            }catch (Exception ex){
                e.putInt("msg_num",0);
            }
            e.apply();

            if(s.length==4)
                singleChoice(s[0],s[1],s[2]);
        }
    }

    public void singleChoice(String title,String msg,String btitle){

        AlertDialog.Builder builder = new AlertDialog.Builder(home,R.style.AlertTheme);
        builder.setMessage(msg).setTitle(title)
                .setCancelable(true)
                .setPositiveButton(btitle, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();
    }
}
