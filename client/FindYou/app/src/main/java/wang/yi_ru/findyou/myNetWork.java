package wang.yi_ru.findyou;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by Dr_Isaac on 2016/7/13.
 */
public class myNetWork {

    public static String session_id = null;
    static String IP = null;
    static String strURL = null;
    static URL url;
    HttpURLConnection httpURLConnection;

    public boolean setIP(String IP, Activity activity) {
        try {
            myNetWork.IP = IP;
            strURL = "http://" + IP + "/login/";
            url = new URL(strURL);
            //Log.e("url", "OK");
            getSession_ID(activity);
            //Log.e("SID", "OK");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void fun(final double x, final double y,final RoutePlan.MyLocationListenner activity) {
        try {
            //if (session_id==null) session_id = "PHPSESSID="+getSession_ID();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        String toPOST = "x=" + Double.toString(x) + "&y=" + Double.toString(y);
                        byte[] data = toPOST.getBytes("UTF-8");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.addRequestProperty("Cookie", session_id);
                        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
                        httpURLConnection.setConnectTimeout(3000);
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setUseCaches(false);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        outputStream.write(data);
                        int response = httpURLConnection.getResponseCode();
                        //System.out.println(response);

                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String sLine = null;
                        StringBuffer sbHTML = new StringBuffer();
                        while ((sLine = bufferedReader.readLine()) != null) {
                            sbHTML.append(sLine);
                        }
                        String sHTML = sbHTML.toString();
                        //System.out.print(sHTML);
                        Log.e("res",sHTML+"  ");
                        JSONObject json = new JSONObject(sHTML);
                        //System.out.println(json);
                        double x = json.getDouble("x");
                        double y = json.getDouble("y");
                        //System.out.printf("%f %f\n", x, y);

                        httpURLConnection.disconnect();
                        Bundle b=new Bundle();
                        b.putDouble("x",x);
                        b.putDouble("y",y);
                        Message msg=new Message();
                        msg.setData(b);
                        activity.flashPositionHandler.sendMessage(msg);
                        //TODO:callback
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSession_ID(final Activity activity) throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String sHTML = new String();

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(true);
                    int response = httpURLConnection.getResponseCode();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String sLine = null;
                    StringBuffer sbHTML = new StringBuffer();
                    while ((sLine = bufferedReader.readLine()) != null) {
                        sbHTML.append(sLine);
                    }
                    sHTML = sbHTML.toString();
                    session_id="PHPSESSID="+sHTML;
                    httpURLConnection.disconnect();
                    Message msg=new Message();
                    msg.what=1;
                    ((MainActivity)activity).netWorkHandler.sendMessage(msg);
                    //System.out.print(httpURLConnection.getHeaderField("Set-Cookie"));
                    //System.out.println(sHTML);

                    //System.out.println(sHTML);
                    //session_id=sHTML;
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg=new Message();
                    msg.what=0;
                    ((MainActivity)activity).netWorkHandler.sendMessage(msg);
                }
            }
        };
        new Thread(runnable).start();
    }

    ;
}
