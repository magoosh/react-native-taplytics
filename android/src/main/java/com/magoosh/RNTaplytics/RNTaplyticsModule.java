package com.magoosh.RNTaplytics;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taplytics.sdk.Taplytics;

/**
 * Adapted from KevinEJohn's RNMixpanel.
 */
public class RNTaplyticsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    ReactApplicationContext reactContext;

    public RNTaplyticsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        // Get lifecycle notifications to flush mixpanel on pause or destroy
        reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "RNTaplytics";
    }

    @Override
    public void onHostResume() {
        // Actvity `onResume`
    }

    @Override
    public void onHostPause() {
        // Actvity `onPause`

        //if (mixpanel != null) {
        //    mixpanel.flush();
        //}
    }

    @Override
    public void onHostDestroy() {
        // Actvity `onDestroy`

        //if (mixpanel != null) {
        //    mixpanel.flush();
        //}
    }

    @ReactMethod
    void init(String apiKey, ReadableMap options) {
        Map<String, Object> nativeOptions = new HashMap<String, Object>();
        Taplytics.startTaplytics(this, apiKey, nativeOptions);

    }

    @ReactMethod
    void setUserAttributes(ReadableMap attributes) {

    }
}
