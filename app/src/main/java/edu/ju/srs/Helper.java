package edu.ju.srs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import edu.ju.srs.models.Detail;
import edu.ju.srs.models.PersonalInfo;
import edu.ju.srs.models.Summary;


public class Helper {
    Context context;
    public Helper(Context ctx){
        this.context=ctx;
    }
     public void saveInfo(PersonalInfo info){
         SharedPreferences preferences=context.getSharedPreferences("SRS",0);
         SharedPreferences.Editor e = preferences.edit();
         e.putString("ID",info.getIDNo());
         e.putString("NAME",info.getName());
         e.putString("DEPT",info.getDept());
         e.putString("SEX",info.getSex());
         e.apply();

    }
    public PersonalInfo getPersonalInfo(){
        return null;
    }
    public edu.ju.srs.models.Summary getSummaryById(String id){
        try {
            InputStream inputStream = context.openFileInput("s"+id);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
                Summary summary=(Summary) objectInputStream.readObject();
                inputStream.close();
                return summary;

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<Summary> getSummary(){
        List<Summary> summaries=new ArrayList<>();
        int n=getSummaryLen();
        for(int i=1;i<=n;i++){
            summaries.add(getSummaryById(i+""));
        }
        return summaries;

    }
    public Detail getDetail(String semId){
        try {
            InputStream inputStream = context.openFileInput("d"+semId);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
                Detail detail=(Detail) objectInputStream.readObject();
                inputStream.close();
                return detail;

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void SaveDetail(Detail data) {
        try {

            ObjectOutputStream outputStream=new ObjectOutputStream(context.openFileOutput("d"+ data.getSemesterId(),Context.MODE_PRIVATE));
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }




     public void saveSummary(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
     public String readSummary(Context context,String s) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(s+".txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    public int getSummaryLen(){
        return context.getSharedPreferences("SRS",0).getInt("slen",0);
    }


    public void saveSummary(Summary s) {
        try {

            ObjectOutputStream outputStream=new ObjectOutputStream(context.openFileOutput("s"+s.getSemId(),Context.MODE_PRIVATE));
            outputStream.writeObject(s);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
