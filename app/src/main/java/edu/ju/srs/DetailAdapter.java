package edu.ju.srs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.ju.srs.models.Courses;
import edu.ju.srs.models.map;


public class DetailAdapter  extends PagerAdapter {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Courses> list;

        DetailAdapter(Context context, List<Courses> list) {
            this.context = context;
            this.list = list;
            this.layoutInflater=LayoutInflater.from(context);
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

            View view=layoutInflater.inflate(R.layout.exam_layout,container,false);
            LinearLayout label=view.findViewById(R.id.labell);
            LinearLayout result=view.findViewById(R.id.result);
            TextView title,tot,gra;
            Courses c=list.get(position);
            String total="";
            String grade="";
            List<map> max=new ArrayList<>();
            List<map> res=new ArrayList<>();
            List<map> i = c.getMax();
            List<map> j = c.getResult();
            String maxx="";
            for (int i1 = 0; i1 < i.size(); i1++) {
                map k = i.get(i1);
                String value=k.getValue();
                if (k.getKey().equals("Grade"))
                    grade = value;
                else if (k.getKey().equals("Total")) {
                } else
                    max.add(k);
                if (k.getKey().equals("Final Examination"))
                    maxx = value;

            }
            for(map k:j){

                if(k.getKey().equals("Total"))
                    total=k.getValue();

                else res.add(k);
            }
             String finalEx="";
            boolean fin=false;
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll.setMargins(0,0,0, (int) convertDpToPx(context,6));
            for (int k = 0; k < res.size(); k++) {
                if(res.get(k).getKey().equals("Final Examination")){
                    fin=true;
                    finalEx=res.get(k).getValue();
                }
                else {
                TextView textView=new TextView(context);
                TextView resu=new TextView(context);
                resu.setLayoutParams(ll);
                textView.setLayoutParams(ll);
                textView.setText(res.get(k).getKey());
                resu.setText(res.get(k).getValue()+" / "+max.get(k).getValue());
                if(fin){
                    label.addView(textView, k-1);
                    result.addView(resu, k-1);
                }
                else {
                    label.addView(textView, k);
                    result.addView(resu, k);
                }
                }
            }

            TextView textView=new TextView(context);
            TextView resu=new TextView(context);

            resu.setLayoutParams(ll);
            textView.setLayoutParams(ll);
            textView.setText("Final");
            resu.setText(finalEx+" / "+maxx);
            label.addView(textView);
            result.addView(resu);
            title=view.findViewById(R.id.title);
            tot=view.findViewById(R.id.total);
            gra=view.findViewById(R.id.grade);
            title.setText(c.getCname());
            tot.setText(total);
            gra.setText(grade);
            container.addView(view,0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    }

