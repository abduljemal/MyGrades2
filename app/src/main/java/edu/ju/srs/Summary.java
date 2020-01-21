package edu.ju.srs;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.List;



public class Summary extends AppCompatActivity {
    List<edu.ju.srs.models.Summary> list;
    ViewPager viewPager;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        setTitle("Summary");
        TextView t=findViewById(R.id.title);
        final Helper helper=new Helper(getApplicationContext());
        list=helper.getSummary();
        adapter=new Adapter(getApplicationContext(),list);
        viewPager= findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);


    }
}
