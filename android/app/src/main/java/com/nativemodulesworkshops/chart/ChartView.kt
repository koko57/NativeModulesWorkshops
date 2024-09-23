package com.nativemodulesworkshops.chart

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.events.EventDispatcher
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

class ChartView(context: Context): RadarChart(context), OnChartGestureListener {
    private var eventDispatcher: EventDispatcher?
    private var unsubscribe: (() -> Unit)? = null;

    init {
        onChartGestureListener = this
        eventDispatcher = (context as ReactContext).getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val module = (context as ReactContext).getNativeModule(ChartModule::class.java)
        val unsubscribeSession = module?.eventEmitter?.on(EnableRotationEvent.EVENT_NAME) {
            isRotationEnabled = module?.enabled ?: false
        }
        unsubscribe = {
            unsubscribeSession?.invoke()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unsubscribe?.invoke()
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        val highlight = getHighlightByTouchPoint(me!!.x,me!!.y)
        val dataSetIndex = highlight.dataSetIndex
        val x = highlight.x
        val y = highlight.y
        highlightValue(x,y,dataSetIndex)
        eventDispatcher?.dispatchEvent(ValueSelectedEvent(id,dataSetIndex,x.toDouble(),y.toDouble()))
    }

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {

    }

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {

    }

    override fun onChartLongPressed(me: MotionEvent?) {

    }

    override fun onChartDoubleTapped(me: MotionEvent?) {

    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {

    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {

    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {

    }
}
