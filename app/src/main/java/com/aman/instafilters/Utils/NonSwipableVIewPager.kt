package com.aman.instafilters.Utils

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import java.util.jar.Attributes

class NonSwipableVIewPager: ViewPager{

    constructor(context: Context): super(context){
        setMyScroller()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context,attributeSet){
        setMyScroller()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Never allow swiping to switch between pages
        return false
    }



    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // Never allow swiping to switch between pages
        return false
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        var heightMeasureSpec = heightMeasureSpec
//
//        var height = 0
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
//            val h = child.measuredHeight
//            if (h > height) height = h
//        }
//
//        if (height != 0) {
//            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
//        }
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }

//down one is added for smooth scrolling

    private fun setMyScroller() {
        try{
            val viewPager= ViewPager::class.java
            val scroller= viewPager.getDeclaredField("mScroller")
            scroller.isAccessible=true
            scroller.set(this, MyScroller(context))
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


}


 class MyScroller(context: Context?): Scroller(context,DecelerateInterpolator()) {

     override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
         super.startScroll(startX, startY, dx, dy, 40)
     }
 }