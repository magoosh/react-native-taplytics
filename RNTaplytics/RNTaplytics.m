//
//  RNTaplytics.m
//  RNTaplytics
//
//  Created by Aria on 7/21/16.
//  Copyright © 2016 Magoosh. All rights reserved.
//

#import "RNTaplytics.h"

#import <RCTBridge.h>
#import <RCTEventDispatcher.h>

#import <Taplytics.h>
#import <TaplyticsVar.h>

// Portions based on RNMixpanel
// https://github.com/davodesign84/react-native-mixpanel

@implementation RNTaplytics

BOOL areEventsInitialized = NO;

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE(RNTaplytics)

RCT_EXPORT_METHOD(init:(NSString *)apiToken options:(NSDictionary *)options) {
    [Taplytics startTaplyticsAPIKey:apiToken options:options];
    if (!areEventsInitialized) {
        areEventsInitialized = YES;
        [Taplytics propertiesLoadedCallback:^(BOOL loaded) {
            [self.bridge.eventDispatcher sendAppEventWithName:@"RNTaplyticsPropertiesLoaded" body:[NSNumber numberWithBool:loaded]];
        }];
    }
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

RCT_EXPORT_METHOD(variable:(NSString*)name defaultValue:(NSDictionary*)defaultValue callback:(RCTResponseSenderBlock)callback) {
    // TODO(aria): Implement a real RCTConverter instead of making a wrapper dict.
    TaplyticsVar * var = [TaplyticsVar taplyticsSyncVarWithName:name defaultValue:defaultValue[@"value"]];
    callback(@[var.value]);
}

RCT_EXPORT_METHOD(variables:(NSArray*)names defaultValues:(NSArray*)defaultValues callback:(RCTResponseSenderBlock)callback) {
    // TODO(aria): Implement a real RCTConverter instead of making a wrapper dict.
    const int count = MIN((int)[names count], (int)[defaultValues count]);
    NSMutableArray * results = [NSMutableArray arrayWithCapacity:count];
    for (int i = 0; i < count; i++) {
        TaplyticsVar * var = [TaplyticsVar taplyticsSyncVarWithName:names[i] defaultValue:defaultValues[i]];
        [results addObject:var.value];
    }
    callback(@[results]);
}


RCT_EXPORT_METHOD(codeBlock:(NSString*)name forBlock:(nonnull void(^)(void))callback) {
    [Taplytics runCodeBlock:name forBlock:callback];
}

RCT_EXPORT_METHOD(propertiesLoadedCallback:(BOOL)triggerFirstTime callback:(RCTResponseSenderBlock)callback) {
    __block int triggerCount = 0;
    [Taplytics propertiesLoadedCallback:^(BOOL loaded) {
        triggerCount++;
        if ((triggerFirstTime && triggerCount == 1) || (!triggerFirstTime && triggerCount == 2)) {
            callback(@[[NSNumber numberWithBool:loaded]]);
        }
    }];
}


@end
