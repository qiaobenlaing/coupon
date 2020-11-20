package cn.suanzi.baomi.shop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import cn.suanzi.baomi.shop.R.color;

/**
 * 日历
 * @author wensi.yu
 *
 */
public class CalendarElement extends TextView {

	public CalendarElement(Context context, AttributeSet attrs) {
        
        super(context, attrs);
    }
     
    public CalendarElement(Context context) {
        super(context);
         
    }
     
    public CalendarElement(Context context, AttributeSet attrs,int res) {
        super(context, attrs,res);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(color.light_grey);
        int startX = this.getWidth();
        int stopX = startX;
        int stopY = this.getHeight();
        //画边框   
	     canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
	    canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
	     canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
	     canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
     
    }
}
