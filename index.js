'use strict';

import { NativeModules, NativeAppEventEmitter } from 'react-native';

const RNTaplytics = NativeModules.RNTaplytics;

let loadStatus = 'loading';

let variableNames = [];
let variableValues = [];
let variableCallbacks = [];

const resolveVariables = (success) => {
  loadStatus = success ? 'succeeded' : 'failed';
  RNTaplytics.variables(variableNames, variableValues, (results) => {
    for (let i = 0; i < variableCallbacks.length; i++) {
      variableCallbacks[i](results[i]);
    }
  });
};

const createVariable = (name, defaultValue, callback) => {
  variableNames.push(name);
  variableValues.push(defaultValue);
  variableCallbacks.push(callback);
  if (loadStatus !== 'loading') {
    return RNTaplytics.variables([name], [defaultValue], (results) => callback(results[0]));
  } else {
    return defaultValue;
  }
};

var propertiesLoadedSubscription = NativeAppEventEmitter.addListener(
  'RNTaplyticsPropertiesLoaded',
  resolveVariables,
);

let alreadyInit = false;

const Taplytics = {
  init: (apiToken, options) => {
    if (alreadyInit) { return; }
    alreadyInit = true;
    loadStatus = 'loading';
    RNTaplytics.init(apiToken, options);
  },
  identify: RNTaplytics.setUserAttributes,
  track: (name, optionalValue, optionalAttributes) => {
    // TODO(aria): Better edge case handling here
    const value = (typeof optionalValue === 'number') ? optionalValue : null;
    const attributes = (optionalValue == null) ? optionalValue : optionalAttributes;

    if (value == null) {
      RNTaplytics.track(name, attributes);
    } else {
      RNTaplytics.trackWithValue(name, value, attributes);
    }
  },
  page: (optionalCategory, optionalName, optionalAttributes) => {
    console.warn(
      "Taplytics.page is unsupported. Sending a track event instead. Called with:",
      optionalCategory,
      optionalName,
      optionalAttributes
    );
    if (optionalAttributes != null) {
      RNTaplytics.track('Page', 0, Object.assign({
        category: optionalCategory,
        name: optionalName,
      }, optionalAttributes));

    } else if (optionalName != null) {

      if (typeof optionalName === 'string') {
        RNTaplytics.track('Page', 0, {
          category: optionalCategory,
          name: optionalName,
        });

      } else {
        RNTaplytics.track('Page', 0, Object.assign({
          name: optionalCategory,
        }, optionalAttributes));
      }

    } else {
      RNTaplytics.track('Page', 0, {
        name: optionalName,
      });
    }
  },
  reset: RNTaplytics.reset, // takes an optional callback
  runningExperiments: RNTaplytics.runningExperiments,
  variable: (name, defaultValue, callback) => {
    return createVariable(name, defaultValue, callback);
  },
  codeBlock: RNTaplytics.codeBlock,
  propertiesLoadedCallback: (callback) => {
    var propertiesLoadedSubscription = NativeAppEventEmitter.addListener(
      'RNTaplyticsPropertiesLoaded',
      callback,
    );
    return () => propertiesLoadedSubscription.remove();
  },
};


export default Taplytics;
