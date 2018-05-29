package cn.edu.zzti.soft.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.activity.ChongZhiActivity;
import cn.edu.zzti.soft.data.LQPreferences;
import cn.edu.zzti.soft.zxing.ui.MipcaActivityCapture;


public class MainFragment extends Fragment implements OnClickListener{
	private Button btn_scan;
	private View view;
	private Context context;
	private RelativeLayout btn_number_open;
	private RelativeLayout btn_money;
	private TextView text_money;
	private LQPreferences lq = new LQPreferences();
	private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity();
		view = inflater.inflate(R.layout.fragment_main, container, false);
		init();
		initlistener();
		return view;
	}


	@Override
	public void onStart() {

		super.onStart();
		text_money.setText("余额:"+lq.getMoney()+"￥");
	}
	private void  init(){
		lq.init(context);
		text_money =(TextView) view.findViewById(R.id.text_yu_money);
		btn_scan =(Button) view.findViewById(R.id.btn_scan);
		btn_number_open = (RelativeLayout)view.findViewById(R.id.btn_number_open_clock);
		btn_money = (RelativeLayout)view.findViewById(R.id.btn_park_money);

		text_money.setText("余额:"+lq.getMoney()+"￥");

	}
	private void initlistener(){
		btn_scan.setOnClickListener(this);
		btn_number_open.setOnClickListener(this);
		btn_money.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_scan:
				if(_bluetooth.isEnabled()==false){
					createdialog();
				}else{
					Intent intent = new Intent(context,MipcaActivityCapture.class);
					startActivity(intent);
				}
				break;
			case R.id.btn_park_money:
				Intent intent2 = new Intent(context,ChongZhiActivity.class);
				startActivity(intent2);
			default:
				break;
		}

	}




	private void createdialog(){

		Dialog dia =new AlertDialog.Builder(context)
				.setTitle("打开蓝牙").setNegativeButton("打开", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						new Thread(){
							public void run(){
								_bluetooth.enable();
								Intent intent = new Intent(context,MipcaActivityCapture.class);
								startActivity(intent);
							}
						}.start();


					}}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {


					}

				}).create();
		dia.show();
	}
}
