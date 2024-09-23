package com.nativemodulesworkshops.chart

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event
import com.facebook.react.uimanager.events.RCTEventEmitter

class ValueSelectedEvent(viewTag: Int, private val datasetIndex: Int, private val x: Double, private val y: Double) : Event<ValueSelectedEvent>(viewTag) {

    override fun getEventName(): String {
        return EVENT_NAME
    }

    override fun dispatch(rctEventEmitter: RCTEventEmitter) {
        rctEventEmitter.receiveEvent(viewTag, eventName, serializeEventData())
    }

    private fun serializeEventData(): WritableMap {
        val eventData = Arguments.createMap()
        eventData.putInt("datasetIndex", datasetIndex)
        eventData.putDouble("x", x)
        eventData.putDouble("y", y)
        return eventData
    }

    companion object {
        const val EVENT_NAME = "topSelectedValue"
    }

    init {
        // folly::toJson default options don't support serialize NaN or Infinite value
    }
}
