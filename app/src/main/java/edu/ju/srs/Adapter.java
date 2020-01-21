package edu.ju.srs;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.atoast.AToast;

import java.util.List;

import edu.ju.srs.models.Summary;

public class Adapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Summary> list;
    private SharedPreferences preferences;
    Adapter(Context context, List<Summary> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater=LayoutInflater.from(context);
        this.preferences=context.getSharedPreferences("SRS",0);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view.equals(o);
    }




      @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=layoutInflater.inflate(R.layout.card_layout,container,false);
        TextView title,sgp,cgp,stat;
        Button btn;
        title=view.findViewById(R.id.title);
        sgp=view.findViewById(R.id.SGP);
        cgp=view.findViewById(R.id.CGP);
        stat=view.findViewById(R.id.Stat);
        btn=view.findViewById(R.id.viewDetail);
        btn.setTag(list.get(position).getSemId());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s=Integer.parseInt((String) view.getTag());
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("id",s);
                Helper h=new Helper(context);
                if(h.getDetail(s+"")!=null)
                context.startActivity(intent);
                else {
                    if(!preferences.getBoolean("OK",false)) {
                        AToast.makeText(context,"Loading...",AToast.LENGTH_SHORT, AToast.INFO).show();
                    } else
                    AToast.makeText(context,"No detail for this semester",AToast.LENGTH_SHORT,AToast.INFO).show();}
            }
        });
          title.setText(list.get(position).getSem());

          sgp.setText(list.get(position).getSGP());

          cgp.setText(list.get(position).getCGP());

          stat.setText(list.get(position).getSTAT());

          container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
