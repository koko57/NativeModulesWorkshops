package com.nativemodulesworkshops.chart

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class EventEmitter(private val reactApplicationContext: ReactApplicationContext) {
    private val listeners = mutableMapOf<String, MutableList<(WritableMap?) -> Unit>>()

    fun sendEvent(eventName: String, params: WritableMap?) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
        sendNativeEvent(eventName, params);
    }

    fun sendNativeEvent(eventName: String, params: WritableMap?) {
        listeners[eventName]?.forEach { it(params) }
    }

    fun on(eventName: String, listener: (WritableMap?) -> Unit): () -> Unit {
        val eventListeners = listeners.getOrPut(eventName) {
            mutableListOf()
        }
        val index = eventListeners.size
        eventListeners.add(index, listener)

        return {
            eventListeners.removeAt(index);
        }
    }
}
