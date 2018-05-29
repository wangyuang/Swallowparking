package cn.edu.zzti.soft.activity;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.entity.Order;
import cn.edu.zzti.soft.entity.PayResult;
import cn.edu.zzti.soft.util.ClsUtils;
import cn.edu.zzti.soft.util.GXCWUtil;

import static cn.edu.zzti.soft.R.id.pub_money;
import static cn.edu.zzti.soft.zxing.ui.MipcaActivityCapture.resultString;

/**
 * Created by WYA on 2018/5/1.
 */

public class UnlockActivity extends Activity implements View.OnClickListener{
    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄

    //SPP服务UUID号

    private InputStream is;    //输入流，用来接收蓝牙数据
    private Bundle bundle;
    private String address;

    private Button btn_zf;//支付

    private TextView pak_name;

    private TextView pak_time;

    private TextView pak_money;

    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存
    private String name = null;

    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;
    boolean bRun = true;
    boolean bThread = false;

    private ImageView title_img;
    private TextView title_name;

    private static final int SDK_PAY_FLAG = 1;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        init();
        initdata();
    }

    private void init(){
        title_img = (ImageView)findViewById(R.id.title_img);
        title_img.setImageResource(R.drawable.fx_icon_back_n);
        title_img.setOnClickListener(this);

        title_name = (TextView)findViewById(R.id.title_name);
        title_name.setText("开锁");

        btn_zf = (Button) findViewById(R.id.btn_zf);
        pak_name = (TextView) findViewById(R.id.pub_number);
        pak_time =(TextView)findViewById(R.id.pub_time);
        pak_money = (TextView)findViewById(pub_money);
        btn_zf.setOnClickListener(this);
    }

    @Override
    protected void onStart() {

        super.onStart();

        _device = _bluetooth.getRemoteDevice(address);
        try{
            //先是创建一个socket先建立一个连接
            ClsUtils.setPin(_device.getClass(), _device, "1234");
            _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(GXCWUtil.MY_UUID));
        }catch(IOException e){
            Toast.makeText(UnlockActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();


        }catch (Exception e) {
            e.printStackTrace();
        }
        try{

            _socket.connect();
            name = _device.getName();
            //onSend("ifoo");
            onSend(GXCWUtil.GET_TIME);
            Toast.makeText(UnlockActivity.this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();

        }catch(IOException e){
            try{
                Toast.makeText(UnlockActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
                _socket.close();
                _socket = null;
            }catch(IOException ee){
                Toast.makeText(UnlockActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
        }
        //打开接收线程
        try{
            Toast.makeText(this, "创建数据流", Toast.LENGTH_SHORT).show();
            is = _socket.getInputStream();   //得到蓝牙数据输入流
        }catch(IOException e){
            Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(bThread==false){
            ReadThread.start();
            bThread=true;
        }else{
            bRun = true;
        }
    }
    private void initdata(){
        Intent intent = getIntent();
        bundle = intent.getExtras();

        address = bundle.getString("result");
        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

        /**
         *
         * 这一块是从服务器中找这个address是否。正确。咱这里是不是有这样
         * 并返回密码
         *
         * 同时开启蓝牙。。。
         * **/

        /**
         * 返回是否为真
         *
         * 连接后发送到ifoo 这样的字符串
         * */

    }

    //发送按键响应
    public void onSend(String data){


        int i=0;
        int n=0;
        try{
            Toast.makeText(this, "发送数据", Toast.LENGTH_SHORT).show();
            OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流

            byte[] bos = getHexBytes(data);

//    		for(i=0;i<bos.length;i++){
//    			if(bos[i]==0x0a)n++;
//    		}
//    		byte[] bos_new = new byte[bos.length+n];
//    		n=0;
//    		for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
//    			if(bos[i]==0x0a){
//    				bos_new[n]=0x0d;
//    				n++;
//    				bos_new[n]=0x0a;
//    			}else{
//    				bos_new[n]=bos[i];
//    			}
//    			n++;
//    		}

            os.write(bos);
            Toast.makeText(UnlockActivity.this, "开锁成功", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
        }
    }
    //接收数据线程
    Thread ReadThread=new Thread(){

        public void run(){
            //Toast.makeText(BTClient.this, "启动数据流", 1000).show();
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            bRun = true;
            //接收线程
            while(true){
                try{
                    //.available() 获取文件的大小
                    // 如果文件为空则就。。经行一个死循环
                    // 就进入这个里面出不出来了
                    while(is.available()==0){
                        while(bRun == false){
                        }
                    }
                    while(true){
                        num = is.read(buffer);         //读入数据
                        n=0;
                        String s0 = new String(buffer,0,num);
                        fmsg+=s0;    //保存收到数据
//						for(i=0;i<num;i++){
//							if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
//								buffer_new[n] = 0x0a;
//								i++;
//							}else{
//								buffer_new[n] = buffer[i];
//							}
//							n++;
//						}
                        //String s = new String(buffer_new,0,n);
                        smsg+=s0;   //写入接收缓存
                        smsg = HexStringToBytes(smsg);
                        if(is.available()==0)break;  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());
                }catch(IOException e){
                }
            }
        }
    };
    public static String HexStringToBytes(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }
    Handler handler= new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            pak_name.setText(name);
            pak_time.setText(smsg);   //显示数据
            //sv.scrollTo(0,dis.getMeasuredHeight()); //跳至数据最后一页
        }
    };


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(UnlockActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        //以下处请书写跳转到成功后的界面
                        onSend(GXCWUtil.SEND_BLUETHOOTH);

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UnlockActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        };
    };


    private byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }
    //关闭程序掉用处理部分
    public void onDestroy(){
        super.onDestroy();
        if(_socket!=null)  //关闭连接socket
            try{
                _socket.close();
            }catch(IOException e){}

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_zf:
                /**
                 * 启动一个副线程的。。经行支付
                 *
                 * 支付成功后然后给其中发送经行开锁
                 *
                 *
                 * */
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        double account = Double.parseDouble( pak_money.getText().toString());
                        Order order = new Order( MyInfoActivity.realToken ,account );
                        final String orderInfo = order.getOrderInfo();   // 订单信息,凭证
                        if( orderInfo.length() > 0   ){
                            PayTask alipay = new PayTask(UnlockActivity.this);
                            Map<String, String> result = alipay.payV2(orderInfo,true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                        else{
                            Log.i("PayActivity","订单凭证获得失败！" );
                        }
                    }
                }).start();

                //onSend(GXCWUtil.SEND_BLUETHOOTH);
                break;
            case R.id.title_img:
                Intent intent = new Intent(UnlockActivity.this,MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                try {
                    _socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            default:
                break;
        }
    }


}
