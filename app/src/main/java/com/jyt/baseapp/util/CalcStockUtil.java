package com.jyt.baseapp.util;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jyt.baseapp.bean.KChartBean;
import com.jyt.baseapp.bean.KChartData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LinWei on 2017/10/18 16:43
 */
public class CalcStockUtil {


    public KChartBean transformTime(KChartBean datas, int num){
        KChartBean convertData=new KChartBean();
        int max=datas.getK().size();
        if (num!=1){
            String open="";
            String close="";
            float high=0;//最高价
            float low=0;//最低价
            int vol=0;
            for (int i = 0,j=0; i <max; i++) {
                if (j==0){
                    open= datas.getK().get(i).open;
                    high=Float.valueOf(datas.getK().get(i).high);
                    low=Float.valueOf(datas.getK().get(i).low);
                }
                if (high < Float.valueOf(datas.getK().get(i).high)){
                    high=Float.valueOf(datas.getK().get(i).high);
                }
                if (low > Float.valueOf(datas.getK().get(i).low)){
                    low=Float.valueOf(datas.getK().get(i).low);
                }
                vol+=Integer.valueOf(datas.getK().get(i).vol);
                if (j==num-1 || i==max-1){
                    close= datas.getK().get(i).close;
                    KChartData data=new KChartData();
                    data.open=open;
                    data.close=close;
                    data.high=high+"";
                    data.low=low+"";
                    data.time=datas.getK().get(i).time;
                    data.vol=vol/num+"";
                    convertData.getK().add(data);//加入到转换后的集合中
                    vol=0;
                    j=0;
                }else {
                    j++;
                }

            }
        }else {
            convertData=datas;
        }
        return convertData;
    }

    public void  CalcMACD(List<KChartData> data,List<Entry> LINE ,List<Entry> DIFF, List<Entry> DEA, List<BarEntry> BAR) {
        float lastEMA12 = -1;
        float lastEMA26 = -1;
        float lastDEA = 0;
        for (int i = 0; i < data.size(); i++){
            float todayEMA12 = 0;
            float todayEMA26 = 0;
            if (i != 0) {
                todayEMA12 = EMA(12, lastEMA12, Float.valueOf(data.get(i).close));
                todayEMA26 = EMA(26, lastEMA26, Float.valueOf(data.get(i).close));
            }

            float todayDIFF = todayEMA12 - todayEMA26;
            float todayDEA = lastDEA * 8 / 10 + todayDIFF * 2 / 10;
            float todayBAR = 2 * (todayDIFF - todayDEA);
            DIFF.add(new Entry(i,todayDIFF));
            DEA.add(new Entry(i,todayDEA));
            BAR.add(new BarEntry(i,todayBAR));
            LINE.add(new Entry(i,todayBAR));
            if (i==0){
                lastEMA12=Float.valueOf(data.get(i).close);
                lastEMA26=Float.valueOf(data.get(i).close);
            }else {
                lastEMA12 = todayEMA12;
                lastEMA26 = todayEMA26;
            }
            lastDEA=todayDEA;
        }


    }

    public void CalcTRIX(List<KChartData> data, List<Entry> TRIX, List<Entry> MATRIX){
        float lastTr = 0;
        float lastEMA2 = 0;
        float lastEMA3 = 0;
        for (int i = 0; i < data.size(); i++) {
            float close = Float.valueOf(data.get(i).close);
            float trix = 0;
            float tr = close;
            float todayEMA2 = close;
            float todayEMA3 = close;
            if (i != 0)
            {
                todayEMA3 = EMA(12, lastEMA3, close);
                todayEMA2 = EMA(12, lastEMA2, todayEMA3);
                tr = EMA(12, lastTr, todayEMA2);
                trix = (tr - lastTr) / lastTr * 100;
            }
            TRIX.add(new Entry(i,trix));
            if (i>19){
                MATRIX.add(new Entry(i,MA(20,i,TRIX,"y")));
            }
            lastTr=tr;
            lastEMA2=todayEMA2;
            lastEMA3=todayEMA3;
        }
    }

