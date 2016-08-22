package com.magoosh.RNTaplytics;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import android.app.Application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Adapted from RNMixpanel by KevinEJohn
 */
public class RNTaplytics implements ReactPackage {

    public static void init(Application app, String apiKey, Map<String, Object> options) {
        RNTaplyticsModule.onCreateInit(app, apiKey, options);
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        RNTaplyticsModule module = new RNTaplyticsModule(reactContext);
        return Arrays.<NativeModule>asList(module);
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(
            ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
