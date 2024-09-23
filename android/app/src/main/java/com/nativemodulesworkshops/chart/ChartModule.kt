package com.nativemodulesworkshops.chart

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule


@ReactModule(name = ChartModule.REACT_CLASS)
class ChartModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName() = REACT_CLASS
    var enabled = true
    val eventEmitter: EventEmitter = EventEmitter(reactContext)

    @ReactMethod
    fun isRotationEnabled(promise: Promise) {
        promise.resolve(enabled)
    }

    @ReactMethod
    fun enableRotation(enable: Boolean,promise: Promise) {
        enabled = enable
        val writableMap = Arguments.createMap()
        writableMap.putBoolean("enabled", enabled)
        eventEmitter.sendEvent(EnableRotationEvent.EVENT_NAME,writableMap)
    }

    companion object {
        const val REACT_CLASS = "ChartsModule"
    }
}
