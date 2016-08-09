package com.magoosh.RNTaplytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.taplytics.sdk.Taplytics;
import com.taplytics.sdk.TaplyticsVar;
import com.taplytics.sdk.TaplyticsExperimentsLoadedListener;
import com.taplytics.sdk.TaplyticsResetUserListener;

/**
 * Adapted heavily from KevinEJohn's RNMixpanel.
 */
public class RNTaplyticsModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    ReactApplicationContext reactContext;
    boolean areEventsInitialized = false;

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

    private void sendEvent(
            String eventName,
            Object data) {
        reactContext
            .getJSModule(RCTNativeAppEventEmitter.class)
            .emit(eventName, data);
    }

    @ReactMethod
    public void init(String apiKey, ReadableMap options) {
        Taplytics.startTaplytics(
            reactContext,
            apiKey,
            hashMapFromReactMap(options),
            new TaplyticsExperimentsLoadedListener() {
                @Override
                public void loaded() {
                    sendEvent("RNTaplyticsPropertiesLoaded", Boolean.TRUE);
                }
            }
        );
        areEventsInitialized = true;
    }

    @ReactMethod
    public void setUserAttributes(ReadableMap attributes) {
        try {
            Taplytics.setUserAttributes(jsonFromReact(attributes));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void track(String name, Double value, ReadableMap metaData) {
        try {
            Taplytics.logEvent(name, value, jsonFromReact(metaData));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset(final Callback callback) {
        Taplytics.resetAppUser(new TaplyticsResetUserListener() {
            public void finishedResettingUser() {
                if (callback != null) {
                    callback.invoke();
                }
            }
        });
    }

    public void propertiesLoaded(Callback callback) {
        // TODO(aria): Implement me
    }

    public void runningExperiments(Callback callback) {
        // TODO(aria): Implement me
    }

    public void variables(ReadableArray names, ReadableArray defaultValues, Callback callback) {
        final ArrayList<Object> names_ = arrayFromReactArray(names);
        final ArrayList<Object> defaultValues_ = arrayFromReactArray(defaultValues);
        final int count = Math.min(names_.size(), defaultValues_.size());
        final WritableArray results = new WritableNativeArray();
        for (int i = 0; i < count; i++) {
            TaplyticsVar<String> v = new TaplyticsVar<String>((String)names_.get(i), (String)defaultValues_.get(i));
            results.pushString(v.get());
        }
        callback.invoke(results);
    }

    // Private JSON converting
    static Map<String, Object> hashMapFromReactMap(ReadableMap reactMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        ReadableMapKeySetIterator iterator = reactMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType valueType = reactMap.getType(key);
            switch (valueType) {
                case Null:
                    result.put(key, null);
                    break;
                case Boolean:
                    result.put(key, reactMap.getBoolean(key));
                    break;
                case Number:
                    result.put(key, reactMap.getDouble(key));
                    break;
                case String:
                    result.put(key, reactMap.getString(key));
                    break;
                case Map:
                    result.put(key, hashMapFromReactMap(reactMap.getMap(key)));
                    break;
                case Array:
                    result.put(key, arrayFromReactArray(reactMap.getArray(key)));
                    break;
            }
        }
        return result;
    }

    static ArrayList<Object> arrayFromReactArray(ReadableArray reactArray) {
        ArrayList<Object> result = new ArrayList<Object>(reactArray.size());
        for (int i=0; i < reactArray.size(); i++) {
            ReadableType valueType = reactArray.getType(i);
            switch (valueType){
                case Null:
                    result.add(null);
                    break;
                case Boolean:
                    result.add(reactArray.getBoolean(i));
                    break;
                case Number:
                    result.add(reactArray.getDouble(i));
                    break;
                case String:
                    result.add(reactArray.getString(i));
                    break;
                case Map:
                    result.add(hashMapFromReactMap(reactArray.getMap(i)));
                    break;
                case Array:
                    result.add(arrayFromReactArray(reactArray.getArray(i)));
                    break;
            }
        }
        return result;
    }

    static JSONObject jsonFromReact(ReadableMap readableMap) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while(iterator.hasNextKey()){
            String key = iterator.nextKey();
            ReadableType valueType = readableMap.getType(key);
            switch (valueType){
                case Null:
                    jsonObject.put(key,JSONObject.NULL);
                    break;
                case Boolean:
                    jsonObject.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    jsonObject.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    jsonObject.put(key, readableMap.getString(key));
                    break;
                case Map:
                    jsonObject.put(key, jsonFromReact(readableMap.getMap(key)));
                    break;
                case Array:
                    jsonObject.put(key, jsonFromReact(readableMap.getArray(key)));
                    break;
            }
        }

        return jsonObject;
    }
    static JSONArray jsonFromReact(ReadableArray readableArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i < readableArray.size(); i++) {
            ReadableType valueType = readableArray.getType(i);
            switch (valueType){
                case Null:
                    jsonArray.put(JSONObject.NULL);
                    break;
                case Boolean:
                    jsonArray.put(readableArray.getBoolean(i));
                    break;
                case Number:
                    jsonArray.put(readableArray.getDouble(i));
                    break;
                case String:
                    jsonArray.put(readableArray.getString(i));
                    break;
                case Map:
                    jsonArray.put(jsonFromReact(readableArray.getMap(i)));
                    break;
                case Array:
                    jsonArray.put(jsonFromReact(readableArray.getArray(i)));
                    break;
            }
        }
        return jsonArray;
    }
}

