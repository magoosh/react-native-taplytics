//
//  RNTaplytics.m
//  RNTaplytics
//
//  Created by Aria on 7/21/16.
//  Copyright © 2016 Magoosh. All rights reserved.
//

#import "RNTaplytics.h"
#import <Taplytics.h>
#import <TaplyticsVar.h>

// Portions based on RNMixpanel
// https://github.com/davodesign84/react-native-mixpanel

@implementation RNTaplytics

RCT_EXPORT_MODULE(RNTaplytics)

RCT_EXPORT_METHOD(init:(NSString *)apiToken options:(NSDictionary *)options) {
    [Taplytics startTaplyticsAPIKey:apiToken options:options];
}

RCT_EXPORT_METHOD(setUserAttributes:(NSDictionary *)userAttributes) {
    [Taplytics setUserAttributes:userAttributes];
}

RCT_EXPORT_METHOD(track:(NSString*)name value:(nullable NSNumber*)value metaData:(nullable NSDictionary*)metaData) {
    [Taplytics logEvent:name value:value metaData:metaData];
}

RCT_EXPORT_METHOD(reset:(nullable void(^)(void))callback) {
    [Taplytics resetUser:callback];
}

RCT_EXPORT_METHOD(propertiesLoaded:(nonnull void(^)(BOOL))callback) {
    [Taplytics propertiesLoadedCallback:callback];
}

RCT_EXPORT_METHOD(runningExperiments:(nonnull void(^)(NSDictionary * _Nullable experimentsAndVariations))callback) {
    [Taplytics getRunningExperimentsAndVariations:callback];
}

RCT_EXPORT_METHOD(variable:(NSString*)name defaultValue:(NSObject*)defaultValue callback:(nullable void(^)(NSObject * value))callback) {
    if (callback) {
        // TODO(aria): Implement a real RCTConverter instead of making a wrapper dict.
        [TaplyticsVar taplyticsVarWithName:name defaultValue:defaultValue updatedBlock:^(NSObject * _Nullable value) {
            if ([value isKindOfClass:[NSNumber class]]) {
                NSNumber * n = (NSNumber*)value;
                callback(n);
            } else if ([value isKindOfClass:[NSString class]]) {
                NSString * s = (NSString*)value;
                callback(s);
            } else {
                callback(nil);
            }
        }];
    } else {
        [TaplyticsVar taplyticsVarWithName:name defaultValue:defaultValue updatedBlock:nil];
    }
}

RCT_EXPORT_METHOD(codeBlock:(NSString*)name forBlock:(nonnull void(^)(void))callback) {
    [Taplytics runCodeBlock:name forBlock:callback];
}


@end
