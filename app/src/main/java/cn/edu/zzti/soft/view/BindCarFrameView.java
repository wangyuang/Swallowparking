package cn.edu.zzti.soft.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.zzti.soft.R;
import cn.edu.zzti.soft.activity.MainActivity;
import cn.edu.zzti.soft.dao.impl.PersonDao;
import cn.edu.zzti.soft.data.LQPreferences;

public class BindCarFrameView extends RelativeLayout {  
	  
    private static final String TAG = "BindCarFrameView";  
  
    private EditText editText;  
    private TextView[] TextViews;  
    private Activity mActivity;  
    private StringBuffer stringBuffer = new StringBuffer();  
    private int count = 7;  
    private String inputContent;  
    private static int ITEM_VIEW_COUNT = 7;  
    private OnFrameTouchListener mTouchListener = new OnFrameTouchListener();  
    private boolean isUpdateView = false;//是否更新view内容  
    private int updateViewPosition;  
    private int lastPosition;  
    private Button input_number_btn;
    
    private PersonDao p_dao;
    
    private static final int[] VIEW_IDS = new int[]{  
            R.id.item_code_iv1, R.id.item_code_iv2, R.id.item_code_iv3,
            R.id.item_code_iv4, R.id.item_code_iv5, R.id.item_code_iv6,  
            R.id.item_code_iv7, R.id.item_code_iv8  
    };
    
    private LQPreferences lq;
  
    public BindCarFrameView(Context context) {  
        this(context, null);  
    }  
  
