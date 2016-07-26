'use strict';

import { NativeModules } from 'react-native';

const RNTaplytics = NativeModules.RNTaplytics;

const Taplytics = {
  init: RNTaplytics.init,
  identify: RNTaplytics.setUserAttributes,
  track: (name, optionalValue, optionalAttributes) => {
    // TODO(aria): Better edge case handling here
    const value = (typeof optionalValue === 'number') ? optionalValue : 0;
    const attributes = (optionalValue == null) ? optionalValue : optionalAttributes;

    RNTaplytics.track(name, value, attributes);
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
  variable: RNTaplytics.variable,
  codeBlock: RNTaplytics.codeBlock,
};

export default Taplytics;
