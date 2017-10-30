package com.jyt.baseapp.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2017/10/17 10:06
 */
public class TimeBean {
    float interval;
    public String forUser;
    public FenshiData forWorker;
    public boolean ret;
    public boolean token;
    public ArrayList<TimeData> data;
    public ArrayList<String> time;


    public float getPercentMax(){
        if (interval ==0.0f){
            computNum();
        }
        return interval/Float.valueOf(forWorker.closePrice);
    }

    public float getPercentMin() {
        if (interval ==0.0f){
            computNum();
        }
        return -interval/Float.valueOf(forWorker.closePrice);
    }

    public float getMax(){
        if (interval ==0.0f){
            computNum();
        }
        return interval +Float.valueOf(forWorker.closePrice);
    }

    public float getMin(){
        if (interval ==0.0f){
            computNum();
        }
        return Float.valueOf(forWorker.closePrice)- interval;
    }

    public void computNum(){
        float min=Float.valueOf(data.get(0).close);
        float max=Float.valueOf(data.get(0).close);
        for (int i = 0; i < data.size(); i++) {
            if (min>Float.valueOf(data.get(i).close)){
                min=Float.valueOf(data.get(i).close);
            }
            if (max<Float.valueOf(data.get(i).close)){
                max=Float.valueOf(data.get(i).close);
            }
        }
        if (Math.abs(max-Float.valueOf(forWorker.closePrice))>Math.abs(min-Float.valueOf(forWorker.closePrice))){
            interval =Math.abs(max-Float.valueOf(forWorker.closePrice));
        }else {
            interval =Math.abs(min-Float.valueOf(forWorker.closePrice));
        }
    }

    public float centerNum(){
        if (interval ==0.0f){
            computNum();
        }
        return (getMax()-getMin())/2+getMin();
    }

    public List<String> getTime(){
        if (time==null){
            time=new ArrayList<>();
        }
        for (int i = 0; i < data.size(); i++) {
            time.add(data.get(i).time);
        }
        return time;
    }


}
