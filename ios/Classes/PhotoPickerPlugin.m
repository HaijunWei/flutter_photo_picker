#import "PhotoPickerPlugin.h"
#import "messages.h"
#import <HXPhotoPicker/HXPhotoPicker.h>

@interface PhotoPickerPlugin () <HJPhotoPicker>

@end

@implementation PhotoPickerPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    PhotoPickerPlugin *plugin = [PhotoPickerPlugin new];
    HJPhotoPickerSetup([registrar messenger], plugin);
    [registrar publish:plugin];
}

- (void)pickPhoto:(nullable HJPhotoPickerOptions *)input completion:(nonnull void (^)(HJPhotoPickerResult * _Nullable, FlutterError * _Nullable))completion {
    UIViewController *viewController = [self viewControllerWithWindow:nil];
    HXPhotoManager *manager = [self createManagerFromOptions:input];
    [viewController hx_presentSelectPhotoControllerWithManager:manager didDone:^(NSArray<HXPhotoModel *> * _Nullable allList, NSArray<HXPhotoModel *> * _Nullable photoList, NSArray<HXPhotoModel *> * _Nullable videoList, BOOL isOriginal, UIViewController * _Nullable viewController, HXPhotoManager * _Nullable manager) {
        [self handleSelectedModels:allList completion:completion];
    } cancel:^(UIViewController * _Nullable viewController, HXPhotoManager * _Nullable manager) {
        completion([HJPhotoPickerResult new], nil);
    }];
}

#pragma mark -

- (HXPhotoManager *)createManagerFromOptions:(HJPhotoPickerOptions *)options {
    HXPhotoManager *manager = [HXPhotoManager new];
    manager.configuration.type = HXConfigurationTypeWXChat;
    if (options.type != nil) { manager.type = options.type.intValue; }
    if (options.maxAssetsCount != nil) {
        manager.configuration.maxNum = options.maxAssetsCount.intValue;
        if (options.maxAssetsCount.intValue == 1) {
            manager.configuration.singleSelected = YES;
        }
    }
    if (options.allowEdit != nil) { manager.configuration.photoCanEdit = options.allowEdit.boolValue; }
    if (options.videoMaximumDuration != nil) { manager.configuration.videoMaximumDuration = options.videoMaximumDuration.intValue; }
    if (options.videoMinimumDuration != nil) { manager.configuration.videoMinimumDuration = options.videoMinimumDuration.intValue; }
    if (options.singleJumpEdit != nil) { manager.configuration.singleJumpEdit = options.singleJumpEdit.boolValue; }
    if (options.isRoundCliping != nil) {
        if (options.isRoundCliping.boolValue) {
            manager.configuration.photoEditConfigur.isRoundCliping = YES;
            manager.configuration.photoEditConfigur.aspectRatio = HXPhotoEditAspectRatioType_1x1;
            manager.configuration.photoEditConfigur.onlyCliping = YES;
        }
    }
    if (options.reverseDate != nil) { manager.configuration.reverseDate = options.reverseDate.boolValue; }
    return manager;
}

- (void)handleSelectedModels:(NSArray<HXPhotoModel *> *)models completion:(nonnull void (^)(HJPhotoPickerResult * _Nullable, FlutterError * _Nullable))completion {
    dispatch_group_t group = dispatch_group_create();
    NSMutableArray *resultAssers = [NSMutableArray new];
    __block BOOL isError = NO;
    for (int i = 0; i < models.count; i++) {
        [resultAssers addObject:[NSNull null]];
    }
    for (int i = 0; i < models.count; i++) {
        dispatch_group_enter(group);
        HXPhotoModel *model = models[i];
        if (isError) {
            dispatch_group_leave(group);
            break;
        }
        [model requestImageDataStartRequestICloud:nil progressHandler:nil success:^(NSData * _Nullable imageData, UIImageOrientation orientation, HXPhotoModel * _Nullable model, NSDictionary * _Nullable info) {
            NSString *tempDir = NSTemporaryDirectory();
            NSString *filename = [NSString stringWithFormat:@"photo_picker_%@", [[NSUUID UUID] UUIDString]];
            NSString *filePath = [tempDir stringByAppendingPathComponent:filename];
            [[NSFileManager defaultManager] createFileAtPath:filePath contents:imageData attributes:nil];
            HJPhotoAsset *resultAsset = [HJPhotoAsset new];
            resultAsset.filePath = filePath;
            resultAssers[i] = resultAsset;
            dispatch_group_leave(group);
        } failed:^(NSDictionary * _Nullable info, HXPhotoModel * _Nullable model) {
            isError = YES;
            dispatch_group_leave(group);
        }];
    }
    dispatch_group_notify(group, dispatch_get_global_queue(0, 0), ^{
        HJPhotoPickerResult *result = [HJPhotoPickerResult new];
        result.assets = resultAssers;
        if (isError) {
            completion(nil, [FlutterError errorWithCode:@"0" message:@"获取相册资源失败" details:nil]);
        } else {
            completion(result, nil);
        }
    });
}

- (UIViewController *)viewControllerWithWindow:(UIWindow *)window {
    UIWindow *windowToUse = window;
    if (windowToUse == nil) {
        for (UIWindow *window in [UIApplication sharedApplication].windows) {
            if (window.isKeyWindow) {
                windowToUse = window;
                break;
            }
        }
    }
    UIViewController *topController = windowToUse.rootViewController;
    while (topController.presentedViewController) {
        topController = topController.presentedViewController;
    }
    return topController;
}

@end
