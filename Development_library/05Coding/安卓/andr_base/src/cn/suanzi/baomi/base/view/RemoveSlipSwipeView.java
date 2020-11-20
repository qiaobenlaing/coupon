package cn.suanzi.baomi.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
/**
 * 银行列表项的侧滑删除
 * @author yingchen
 *
 */
public class RemoveSlipSwipeView extends FrameLayout {
	//内容
	private RelativeLayout mSwipeViewCotent;
	//删除按钮
	private TextView mSwipeViewDelete;
	
	//内容宽度
	private int mContentWidth;
	//删除按钮宽度
	private int mMenuWidth;
	//视图高度
	private int mViewHeight;
	
	private Scroller mScroller;
	
	private boolean openMenu = false;
	
	public RemoveSlipSwipeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mSwipeViewCotent = (RelativeLayout) getChildAt(0);
		mSwipeViewDelete = (TextView) getChildAt(1);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContentWidth = mSwipeViewCotent.getMeasuredWidth();
		mViewHeight = mSwipeViewCotent.getMeasuredHeight();
		mMenuWidth = mSwipeViewDelete.getMeasuredWidth();
	}
	
	//重新布局
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mSwipeViewDelete.layout(mContentWidth, 0, mContentWidth+mMenuWidth, mViewHeight);
	}
	
	private int startX;
	private int startY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: //按下
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE: //移动
			int moveX = (int) event.getRawX();
			int moveY = (int) event.getRawY();
			int distanceX = moveX-startX;
			int distanceY = moveY-startY;
			//得到最新需要的偏移量
			
			//水平方向的移动距离大于垂直方向的移动距离
			if(Math.abs(moveX)>Math.abs(moveY)){
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			
			int newScrollX = getScrollX()-distanceX;// distanceX<0
			if(newScrollX>=0&&newScrollX<=mMenuWidth){
				scrollTo(getScrollX()-distanceX, getScrollY());
			}
			startX = moveX;
			break;
		case MotionEvent.ACTION_UP: //松开
			int scrollX = getScrollX();
			if(scrollX<mMenuWidth/2){
				openMenu = false;
			}else{
				openMenu = true;
			}
			flushState();
			break;
		default:
			break;
		}
		
		return true;
	}
	
	public  void flushState(){
		if(openMenu){  //打开菜单
			mScroller.startScroll(getScrollX(), 0, -getScrollX()+mMenuWidth, 0);
		}else{  //关闭菜单
			mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0);
		}
		//强制重绘
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()) {
			//立即滚动一点
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			//强制重绘
			invalidate();
		}
	}

}
