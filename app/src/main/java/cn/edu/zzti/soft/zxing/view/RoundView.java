package cn.edu.zzti.soft.zxing.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;


public class RoundView extends AppCompatImageView {

	 public RoundView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public RoundView(Context context) {
	        super(context);
	        init();
	    }

	    private final RectF roundRect = new RectF();
	    private float rect_adius = 90;
	    private final Paint maskPaint = new Paint();
	    private final Paint zonePaint = new Paint();

	    private void init() {
	    	//会使图片绘制的速度变慢
	        maskPaint.setAntiAlias(true);
	        // 设置图片重叠处理方式的
	        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	        maskPaint.setFilterBitmap(true);
	        zonePaint.setAntiAlias(true);
	        zonePaint.setColor(Color.WHITE);
	        zonePaint.setFilterBitmap(true);
	        // 这个是将其变成像素
	        float density = getResources().getDisplayMetrics().density;
	        rect_adius = rect_adius * density;
	    }

	    public void setRectAdius(float adius) {
	        rect_adius = adius;
	        invalidate();
	    }

	    @Override
	    protected void onLayout(boolean changed, int left, int top, int right,
	            int bottom) {
	        super.onLayout(changed, left, top, right, bottom);
	        int w = getWidth();
	        int h = getHeight();
	        roundRect.set(0, 0, w, h);
	    }

	    @Override
	    public void draw(Canvas canvas) {
	        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG);
	        canvas.drawRoundRect(roundRect, rect_adius, rect_adius, zonePaint);
	        //
	        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG);
	        super.draw(canvas);
	        canvas.restore();
	    }

	
}
