//
//  ChartsModule.m
//  NativeModulesWorkshops
//
//  Created by Agata Kosior on 23/09/2024.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTBridge.h>

@interface RCT_EXTERN_MODULE(ChartsModule, NSObject)

RCT_EXTERN_METHOD(isRotationEnabled:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(enableRotation:(nonnull NSNumber *)enable)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
