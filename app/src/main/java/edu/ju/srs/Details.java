package edu.ju.srs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.atoast.AToast;

import java.util.ArrayList;
import java.util.List;

import edu.ju.srs.models.Courses;
import edu.ju.srs.models.Detail;


public class Details extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i=getIntent();
       int j= i.getIntExtra("id",-1);

        Helper helper=new Helper(getApplicationContext());
        Detail d= helper.getDetail(j+"");
        if(d!=null){
            List<Courses> l=new ArrayList<>();
            List<Courses> iter = d.getCourses();
            for (Courses c:iter){
                l.add(c);
            }
            setTitle(helper.getSummaryById(d.getSemesterId()).getSem());


            DetailAdapter adapter = new DetailAdapter(getApplicationContext(), l);
            ViewPager viewPager = findViewById(R.id.container);
            viewPager.setAdapter(adapter);
            viewPager.setPadding(130,0,130,0);
        }

        else
            AToast.makeText(getApplicationContext(),"No detail for this semester",Toast.LENGTH_LONG, AToast.CONFUSING).show();

    }

}
