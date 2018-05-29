package cn.edu.zzti.soft.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.activity.MyInfoActivity;
import cn.edu.zzti.soft.activity.SetActivity;
import cn.edu.zzti.soft.dao.impl.PersonDao;
import cn.edu.zzti.soft.data.LQPreferences;


/**
 * 个人界面
 * 
 * */ 
public class MyInfoFragment extends Fragment implements OnClickListener{
	private ImageView imgview;
	private TextView text_name;
	private LinearLayout ll_gerenxinxi_layout;
	private LinearLayout ll_qianbao_layout;
	private LinearLayout ll_xiugaimima_layout;
	private LinearLayout ll_set_layout;

	private View view;

	private Context context;
	private LQPreferences lq ;
    private PersonDao persondao ;
	// 图片的数据
	private	ByteArrayOutputStream baos;

	private String imageName;
	private String phone;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int UPDATE_FXID = 4;
    private static final int UPDATE_NICK = 5;
    private static final int UPDATE_SIGN = 6;
    private static final String fileName ="/sdcard/gxcw/";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity();
		lq = LQPreferences.getLq(context);
		persondao = new PersonDao(context);
		view = inflater.inflate(R.layout.fragment_myinfo, container, false);
		init();
		listener();
		return view;
	}
	
	@Override
	public void onStart() {
		if(lq.getName()!=""){
			text_name.setText("Name:"+lq.getName());
		}
		if(phone!=null){
			byte[] dataImage =persondao.readImage(phone);
			if(dataImage!=null){
				Bitmap bitmap = BitmapFactory.decodeByteArray(dataImage, 0, dataImage.length);
				imgview.setImageBitmap(bitmap);
			}
		}
		super.onStart();
	}
	@SuppressLint("ShowToast")
	private void init(){
		imgview = (ImageView)view.findViewById(R.id.myinfo_img);
		text_name = (TextView) view.findViewById(R.id.title_name);
		ll_gerenxinxi_layout = (LinearLayout)view.findViewById(R.id.gerenxinxi_layout);
		ll_qianbao_layout = (LinearLayout)view.findViewById(R.id.qianbao_layout);
		ll_xiugaimima_layout = (LinearLayout)view.findViewById(R.id.xiugaimima_layout);
		ll_set_layout = (LinearLayout)view.findViewById(R.id.set_layout);
		phone = lq.getPhone();
	}
	
	private void listener(){
		ll_gerenxinxi_layout.setOnClickListener(this);
		ll_qianbao_layout.setOnClickListener(this);
		ll_xiugaimima_layout.setOnClickListener(this);
		ll_set_layout.setOnClickListener(this);
		imgview.setOnClickListener(this);
	}

	
	private void createdialog(){
		
		Dialog dia =new AlertDialog.Builder(context)
			.setTitle("选择图片").setNegativeButton("相册", new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					imageName = getNowTime()+".png";
					// 调用
					Intent intent = new Intent(Intent.ACTION_PICK,null);
					intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent, PHOTO_REQUEST_GALLERY);	
					
					
				}})
				.setPositiveButton("相机", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					imageName = getNowTime()+".png";
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
                             Uri.fromFile(new File(fileName, imageName)));
					startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				}
		}).create();
		dia.show();
    }
	
	@SuppressLint({ "ShowToast", "SdCardPath" })
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			
			startPhotoZoom(Uri.fromFile(new File(fileName, imageName)),480);
			break;
		case PHOTO_REQUEST_GALLERY://相册中选择
			if(data!=null){
				
				startPhotoZoom(data.getData(),480);
			}
			break;
		case PHOTO_REQUEST_CUT:
			
			baos= new ByteArrayOutputStream();
			Bitmap bitmap = BitmapFactory.decodeFile(fileName
					+ imageName);

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			imgview.setImageBitmap(bitmap);
			if(persondao.UpdateUserImage(lq.getPhone(), baos.toByteArray())){
				Toast.makeText(context, "图片存储失败", Toast.LENGTH_SHORT).show();
			}
			
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	 private void startPhotoZoom(Uri uri,int size){
		 Intent intent = new Intent("com.android.camera.action.CROP");
		 
		 intent.setDataAndType(uri, "image/*");
		 intent.putExtra("crop", "true");
		 intent.putExtra("aspectX", 1);
	     intent.putExtra("aspectY", 1);
	     intent.putExtra("outputX", size);
	     intent.putExtra("outputY", size);
	     intent.putExtra("scale", true);
	     intent.putExtra("return-data", true);
	     
	     intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(fileName, imageName)));
//	     // 这个是将图片经行压缩将图片的将其中存放在
	     intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
	     intent.putExtra("noFaceDetection", true); // no face detection
	     startActivityForResult(intent, PHOTO_REQUEST_CUT);
	 }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myinfo_img:
			createdialog();
			break;
		case R.id.gerenxinxi_layout:

			//seeUserDetail();
			Intent intent = new Intent(context, MyInfoActivity.class);
			startActivity(intent);

			break;
		case R.id.qianbao_layout:
			break;
		case R.id.xiugaimima_layout:
			break;
		case R.id.set_layout:	
			Intent intent2 = new Intent(context, SetActivity.class);
			context.startActivity(intent2);
			break;
		default:
			break;
		}
	}

	private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        return dateFormat.format(date);
   }
}
