package cn.edu.zzti.soft.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import cn.edu.zzti.soft.R;

public class MyEditView extends AppCompatEditText implements OnFocusChangeListener, TextWatcher{

	private Context context;
	private boolean hasFoucs;
	private Drawable drawable;
	public MyEditView(Context context) {
		this(context,null);
		this.context = context;

	}
	public MyEditView(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
		this.context = context;
	}

	public MyEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	private void init(){
		drawable = context.getResources().getDrawable(R.drawable.hintcard_close);

		drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);

	}

	protected void setClearIconVisible(boolean visible) {
		//这个
		Drawable right = visible ? drawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		// 如果其中改变则设置其中为
		if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {


	}
	@Override
	public void afterTextChanged(Editable s) {


	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;

		// 如果的改变则
		if (hasFocus) {
			// 将其中变成true
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 这个是给图片设其中的监听
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
				}
			}
		}


		return super.onTouchEvent(event);
	}


}