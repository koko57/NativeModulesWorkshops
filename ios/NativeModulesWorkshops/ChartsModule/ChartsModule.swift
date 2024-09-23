//
//  ChartsModule.swift
//  NativeModulesWorkshops
//
//  Created by Agata Kosior on 23/09/2024.
//

import Foundation

@objc(ChartsModule)
class ChartsModule: NSObject {
  var enabled = true;

  @objc(isRotationEnabled:withRejecter:)
  func isRotationEnabled(resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
    resolve(enabled)
  }

  @objc(enableRotation:)
  func enableRotation(enable: NSNumber) -> Void {
    enabled = enable.boolValue
    NotificationCenter.default.post(name: Notification.Name(enableRotationNotification), object: enabled)
  }
}
