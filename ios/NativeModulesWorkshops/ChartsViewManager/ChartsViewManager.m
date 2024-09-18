#import <React/RCTViewManager.h>
#import <React/RCTBridge.h>

@interface RCT_EXTERN_MODULE(ChartsViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(datasets, NSArray)
RCT_EXPORT_VIEW_PROPERTY(RNRotationEnabled, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(RNLegendEnabled, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(RNWebLineWidth, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(RNInnerWebLineWidth, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(RNWebColor, NSString)
RCT_EXPORT_VIEW_PROPERTY(RNInnerWebColor, NSString)

RCT_EXPORT_VIEW_PROPERTY(onValueSelected, RCTBubblingEventBlock)

RCT_EXTERN_METHOD(selectValue:(nonnull NSNumber *)reactTag
         datasetIndex:(nonnull NSNumber *)datasetIndex x:(nonnull NSNumber *)x)


@end