    public BindCarFrameView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    public BindCarFrameView(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
        
        
        mActivity = (Activity) context;  
        lq = LQPreferences.getLq(context);
        p_dao = new PersonDao(mActivity);
        
       
        TextViews = new TextView[8];  
        View.inflate(context, R.layout.input_num, this);  
  
        int textsLength = VIEW_IDS.length;  
        for (int i = 0; i < textsLength; i++) {  
            //textview放进数组中，方便修改操作  
            TextViews[i] = (TextView) findViewById(VIEW_IDS[i]);  
            TextViews[i].setOnTouchListener(mTouchListener);  
            TextViews[i].setBackgroundResource(R.drawable.pp_bind_car_first_view_blue);
            if(i==textsLength-2){
            	TextViews[i].setBackgroundResource(R.drawable.last_text);
            }
        }  
        editText = (EditText) findViewById(R.id.item_edittext);  
       //第一个输入框默认设置点中效果  
        editText.setBackgroundResource(R.drawable.edit_bg);
        editText.setCursorVisible(false);//将光标隐藏  
        
        input_number_btn = (Button) findViewById(R.id.input_btn_que);
        setListener();  
    }  
  
  
    private void setListener() {  
    	
    	
    	input_number_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(p_dao.getPerson(lq.getPhone())==false){
					p_dao.addPerson(lq.getPhone(), stringBuffer.toString());
				}
				lq.savePass(stringBuffer.toString());
				Intent intent = new Intent(mActivity,MainActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
				
			}
		});
    	
  
    	Log.d(TAG, "sddddddd");
        editText.addTextChangedListener(new TextWatcher() {  
  
            @Override  
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  
  
            }  
  
            @Override  
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  
  
            }  
  
            @Override  
            public void afterTextChanged(Editable editable) {  
                //如果字符不为""时才进行操作  
                if (!editable.toString().equals("")) {  
                		
                    if (stringBuffer.length() > ITEM_VIEW_COUNT - 1) {  
                        //当文本长度大于 ITEM_VIEW_COUNT - 1 位时 EditText 置空  
                        editText.setText("");   
                        return;  
                    } else {  
                        //将文字添加到 StringBuffer 中  
                        stringBuffer.append(editable);  
                        editText.setText("");//添加后将 EditText 置空  造成没有文字输入的错局  
                        count = stringBuffer.length();//记录 stringBuffer 的长度  
                        //这个是记录其中传入的数据
                        inputContent = stringBuffer.toString();  
                        Toast.makeText(mActivity, inputContent+"长度"+inputContent.length(), Toast.LENGTH_SHORT).show();
                        if(inputContent.length()==7){
                        	 input_number_btn.setEnabled(true);
                        	 Toast.makeText(mActivity, "sssss", Toast.LENGTH_SHORT).show();
                        }
                    }  
              
                    for (int i = 0; i < stringBuffer.length(); i++) {  
                        Log.e(TAG, "stringBuffer.length(): " + stringBuffer.length() + "----I---" + i);  
                        TextViews[i].setText(String.valueOf(inputContent.charAt(i)));   
                    }  
  
                }  
            }

		
        });  
  
        editText.setOnKeyListener(new OnKeyListener() {  
            @Override  
            public boolean onKey(View v, int keyCode, KeyEvent event) {  
            	
                if (keyCode == KeyEvent.KEYCODE_DEL  
                        && event.getAction() == KeyEvent.ACTION_DOWN) {  
                    if (onKeyDelete()) return true;  
                    return true;  
                }  
                return false;  
            }

		
        });  
    }  
  
    /** 
     * 显示 8 个输入框 
     */  
    public boolean showLastView() {  
        TextViews[7].setVisibility(VISIBLE);  
       
        ITEM_VIEW_COUNT = 8;  
        return true;  
    }  
  
    public boolean hideLastView() {  
        TextViews[7].setVisibility(GONE);  
        if (stringBuffer.length() == 8) {  
            TextViews[7].setText("");  
            stringBuffer.delete(7, 8);  
      
            inputContent = stringBuffer.toString();  
            count = stringBuffer.length();  
           
        }
        ITEM_VIEW_COUNT = 7;  
        return false;  
    }  
  
    public boolean onKeyDelete() {  
        if (count == 0) {  
            count = 7;  
            return true;  
        }  
        if (stringBuffer.length() > 0) {  
            //删除相应位置的字符  
            stringBuffer.delete((count - 1), count);  
            count--;  
            Log.d(TAG, "onKeyDelete: stringBuffer.length() is " + stringBuffer.length());  
          
            inputContent = stringBuffer.toString();  
            TextViews[stringBuffer.length()].setText("");
            input_number_btn.setEnabled(false);
        }  
        
    
        return false;  
    }  
  
    /** 
     * 清空输入内容 
     */  
    public void clearEditText() {  
        stringBuffer.delete(0, stringBuffer.length());  
        inputContent = stringBuffer.toString();  
        for (int i = 0; i < TextViews.length; i++) {  
            TextViews[i].setText("");  
        }  
    }  
  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        return super.onKeyDown(keyCode, event);  
    }  
  

  
    /** 
     * 获取输入文本 
     * 
     * @return 
     */  
    public String getEditContent() {  
        return inputContent;  
    }  
  
    /** 
     * 设置 EditText 的输入内容 
     * 根据isUpdateView 判断修改/删除操作 
     */  
    public void setEditContent(String content) {  
        if (!isUpdateView) {  
            if (!TextUtils.isEmpty(content)) {  
                editText.setText(content);  
            } else {  
                onKeyDelete();  
            }  
        } else {  
            if (!TextUtils.isEmpty(content)) {  
                stringBuffer.replace(updateViewPosition, updateViewPosition + 1, content);  
                isUpdateView = !isUpdateView;  
            } else {  
                TextViews[updateViewPosition].setText(content);  
                if (updateViewPosition + 1 == ITEM_VIEW_COUNT) {  
                    isUpdateView = !isUpdateView;  
                    stringBuffer.delete(updateViewPosition, updateViewPosition + 1);  
                    count--;  
                }  
                return;  
            }  
            TextViews[updateViewPosition].setText(content);  
            inputContent = stringBuffer.toString();  
            count = stringBuffer.length();  
            Log.e(TAG, "setEditContent===inputContent==" + inputContent + "------" + count + "---" + ITEM_VIEW_COUNT);  
        
         
        }  
    }  
  
    /** 
     * 显示输入框的TouchListener 
     */  
    private class OnFrameTouchListener implements OnTouchListener {  
  
        @Override  
        public boolean onTouch(View view, MotionEvent event) {  
            if (view instanceof TextView) {  
                TextView tv = (TextView) view;  
                tv.setFocusable(true);  
                String tvString = (String) tv.getText();  
                if (TextUtils.isEmpty(tvString)) {  
                    isUpdateView = false;  
                    return false;  
                }  
                int viewId = tv.getId();  
                for (int i = 0; i < stringBuffer.length(); i++) {  
                    if (viewId == VIEW_IDS[i]) {  
                        updateViewPosition = i;  
                        if (i == 0) {  
                            isUpdateView = true;  
                        } else if (i == 1) {  
                            
                            isUpdateView = true;  
                            
                        } else if (i < ITEM_VIEW_COUNT - 1 && i > 1) {  
                        
                            isUpdateView = true;  
                          
                        } else {  
                            isUpdateView = true;  
                         
                        }  
                        lastPosition = i;  
                    }  
                }  
            }  
            return true;  
        }

	
    }  
 
}
