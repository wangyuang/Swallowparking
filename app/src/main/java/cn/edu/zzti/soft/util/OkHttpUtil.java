package cn.edu.zzti.soft.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by WYA on 2018/5/23.
 */

public class OkHttpUtil {

    public static void post(String address, okhttp3.Callback callback, Map<String,String> map)
    {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null)
        {
            for (Map.Entry<String,String> entry:map.entrySet())
            {
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void get(String address, okhttp3.Callback callback, Map<String,String> map)
    {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
        if (map!=null)
        {
            String value="?";
            int i=0;
            for( Map.Entry<String,String> entry:map.entrySet() )
            {
                value = value + entry.getKey() + "=" + entry.getValue();
                if ( i!=map.size()-1 ) {
                    value += "&";
                }
                i++;
            }
            address += value;
        }

        // System.out.println(address);
        Request request = new Request.Builder().addHeader("Accept","*/*")
                .url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
