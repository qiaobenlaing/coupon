package cn.suanzi.baomi.shop.util;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * 图文编辑
 * @author wensi.yu
 *
 */
public class MyEditText extends EditText {
	private int windowWidth;
	private int windowHeight;
	
	public MyEditText(Context context) {
		super(context);
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowWidth = manager.getDefaultDisplay().getWidth();
		windowHeight = manager.getDefaultDisplay().getHeight();
	}
	

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowWidth = manager.getDefaultDisplay().getWidth();
		windowHeight = manager.getDefaultDisplay().getHeight();
		
	}

	public void insertDrawable(int id) {
		/*final SpannableString ss = new SpannableString("aa");
		Drawable d = getResources().getDrawable(id);
		d.setBounds(0, 0, windowWidth, windowHeight / 3);
		ImageSpan span = new ImageSpan(d);
		ss.setSpan(span, 0, "aa".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		append(ss);
		append("\n");
		this.setText(ss);*/
		Editable eb = this.getEditableText();
		//获得光标所在位置
		int qqPosition = this.getSelectionStart();
		SpannableString ss = new SpannableString("1111");

		//定义插入图片
		Drawable drawable = getResources().getDrawable(id);
		ss.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE), 0 , ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		drawable.setBounds(2 , 0 , drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		//插入图片
		eb.insert(qqPosition, ss);
	} 
}
