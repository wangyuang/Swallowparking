package cn.edu.zzti.soft.zxing.ui;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cn.edu.zzti.soft.zxing.camera.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import com.google.zxing.Result;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.activity.UnlockActivity;
import cn.edu.zzti.soft.zxing.decoding.CaptureActivityHandler;
import cn.edu.zzti.soft.zxing.decoding.InactivityTimer;
import cn.edu.zzti.soft.zxing.decoding.RGBLuminanceSource;
import cn.edu.zzti.soft.zxing.view.ViewfinderView;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Initial the camera
 * @author Ryan.Tang
 *
 *
 *
 */
public class MipcaActivityCapture extends Activity implements SurfaceHolder.Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;// 一个计时器
	private MediaPlayer mediaPlayer;   // 这个是播放媒体
	private boolean playBeep; // 是否播放媒体
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ProgressDialog mProgress;
	private static final int PARSE_BARCODE_SUC = 300;
	private static final int PARSE_BARCODE_FAIL = 303;
	/** Called when the activity is first created. */

	public static String resultString;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);

		//相机初始化
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

		/*ImageView mButtonBack = (ImageView) findViewById(R.id.back_img);
		// 将这个界面的给撤销掉
		mButtonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MipcaActivityCapture.this.finish();

			}
		});
*/

		hasSurface = false;
		// 新建一个 计时类
		inactivityTimer = new InactivityTimer(this);
	}

	/**
	 * 跳转到显示的页面
	 * @param resultString
	 * @param bitmap
	 *
	 *
	 * 这里的扫描的结果返回 和 二维码的图片返回 结果界面上
	 */
	private void onResultHandler(String resultString, Bitmap bitmap){
		if(TextUtils.isEmpty(resultString)){
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(MipcaActivityCapture.this,UnlockActivity.class);

		Bundle bundle = new Bundle();

		bundle.putString("result", resultString);

		intent.putExtras(bundle);
		this.startActivity(intent);
	}


	/*
	 *
	 * 解析手机自带的二维码图片
	 *
	 * */

	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8"); //设置二维码内容的编码

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		// 这个是将图片bitmap的形式。
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 可以直接从内存或者DMA等硬件接口取得图像数据，
	 * 是个非常重要的绘图视图。
	 * 它的特性是：可以在主线程之外的线程中向屏幕绘图上。
	 * 这样可以避免画图任务繁重的时候造成主线程阻塞，从而提高了程序的反应速度
	 * */
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		//SurfaceView.getHolder()获得SurfaceHolder对象
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			//这个是改变其中的 视图
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}

		vibrate = true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理扫描结果
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "二维码错误", Toast.LENGTH_SHORT).show();
		}else {
			onResultHandler(resultString, barcode);
		}
		MipcaActivityCapture.this.finish();
	}


	/**
	 *
	 * 监视对画布的 Carve 这些的变化 
	 *
	 * SurfaceHolder 
	 * 是用来SurfaceHolder,surface的控制器，
	 * 用来操纵surface。处理它的Canvas上画的效果和动画，控制表面，大小，像素等。
	 * */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}



	private static final long VIBRATE_DURATION = 200L;


	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private String photo_path;
	private Bitmap scanBitmap;

}