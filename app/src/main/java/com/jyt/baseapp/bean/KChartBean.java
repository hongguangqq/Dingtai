package com.jyt.baseapp.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LinWei on 2017/9/27 14:45
 */
public class KChartBean {
    private float min=0f;
    private float max=0f;
    private List<KChartData> k;


    public float getMin(){
        if (k!=null && k.size()>0){
            for (int i=0;i<k.size();i++){
                float num=Float.valueOf(k.get(i).low);
                if (min>num){
                    min=num;
                }
                if (max<num){
                    max=num;
                }
            }
        }
        return this.min;
    }

    public float getMax(){
        if (k!=null && k.size()>0){
            for (int i=0;i<k.size();i++){
                float num=Float.valueOf(k.get(i).high);
                if (max<num){
                    max=num;
                }
            }
        }
        return this.max;
    }
    private boolean isreverse;
    public List<KChartData> getK(){
        if (k==null){
            k=new ArrayList<>();
        }
        if (!isreverse){
            Collections.reverse(k);
            isreverse=true;
        }
        return k;
    }

}
