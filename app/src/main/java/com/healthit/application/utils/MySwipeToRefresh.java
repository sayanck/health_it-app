//package com.healthit.application.utils;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.ViewConfiguration;
//
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//public class MySwipeToRefresh  extends SwipeRefreshLayout {
//
//    private int mTouchSlop;
//    private float mPrevx;
//    private float mPrevy;
//
//    public MySwipeToRefresh(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPrevx = MotionEvent.obtain(event).getX();
//                mPrevy = MotionEvent.obtain(event).getY();
//                System.out.println("mprevx ix "+mPrevx);
//                System.out.println("mprevy ix "+mPrevy);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("move  ix ");
//                final float evX = event.getX();
//                final float evy = event.getY();
//                float xDiff = Math.abs(evX - mPrevx);
//                float yDiff = Math.abs(evy - mPrevy);
//                if (xDiff > mTouchSlop && xDiff > yDiff) {
//                    return false;
//                }
//        }
//
//        return super.onInterceptTouchEvent(event);
//    }
//}