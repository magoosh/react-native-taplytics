//
//  RNTaplytics.m
//  RNTaplytics
//
//  Created by Aria on 7/21/16.
//  Copyright © 2016 Magoosh. All rights reserved.
//

#import "RNTaplytics.h"
#import <Taplytics.h>

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

@end
