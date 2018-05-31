package cn.edu.zzti.soft.util;

import android.content.SharedPreferences;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.edu.zzti.soft.activity.LoginActivity;
import cn.edu.zzti.soft.activity.MainActivity;
import cn.edu.zzti.soft.activity.MyInfoActivity;
import cn.edu.zzti.soft.fragment.MyInfoFragment;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
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

    //异步post,添加请求头
//    public static void postRequestHeader(String address, okhttp3.Callback callback
//            //, Map<String,String> map
//    )
//    {
//        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();
//        FormBody.Builder builder = new FormBody.Builder();
////        if (map!=null)
////        {
////            for (Map.Entry<String,String> entry:map.entrySet())
////            {
////                builder.add(entry.getKey(),entry.getValue());
////            }
////        }
//        FormBody formbody = builder.build();
//
//        Request request = new Request.Builder()
//                .url(address)
//                .post(formbody)
//                .header("token", MyInfoActivity.realToken)
//                .build();
//        client.newCall(request).enqueue(callback);
//    }

    //同步Post请求方式
    public static Response postSync(String address, Map<String,String> map)
    {
        OkHttpClient client = new OkHttpClient();
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
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //同步get请求方式
    public static Response getSync(String address, Map<String,String> map)
    {
        OkHttpClient client = new OkHttpClient();
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
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //同步get请求方式，可以赋值Header部分
    public static Response getWithHeaderSync(String address, Map<String,String> map, Map<String,String> mapHeader) {
        OkHttpClient client = new OkHttpClient();
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
        Request.Builder  builder = new Request.Builder();
        builder.addHeader("Accept","*/*");
        if (mapHeader!=null)
        {
            for( Map.Entry<String,String> entry:map.entrySet() )
            {
                builder.addHeader(  entry.getKey(), entry.getValue()  );
            }
        }
        Request request = builder.url(address).build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void postRequestHeader(String address, okhttp3.Callback callback, Map<String,String> map
    )
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
                .addHeader("Accept","*/*")
                .addHeader("token", MainActivity.realToken)
                .build();
        client.newCall(request).enqueue(callback);
    }



}