    public void CalcBRAR(List<KChartData> data , List<Entry> BR , List<Entry> AR) {
        List<Float> HighSubClose = new ArrayList<>();
        List<Float> CloseSubLow = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            float ar=0;
            if (i!=0){
                float fm=(SUM(26,i,data,"open") - - (SUM(26, i, data, "close")));
                if (fm==0){
                    ar=0;
                }else {
                    ar=(SUM(26, i, data, "high") - SUM(26, i, data, "open")) / fm;
                }
            }

            AR.add(new Entry(i,ar));
            HighSubClose.add(Math.max(0,(i==0 ? Float.valueOf(data.get(i).high) : Float.valueOf(data.get(i-1).close)))-Float.valueOf(data.get(i).close));
            CloseSubLow.add(Math.max(0,(i==0 ? Float.valueOf(data.get(i).close): Float.valueOf(data.get(i-1).close)))-Float.valueOf(data.get(i).low));
            float br =SUM(26,i,HighSubClose,null) / SUM(26, i, CloseSubLow, null);
            BR.add(new Entry(i,br));
        }
    }

    public void CalcCR(List<KChartData> data, List<Entry> CR, List<Entry> MA1, List<Entry> MA2, List<Entry> MA3){
        List<Float> HighSubMid = new ArrayList<>();
        List<Float> MidSubLow = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {

            float mid;
            if (i!=0){
                mid=(Float.valueOf(data.get(i).high) + Float.valueOf(data.get(i-1).low) - Float.valueOf(data.get(i-1).close))/3;
            }else {
                mid=(Float.valueOf(data.get(i).high) + Float.valueOf(data.get(i).low) - Float.valueOf(data.get(i).close))/3;
            }
            HighSubMid.add(Math.max(0,Float.valueOf(data.get(i).high)-mid));
            MidSubLow.add(Math.max(0,mid-Float.valueOf(data.get(i).low)));

            float fm=SUM(26,i,MidSubLow,null);
            float cr= fm==0 ? 0:SUM(26,i,HighSubMid,null);

            CR.add(new Entry(i,Float.valueOf(BaseUtil.getDecimalNum("0.00",cr))));

            if (i>=4){
                float ma1=MA(5,i,CR,"y");
                MA1.add(new Entry(i,ma1));
            }

            if (i>=9){
                float ma2=MA(10,i,CR,"y");
                MA2.add(new Entry(i,ma2));
            }

            if (i>=19){
                float ma3=MA(20,i,CR,"y");
                MA3.add(new Entry(i,ma3));
            }
        }
    }

    public void CalcVR(List<KChartData> data,List<Entry> MAVV, List<Entry> VRData, List<Entry> MAVR){
        for (int i = 0; i < data.size(); i++) {

            if (i>=25){
                float num=Float.valueOf(BaseUtil.getDecimalNum("0.00",VR(26,i,data)));
                VRData.add(new Entry(i,num));
                MAVV.add(new Entry(i,num));
            }else {
                MAVV.add(new Entry(i,0));
            }
            MAVR.add(new Entry(i,Float.valueOf(BaseUtil.getDecimalNum("0.00",MA(4,i,MAVV,"y")))));
        }
        Log.e("@#","MAVRsize="+MAVR.size());
        Log.e("@#","datasize="+data.size());

    }

    public void CalcOBV(List<KChartData> data,List<Entry> OBVData, List<Entry> MAOBVData){
        for (int i = 0; i < data.size(); i++) {
            float obv = 0;
            if (i==0){
                obv=OBV(30,i,data);
            }
            OBVData.add(new Entry(i,obv));
            MAOBVData.add(new Entry(i,MA(5,i,OBVData,"y")));
        }
    }


    public void CalcEMV(List<KChartData> data,List<Entry> EMVData, List<Entry> MAEMVData){
        List<Float> EMData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {

            float a=(Float.valueOf(data.get(i).high) + Float.valueOf(data.get(i).low))/2;
            float b=0;
            float c=Float.valueOf(data.get(i).high) - Float.valueOf(data.get(i).low);
            if (i>1){
                b=(Float.valueOf(data.get(i-2).high) + Float.valueOf(data.get(i-2).low))/2;
            }
            float em=0;
            if (Float.valueOf(data.get(i).vol)!=0){
                em=(a-b)*c/Float.valueOf(data.get(i).vol);
            }
            EMData.add(em);
            EMVData.add(new Entry(i,Float.valueOf(SUM(14,i,EMData,null))));
            MAEMVData.add(new Entry(i,Float.valueOf(SUM(9,i,EMVData,"y"))));

        }
    }

    public void CalcRSI(List<KChartData> data, List<Entry> RSIData1,List<Entry> RSIData2,List<Entry> RSIData3){
        List<Float> MaxCloseSubLastClose = new ArrayList<>();
        List<Float> AbsCloseSubLastClose = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            float lc=0;
            if (i!=0){
                lc=Float.valueOf(data.get(i-1).close);
            }else {
                lc=Float.valueOf(data.get(i).close);
            }
            MaxCloseSubLastClose.add(Math.max(Float.valueOf(data.get(i).close)-lc,0));
            AbsCloseSubLastClose.add(Math.abs(Float.valueOf(data.get(i).close)-lc));
            RSIData1.add(new Entry(i,
                    i==0 ? 0: SUM(6,i,MaxCloseSubLastClose,null)/SUM(6,i,AbsCloseSubLastClose,null)));
            RSIData2.add(new Entry(i,
                    i==0 ? 0: SUM(12,i,MaxCloseSubLastClose,null)/SUM(12,i,AbsCloseSubLastClose,null)));
            RSIData3.add(new Entry(i,
                    i==0 ? 0: SUM(24,i,MaxCloseSubLastClose,null)/SUM(24,i,AbsCloseSubLastClose,null)));
        }
    }

    public void CalcKDJ(List<KChartData> data, List<Entry> K,List<Entry> D,List<Entry> J){
        float last9High = 0;
        float last9Low = 0;
        float lastK = 100;
        float lastD = 100;
        for (int i = 0; i < data.size(); i++) {
            last9High=MAX(9,i,data);
            last9Low=MIN(9,i,data);

            float rsv=(Float.valueOf(data.get(i).close)-last9Low) / (last9High-last9Low)*100;
            float todayK = (rsv + 2 * lastK) / 3;
            float todayD = (todayK + 2 * lastD) / 3;
            float todayJ = (3 * todayK - 2 * todayD);
            lastK = todayK;
            lastD = todayD;
            K.add(new Entry(i,todayK));
            D.add(new Entry(i,todayD));
            J.add(new Entry(i,todayJ));
        }
    }

    public void CalcCCI(List<KChartData> data, List<Entry> CCIdata){
        List<Float> MASubClose=new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            float tp=(Float.valueOf(data.get(i).high) + Float.valueOf(data.get(i).low) + Float.valueOf(data.get(i).close))/3;
            float ma=SUM(14,i,data,"close");
            MASubClose.add(ma - Float.valueOf(data.get(i).close));
            float md=SUM(14,i,MASubClose,null)/14;
            float cci=0;
            if (md!=0){
                cci= (float) ((tp-ma)/md/0.015);
            }
            CCIdata.add(new Entry(i,cci));
        }
    }

    public void CalcMTM(List<KChartData> data, List<Entry> MTMData,List<Entry> MAMTMData){
        for (int i=0;i<data.size();i++){
            float mtm=0;
            if (i!=0){
                mtm=Float.valueOf(data.get(i).close) - REF(12,i,data,"close");
            }
            MTMData.add(new Entry(i,mtm));
            MAMTMData.add(new Entry(i,MA(6,i,MTMData,"y")));
        }
    }

    public void CalcPSY(List<KChartData> data, List<Entry> PSYData,List<Entry> MAPSYData){
        List<Boolean> CloseSubRefClose = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            CloseSubRefClose.add(Float.valueOf(data.get(i).close)>REF(1,i,data,"close"));
            float psy=COUNT(12,i,CloseSubRefClose,null);
            PSYData.add(new Entry(i,psy));
            MAPSYData.add(new Entry(i,MA(6,i,PSYData,"y")));
        }
    }


    private  float COUNT(int time, int index, List data, String keyPath)
    {
        float all = 0;
        int startIndex = index - time >= -1 ? index - time + 1 : 0;
        for (; startIndex <= index; startIndex++)
        {
            boolean temp= (boolean) Reflect(data.get(startIndex),keyPath);
            if (temp)
            {
                all++;
            }
        }
        return all;
    }

    public float OBV(int time, int index, List<KChartData> data)
    {
        float all = 0;
        int startIndex = index - time >= -1 ? index - time + 1 : 0;
        for (; startIndex <= index; startIndex++)
        {
            if (startIndex == 0)
            {
                continue;
            }
            if ( Float.valueOf(data.get(index).close) - Float.valueOf(data.get(index-1).close) > 0)
            {
                all += Float.valueOf(data.get(index).vol);
            }
            else
            {
                all -= Float.valueOf(data.get(index).vol);
            }
        }
        return all;
    }

    public float REF(int time, int index, List data, String keyPath)
    {
        int realIndex = index - time;
        if (realIndex < 0)
            realIndex = 0;
        return Float.valueOf(Reflect(data.get(realIndex),keyPath).toString());
    }

    public float SUM(int time,int index,List data,String keyPath)
    {
        float all = 0;
        int startIndex = index - time >= -1 ? index - time + 1 : 0;
        for (; startIndex <= index;startIndex++ ) {
            float f = 0;
            Object value = Reflect(data.get(startIndex), keyPath);
            if (value instanceof String){
                try {
                    f=Float.valueOf((String) value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (value instanceof Float){
                f= (float) value;
            }
            all += f;
        }
        return all;
    }


    public float MA(int time,int index,List data,String keyPath)
    {
        return SUM(time, index, data, keyPath) / time;
    }

    public float VR(int time,int index,List<KChartData> data)
    {
        if (time > index +1)
        {
            return 0;
        }
        int startIndex = index - time + 1;
        float uvs = 0;
        float dvs = 0;
        float pvs = 0;
        for (; startIndex <= index; startIndex++)
        {
            float openCloseGap = Float.valueOf(data.get(startIndex).open) - Float.valueOf(data.get(startIndex).close);
            float vol = Float.valueOf(data.get(startIndex).vol);

            if (openCloseGap > 0)
            {
                uvs += vol;
            }
            else if (openCloseGap < 0)
            {
                dvs += vol;
            }
            else
            {
                pvs += vol;
            }
        }

        return (float)((uvs + 0.5 * pvs) / (dvs + 0.5 * pvs));
    }

    public float EMA(int time,float lastEMA,float val)
    {
        return lastEMA * (time - 1) / (time + 1) + val * 2 / (time + 1);
    }

    public float MAX(int time,int index,List<KChartData> data)
    {
        float high = 0;
        int startIndex = index - time >= -1 ? index - time + 1 : 0;
        for (; startIndex <= index; startIndex++)
        {
            if (Float.valueOf(data.get(startIndex).high)> high)
            {
                high = Float.valueOf(data.get(startIndex).high);
            }
        }

        return high;
    }

    public float MIN(int time,int index,List<KChartData> data)
    {
        float low = 0;
        int startIndex = index - time >= -1 ? index - time + 1 : 0;
        for (; startIndex <= index; startIndex++)
        {
            if (Float.valueOf(data.get(startIndex).low) > low)
            {
                low = Float.valueOf(data.get(startIndex).low);
            }
        }
        return low;
    }


    public Object Reflect(Object object, String keypath){
        if (keypath==null){
            return object;
        }
        Field field = null ;
        Class<?> clazz = object.getClass() ;
//        Field[] fields=clazz.getDeclaredFields();
//        for (Field f:fields) {
//            Log.e("@#","name="+f.getName());
//        }
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getField(keypath);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }




}
