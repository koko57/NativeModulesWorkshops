package com.nativemodulesworkshops.chart

import android.graphics.Color
import com.facebook.infer.annotation.Assertions
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.*
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ChartViewManager : SimpleViewManager<ChartView>() {

    override fun getName() = "ChartsView"

    override fun createViewInstance(context: ThemedReactContext): ChartView {
        val view = ChartView(context)
        return ChartView(context)
    }

    @ReactProp(name = "datasets")
    fun setDatasets(view: ChartView, value: ReadableArray) {
        val data: ArrayList<HashMap<String, Any>> =
            value.toArrayList() as ArrayList<HashMap<String, Any>>
        setData(view, data)
    }

    private fun setData(view: ChartView, data: ArrayList<HashMap<String, Any>>) {
        val sets = data.reversed().map { item ->
            val entries = item["data"] as ArrayList<Double>
            val label = item["name"] as String
            val lineColor = item["lineColor"] as String
            val fillColor = item["fillColor"] as String
            val lineWidth = item["lineWidth"] as Double
            val set = RadarDataSet(entries.map { item -> RadarEntry(item.toFloat()) }, label)
            set.color = Color.parseColor(lineColor)
            set.fillColor = Color.parseColor(fillColor)
            set.setDrawFilled(true)
            set.lineWidth = lineWidth.toFloat()
            return@map set
        }
        val radarData = RadarData(sets)
        radarData.setValueTextSize(8f)
        radarData.setDrawValues(false)
        radarData.setValueTextColor(Color.WHITE)
        view.data = radarData
        view.invalidate()
    }

    @ReactProp(name = "RNLegendEnabled")
    fun setRNLegendEnabled(view: ChartView, value: Boolean) {
        view.legend.isEnabled = value

    }

    @ReactProp(name = "RNWebLineWidth")
    fun setRNWebLineWidth(view: ChartView, value: Double) {
        view.webLineWidth = value.toFloat()

    }

    @ReactProp(name = "RNInnerWebLineWidth")
    fun setRNInnerWebLineWidth(view: ChartView, value: Double) {
        view.webLineWidthInner = value.toFloat()
    }

    @ReactProp(name = "RNWebColor")
    fun setRNWebColor(view: ChartView, value: String) {
        view.webColor = Color.parseColor(value)

    }

    @ReactProp(name = "RNInnerWebColor")
    fun setRNInnerWebColor(view: ChartView, value: String) {
        view.webColorInner = Color.parseColor(value)
    }

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Map<String, String>> {
        return MapBuilder.of(
            ValueSelectedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onValueSelected")
        )
    }

    override fun getCommandsMap(): Map<String, Int>? {
        return MapBuilder.of(
            "selectValue",
            COMMAND_SELECT_VALUE
        )
    }

    override fun receiveCommand(view: ChartView, commandId: Int, args: ReadableArray?) {
        super.receiveCommand(view, commandId, args)
        Assertions.assertNotNull(args)

        when (commandId) {
            COMMAND_SELECT_VALUE -> {
                val dataSet = args!!.getInt(0)
                val x = args!!.getInt(1).toFloat()
                view.highlightValue(x,dataSet);
            }
            else -> throw IllegalArgumentException(String.format(
                "Unsupported command %d received by %s.",
                commandId,
                javaClass.simpleName))
        }
    }

    companion object {
        private const val COMMAND_SELECT_VALUE = 1
    }
}
