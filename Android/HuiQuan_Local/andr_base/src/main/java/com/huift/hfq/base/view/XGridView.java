/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.huift.hfq.base.view;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Scroller;
import android.widget.WrapperListAdapter;
import com.huift.hfq.base.R;

public class XGridView extends GridView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXGridViewListener mListViewListener;

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 10; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	private int footerHeight=PULL_LOAD_MORE_DELTA;
	private boolean pullable=false;
	private boolean isUp=false;
	private int mVisible=0;
	private int maxY=0;
	/**
	 * @param context
	 */
	public XGridView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);
		footerHeight=(int)context.getResources().getDimension(R.dimen.load_more_margin);
		// init footer view
		mFooterView = new XListViewFooter(context);
	}



	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			addFooterView(mFooterView);
			mPullLoading = false;
			mFooterView.setBigFooter(false);
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}


	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}


	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXGScrollListener) {
			OnXGScrollListener l = (OnXGScrollListener) mScrollListener;
			l.onXGScrolling(this);
		}
	}

	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if(maxY<height){
				maxY=height;
			}
			int limit=PULL_LOAD_MORE_DELTA+mFooterView.getMargin()+10;//临界值
			if (maxY > limit&&pullable) { // height enough to invoke load
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		if(height>footerHeight){
			height=footerHeight;
		}
		mFooterView.setBottomMargin(height);
		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			maxY=0;
			mLastY = ev.getRawY();
			break;
		}
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if ((mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.
				if(mEnablePullLoad&&!mPullLoading){
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
			} 
			break;
		default:
			mLastY = -1; // reset
			//System.out.println("===:"+mEnablePullLoad+" fbm:"+mFooterView.getBottomMargin()+" m:"+mFooterView.getMargin());
			if(mFooterView.getState()!=XListViewFooter.STATE_READY){
				break ;
			}
			if (mEnablePullLoad
					&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
				if(isUp&&pullable&&getLastVisiblePosition()==mTotalItemCount-1){
					startLoadMore();
					resetFooterHeight();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mFooterView.setBottomMargin(mScroller.getCurrY());
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mVisible!=firstVisibleItem){
			if(mVisible<firstVisibleItem){
				isUp=true;
			}else{
				isUp=false;
			}
		}
		mVisible=firstVisibleItem;
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if(firstVisibleItem+visibleItemCount>=totalItemCount){
			pullable=true;
		}else{
			pullable=false;
		}
		//System.out.println("==firstVisibleItem:"+firstVisibleItem+" visibleItemCount:"+visibleItemCount+" totalItemCount:"+totalItemCount);
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXGridViewListener(IXGridViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXGScrollListener extends OnScrollListener {
		public void onXGScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXGridViewListener {

		public void onLoadMore();
	}
	


	public static boolean DEBUG = false;

    /**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
    private static class FixedViewInfo {
        /**
         * The view to add to the grid
         */
        public View view;
        public ViewGroup viewContainer;
        /**
         * The data backing the view. This is returned from {@link ListAdapter#getItem(int)}.
         */
        public Object data;
        /**
         * <code>true</code> if the fixed view should be selectable in the grid
         */
        public boolean isSelectable;
    }

    private int mNumColumns = AUTO_FIT;
    private View mViewForMeasureRowHeight = null;
    private int mRowHeight = -1;
    private static final String LOG_TAG = "grid-view-with-header-and-footer";

    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<FixedViewInfo>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<FixedViewInfo>();


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter instanceof HeaderViewGridAdapter) {
            ((HeaderViewGridAdapter) adapter).setNumColumns(getNumColumnsCompatible());
            ((HeaderViewGridAdapter) adapter).setRowHeight(getRowHeight());
        }
    }

    @Override
    public void setClipChildren(boolean clipChildren) {
        // Ignore, since the header rows depend on not being clipped
    }

    /**
     * Do not call this method unless you know how it works.
     *
     * @param clipChildren
     */
    public void setClipChildrenSupper(boolean clipChildren) {
        super.setClipChildren(false);
    }

    /**
     * Add a fixed view to appear at the top of the grid. If addHeaderView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p/>
     * NOTE: Call this before calling setAdapter. This is so HeaderGridView can wrap
     * the supplied cursor with one that will also account for header views.
     *
     * @param v The view to add.
     */
    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    /**
     * Add a fixed view to appear at the top of the grid. If addHeaderView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p/>
     * NOTE: Call this before calling setAdapter. This is so HeaderGridView can wrap
     * the supplied cursor with one that will also account for header views.
     *
     * @param v            The view to add.
     * @param data         Data to associate with this view
     * @param isSelectable whether the item is selectable
     */
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        ListAdapter adapter = getAdapter();
        if (adapter != null && !(adapter instanceof HeaderViewGridAdapter)) {
            throw new IllegalStateException(
                    "Cannot add header view to grid -- setAdapter has already been called.");
        }
        mHeaderViewInfos.clear();
        ViewGroup.LayoutParams lyp = v.getLayoutParams();

        FixedViewInfo info = new FixedViewInfo();
        FrameLayout fl = new FullWidthFixedViewLayout(getContext());

        if (lyp != null) {
            v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
            fl.setLayoutParams(new AbsListView.LayoutParams(lyp.width, lyp.height));
        }
        fl.addView(v);
        info.view = v;
        info.viewContainer = fl;
        info.data = data;
        info.isSelectable = isSelectable;
        mHeaderViewInfos.add(info);
        // in the case of re-adding a header view, or adding one later on,
        // we need to notify the observer
        if (adapter != null) {
            ((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        ListAdapter mAdapter = getAdapter();
        if (mAdapter != null && !(mAdapter instanceof HeaderViewGridAdapter)) {
            throw new IllegalStateException(
                    "Cannot add header view to grid -- setAdapter has already been called.");
        }
        mFooterViewInfos.clear();
        ViewGroup.LayoutParams lyp = v.getLayoutParams();

        ViewGroup parent=(ViewGroup)v.getParent();
        if(parent!=null){
        	parent.removeView(v);
        }
        FixedViewInfo info = new FixedViewInfo();
        FrameLayout fl = new FullWidthFixedViewLayout(getContext());
        fl.setBackgroundDrawable(null);
        if (lyp != null) {
            v.setLayoutParams(new FrameLayout.LayoutParams(lyp.width, lyp.height));
            fl.setLayoutParams(new AbsListView.LayoutParams(lyp.width, lyp.height));
        }
        fl.addView(v);
        info.view = v;
        info.viewContainer = fl;
        info.data = data;
        info.isSelectable = isSelectable;
        mFooterViewInfos.add(info);

        if (mAdapter != null) {
            ((HeaderViewGridAdapter) mAdapter).notifyDataSetChanged();
        }
    }

    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }

    /**
     * Removes a previously-added header view.
     *
     * @param v The view to remove
     * @return true if the view was removed, false if the view was not a header
     * view
     */
    public boolean removeHeaderView(View v) {
        if (mHeaderViewInfos.size() > 0) {
            boolean result = false;
            ListAdapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewGridAdapter) adapter).removeHeader(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViewInfos);
            return result;
        }
        return false;
    }

    /**
     * Removes a previously-added footer view.
     *
     * @param v The view to remove
     * @return true if the view was removed, false if the view was not a header
     * view
     */
    public boolean removeFooterView(View v) {
    	if(v==null){
    		return false;
    	}
        if (mFooterViewInfos.size() > 0) {
            boolean result = false;
            ListAdapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewGridAdapter) adapter).removeFooter(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mFooterViewInfos);
            return result;
        }
        return false;
    }
    
    public boolean hasFooter() {
    	if(mFooterViewInfos==null||mFooterViewInfos.size()==0){
    		return false;
    	}
    	return true;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

    @TargetApi(11)
    private int getNumColumnsCompatible() {
        if (Build.VERSION.SDK_INT >= 11) {
            return super.getNumColumns();
        } else {
            try {
                Field numColumns = getClass().getSuperclass().getDeclaredField("mNumColumns");
                numColumns.setAccessible(true);
                return numColumns.getInt(this);
            } catch (Exception e) {
                if (mNumColumns != -1) {
                    return mNumColumns;
                }
                throw new RuntimeException("Can not determine the mNumColumns for this API platform, please call setNumColumns to set it.");
            }
        }
    }

    private int getColumnWidthCompatible() {
    	try {
            Field numColumns = getClass().getSuperclass().getDeclaredField("mColumnWidth");
            numColumns.setAccessible(true);
            return numColumns.getInt(this);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    	/*
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getColumnWidth();
        } else {
            try {
                Field numColumns = getClass().getSuperclass().getDeclaredField("mColumnWidth");
                numColumns.setAccessible(true);
                return numColumns.getInt(this);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }*/
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewForMeasureRowHeight = null;
    }

    public void invalidateRowHeight() {
        mRowHeight = -1;
    }

    public int getRowHeight() {
        if (mRowHeight > 0) {
            return mRowHeight;
        }
        ListAdapter adapter = getAdapter();
        int numColumns = getNumColumnsCompatible();

        // adapter has not been set or has no views in it;
        if (adapter == null || adapter.getCount() <= numColumns * (mHeaderViewInfos.size() + mFooterViewInfos.size())) {
            return -1;
        }
        int mColumnWidth = getColumnWidthCompatible();
        View view = getAdapter().getView(numColumns * mHeaderViewInfos.size(), mViewForMeasureRowHeight, this);
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) view.getLayoutParams();
        if (p == null) {
            p = new AbsListView.LayoutParams(-1, -2, 0);
            view.setLayoutParams(p);
        }
        int childHeightSpec = getChildMeasureSpec(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), 0, p.height);
        int childWidthSpec = getChildMeasureSpec(
                MeasureSpec.makeMeasureSpec(mColumnWidth, MeasureSpec.EXACTLY), 0, p.width);
        view.measure(childWidthSpec, childHeightSpec);
        mViewForMeasureRowHeight = view;
        mRowHeight = view.getMeasuredHeight();
        return mRowHeight;
    }

    @TargetApi(11)
    public void tryToScrollToBottomSmoothly() {
        int lastPos = getAdapter().getCount() - 1;
        if (Build.VERSION.SDK_INT >= 11) {
            smoothScrollToPositionFromTop(lastPos, 0);
        } else {
            setSelection(lastPos);
        }
    }

    @TargetApi(11)
    public void tryToScrollToBottomSmoothly(int duration) {
        int lastPos = getAdapter().getCount() - 1;
        if (Build.VERSION.SDK_INT >= 11) {
            smoothScrollToPositionFromTop(lastPos, 0, duration);
        } else {
            setSelection(lastPos);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
    	if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
		}
        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            HeaderViewGridAdapter headerViewGridAdapter = new HeaderViewGridAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
            int numColumns = getNumColumnsCompatible();
            if (numColumns > 1) {
                headerViewGridAdapter.setNumColumns(numColumns);
            }
            headerViewGridAdapter.setRowHeight(getRowHeight());
            super.setAdapter(headerViewGridAdapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    /**
     * full width
     */
    private class FullWidthFixedViewLayout extends FrameLayout {

        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            int realLeft = XGridView.this.getPaddingLeft() + getPaddingLeft();
            // Try to make where it should be, from left, full width
            if (realLeft != left) {
                offsetLeftAndRight(realLeft - left);
            }
            super.onLayout(changed, left, top, right, bottom);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int targetWidth = XGridView.this.getMeasuredWidth()
                    - XGridView.this.getPaddingLeft()
                    - XGridView.this.getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        mNumColumns = numColumns;
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter instanceof HeaderViewGridAdapter) {
            ((HeaderViewGridAdapter) adapter).setNumColumns(numColumns);
        }
    }

    /**
     * ListAdapter used when a HeaderGridView has header views. This ListAdapter
     * wraps another one and also keeps track of the header views and their
     * associated data objects.
     * <p>This is intended as a base class; you will probably not need to
     * use this class directly in your own code.
     */
    private static class HeaderViewGridAdapter implements WrapperListAdapter, Filterable {
        // This is used to notify the container of updates relating to number of columns
        // or headers changing, which changes the number of placeholders needed
        private final DataSetObservable mDataSetObservable = new DataSetObservable();
        private final ListAdapter mAdapter;
        static final ArrayList<FixedViewInfo> EMPTY_INFO_LIST =
                new ArrayList<FixedViewInfo>();

        // This ArrayList is assumed to NOT be null.
        ArrayList<FixedViewInfo> mHeaderViewInfos;
        ArrayList<FixedViewInfo> mFooterViewInfos;
        private int mNumColumns = 1;
        private int mRowHeight = -1;
        boolean mAreAllFixedViewsSelectable;
        private final boolean mIsFilterable;
        private boolean mCachePlaceHoldView = true;
        // From Recycle Bin or calling getView, this a question...
        private boolean mCacheFirstHeaderView = false;

        public HeaderViewGridAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footViewInfos, ListAdapter adapter) {
            mAdapter = adapter;
            mIsFilterable = adapter instanceof Filterable;
            if (headerViewInfos == null) {
                mHeaderViewInfos = EMPTY_INFO_LIST;
            } else {
                mHeaderViewInfos = headerViewInfos;
            }

            if (footViewInfos == null) {
                mFooterViewInfos = EMPTY_INFO_LIST;
            } else {
                mFooterViewInfos = footViewInfos;
            }
            mAreAllFixedViewsSelectable = areAllListInfosSelectable(mHeaderViewInfos)
                    && areAllListInfosSelectable(mFooterViewInfos);
        }

        public void setNumColumns(int numColumns) {
            if (numColumns < 1) {
                return;
            }
            if (mNumColumns != numColumns) {
                mNumColumns = numColumns;
                notifyDataSetChanged();
            }
        }

        public void setRowHeight(int height) {
            mRowHeight = height;
        }

        public int getHeadersCount() {
            return mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return mFooterViewInfos.size();
        }

        @Override
        public boolean isEmpty() {
            return (mAdapter == null || mAdapter.isEmpty()) && getHeadersCount() == 0 && getFootersCount() == 0;
        }

        private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
            if (infos != null) {
                for (FixedViewInfo info : infos) {
                    if (!info.isSelectable) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean removeHeader(View v) {
            for (int i = 0; i < mHeaderViewInfos.size(); i++) {
                FixedViewInfo info = mHeaderViewInfos.get(i);
                if (info.view == v) {
                    mHeaderViewInfos.remove(i);
                    mAreAllFixedViewsSelectable =
                            areAllListInfosSelectable(mHeaderViewInfos) && areAllListInfosSelectable(mFooterViewInfos);
                    mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        public boolean removeFooter(View v) {
            for (int i = 0; i < mFooterViewInfos.size(); i++) {
                FixedViewInfo info = mFooterViewInfos.get(i);
                if (info.view == v) {
                    mFooterViewInfos.remove(i);
                    mAreAllFixedViewsSelectable =
                            areAllListInfosSelectable(mHeaderViewInfos) && areAllListInfosSelectable(mFooterViewInfos);
                    mDataSetObservable.notifyChanged();
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getCount() {
            if (mAdapter != null) {
                return (getFootersCount() + getHeadersCount()) * mNumColumns + getAdapterAndPlaceHolderCount();
            } else {
                return (getFootersCount() + getHeadersCount()) * mNumColumns;
            }
        }

        @Override
        public boolean areAllItemsEnabled() {
            if (mAdapter != null) {
                return mAreAllFixedViewsSelectable && mAdapter.areAllItemsEnabled();
            } else {
                return true;
            }
        }

        private int getAdapterAndPlaceHolderCount() {
            final int adapterCount = (int) (Math.ceil(1f * mAdapter.getCount() / mNumColumns) * mNumColumns);
            return adapterCount;
        }

        @Override
        public boolean isEnabled(int position) {
            // Header (negative positions will throw an IndexOutOfBoundsException)
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                return position % mNumColumns == 0
                        && mHeaderViewInfos.get(position / mNumColumns).isSelectable;
            }

            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition < adapterCount) {
                    return adjPosition < mAdapter.getCount() && mAdapter.isEnabled(adjPosition);
                }
            }

            // Footer (off-limits positions will throw an IndexOutOfBoundsException)
            final int footerPosition = adjPosition - adapterCount;
            return footerPosition % mNumColumns == 0
                    && mFooterViewInfos.get(footerPosition / mNumColumns).isSelectable;
        }

        @Override
        public Object getItem(int position) {
            // Header (negative positions will throw an ArrayIndexOutOfBoundsException)
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                if (position % mNumColumns == 0) {
                    return mHeaderViewInfos.get(position / mNumColumns).data;
                }
                return null;
            }

            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition < adapterCount) {
                    if (adjPosition < mAdapter.getCount()) {
                        return mAdapter.getItem(adjPosition);
                    } else {
                        return null;
                    }
                }
            }

            // Footer (off-limits positions will throw an IndexOutOfBoundsException)
            final int footerPosition = adjPosition - adapterCount;
            if (footerPosition % mNumColumns == 0) {
                return mFooterViewInfos.get(footerPosition).data;
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (mAdapter != null && position >= numHeadersAndPlaceholders) {
                int adjPosition = position - numHeadersAndPlaceholders;
                int adapterCount = mAdapter.getCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public boolean hasStableIds() {
            if (mAdapter != null) {
                return mAdapter.hasStableIds();
            }
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (DEBUG) {
                Log.d(LOG_TAG, String.format("getView: %s, reused: %s", position, convertView == null));
            }
            // Header (negative positions will throw an ArrayIndexOutOfBoundsException)
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                View headerViewContainer = mHeaderViewInfos
                        .get(position / mNumColumns).viewContainer;
                if (position % mNumColumns == 0) {
                    return headerViewContainer;
                } else {
                    if (convertView == null) {
                        convertView = new View(parent.getContext());
                    }
                    // We need to do this because GridView uses the height of the last item
                    // in a row to determine the height for the entire row.
                    convertView.setVisibility(View.INVISIBLE);
                    convertView.setMinimumHeight(headerViewContainer.getHeight());
                    return convertView;
                }
            }
            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition < adapterCount) {
                    if (adjPosition < mAdapter.getCount()) {
                        View view = mAdapter.getView(adjPosition, convertView, parent);
                        return view;
                    } else {
                        if (convertView == null) {
                            convertView = new View(parent.getContext());
                        }
                        convertView.setVisibility(View.INVISIBLE);
                        convertView.setMinimumHeight(mRowHeight);
                        return convertView;
                    }
                }
            }
            // Footer
            final int footerPosition = adjPosition - adapterCount;
            if (footerPosition < getCount()) {
                View footViewContainer = mFooterViewInfos
                        .get(footerPosition / mNumColumns).viewContainer;
                if (position % mNumColumns == 0) {
                    return footViewContainer;
                } else {
                    if (convertView == null) {
                        convertView = new View(parent.getContext());
                    }
                    // We need to do this because GridView uses the height of the last item
                    // in a row to determine the height for the entire row.
                    convertView.setVisibility(View.INVISIBLE);
                    convertView.setMinimumHeight(footViewContainer.getHeight());
                    return convertView;
                }
            }
            throw new ArrayIndexOutOfBoundsException(position);
        }

        @Override
        public int getItemViewType(int position) {

            final int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            final int adapterViewTypeStart = mAdapter == null ? 0 : mAdapter.getViewTypeCount() - 1;
            int type = AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
            if (mCachePlaceHoldView) {
                // Header
                if (position < numHeadersAndPlaceholders) {
                    if (position == 0) {
                        if (mCacheFirstHeaderView) {
                            type = adapterViewTypeStart + mHeaderViewInfos.size() + mFooterViewInfos.size() + 1 + 1;
                        }
                    }
                    if (position % mNumColumns != 0) {
                        type = adapterViewTypeStart + (position / mNumColumns + 1);
                    }
                }
            }

            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = getAdapterAndPlaceHolderCount();
                if (adjPosition >= 0 && adjPosition < adapterCount) {
                    if (adjPosition < mAdapter.getCount()) {
                        type = mAdapter.getItemViewType(adjPosition);
                    } else {
                        if (mCachePlaceHoldView) {
                            type = adapterViewTypeStart + mHeaderViewInfos.size() + 1;
                        }
                    }
                }
            }

            if (mCachePlaceHoldView) {
                // Footer
                final int footerPosition = adjPosition - adapterCount;
                if (footerPosition >= 0 && footerPosition < getCount() && (footerPosition % mNumColumns) != 0) {
                    type = adapterViewTypeStart + mHeaderViewInfos.size() + 1 + (footerPosition / mNumColumns + 1);
                }
            }
            if (DEBUG) {
                Log.d(LOG_TAG, String.format("getItemViewType: pos: %s, result: %s", position, type, mCachePlaceHoldView, mCacheFirstHeaderView));
            }
            return type;
        }

        /**
         * content view, content view holder, header[0], header and footer placeholder(s)
         *
         * @return
         */
        @Override
        public int getViewTypeCount() {
            int count = mAdapter == null ? 1 : mAdapter.getViewTypeCount();
            if (mCachePlaceHoldView) {
                int offset = mHeaderViewInfos.size() + 1 + mFooterViewInfos.size();
                if (mCacheFirstHeaderView) {
                    offset += 1;
                }
                count += offset;
            }
            if (DEBUG) {
                Log.d(LOG_TAG, String.format("getViewTypeCount: %s", count));
            }
            return count;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.registerObserver(observer);
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(observer);
            }
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.unregisterObserver(observer);
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(observer);
            }
        }

        @Override
        public Filter getFilter() {
            if (mIsFilterable) {
                return ((Filterable) mAdapter).getFilter();
            }
            return null;
        }

        @Override
        public ListAdapter getWrappedAdapter() {
            return mAdapter;
        }

        public void notifyDataSetChanged() {
            mDataSetObservable.notifyChanged();
        }
    }
}
