package edu.ju.srs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.atoast.AToast;

import java.util.List;

import edu.ju.srs.models.Detail;
import edu.ju.srs.models.Summary;


public class Details_Menu extends AppCompatActivity {
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__menu);
        linearLayout = findViewById(R.id.semester);
        setTitle("Detail");
         final Helper helper=new Helper(getApplicationContext());
        List<Summary> summaries=helper.getSummary();
        int i=1;
        LayoutInflater inflater=getLayoutInflater();

        for(Summary s:summaries){
            View view=inflater.inflate(R.layout.btn_card,null);
            TextView semseter=view.findViewById(R.id.semester);
            TextView year=view.findViewById(R.id.year);

            view.setTag(i);
            String[] str = s.getSem().split(",");
            if(str.length>1){
                semseter.setText(str[0]);
                year.setText(str[1]);
            }
            else {
                semseter.setText(s.getSem());
            }

           view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.findViewById(R.id.progress).getVisibility()!=View.GONE)
                        return;
                    int i=(int)view.getTag();
                    Intent intent = new Intent(getApplicationContext(), Details.class);
                    intent.putExtra("id",i);
                    Detail k = helper.getDetail("" + i);
                    if(k!=null)
                    startActivity(intent);
                    else AToast.makeText(getApplicationContext(),"No detail for this semester",AToast.LENGTH_SHORT, AToast.CONFUSING).show();;
                }
            });
            linearLayout.addView(view);
            i++;
        }
        BGP bg=new BGP(getApplicationContext(),linearLayout);
        bg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
class  BGP extends AsyncTask<Integer,Integer,Integer> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private LinearLayout linearLayout;
    BGP(Context context,LinearLayout linearLayout){
        this.context=context;
        this.linearLayout=linearLayout;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        Helper helper=new Helper(context);

        do for (int i = 1; i <= new Helper(context).getSummary().size(); i++)
            if (helper.getDetail(i + "") != null)
                publishProgress(i); while (!context.getSharedPreferences("SRS", 0).getString("DETAIL", "").equals("OK"));
        publishProgress(-1);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(values[0]==-1){
            for (int i = 0; i <linearLayout.getChildCount() ; i++)
                linearLayout.getChildAt(i).findViewById(R.id.progress).setVisibility(View.GONE);
            return;
        }

        View view=linearLayout.getChildAt(values[0]-1);
        ProgressBar p=view.findViewById(R.id.progress);
        view.findViewById(R.id.semester).setAlpha(1);
        view.findViewById(R.id.arrow).setAlpha(1);
        p.setVisibility(View.GONE);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("SRSI","finished bg process ");
    }
}
