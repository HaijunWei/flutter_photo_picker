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

- (void)pickPhotoFromCamera:(nullable HJPhotoPickerOptions *)input completion:(nonnull void (^)(HJPhotoPickerResult * _Nullable, FlutterError * _Nullable))completion {
    UIViewController *viewController = [self viewControllerWithWindow:nil];
    HXPhotoManager *manager = [self createManagerFromOptions:input];
    [viewController hx_presentCustomCameraViewControllerWithManager:manager done:^(HXPhotoModel *model, HXCustomCameraViewController *viewController) {
        [self handleSelectedModels:@[model] completion:completion];
    } cancel:^(HXCustomCameraViewController *viewController) {
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
    if (options.singleJumpEdit != nil) { manager.configuration.singleJumpEdit =  options.allowEdit.boolValue && options.singleJumpEdit.boolValue; }
    if (options.isRoundCliping != nil) {
        if (options.isRoundCliping.boolValue) {
            manager.configuration.photoEditConfigur.isRoundCliping = YES;
            manager.configuration.photoEditConfigur.aspectRatio = HXPhotoEditAspectRatioType_1x1;
            manager.configuration.photoEditConfigur.onlyCliping = YES;
        }
    }
    if (options.photoEditCustomRatioW != nil && options.photoEditCustomRatioW.intValue > 0 && options.photoEditCustomRatioH != nil && options.photoEditCustomRatioW.intValue > 0) {
        manager.configuration.photoEditConfigur.aspectRatio = HXPhotoEditAspectRatioType_Custom;
        manager.configuration.photoEditConfigur.customAspectRatio = CGSizeMake(options.photoEditCustomRatioW.intValue, options.photoEditCustomRatioH.intValue);
    }
    if (options.imageSpanCount != nil) { manager.configuration.rowCount = options.imageSpanCount.intValue; }
    if (options.allowOpenCamera != nil) { manager.configuration.openCamera = options.allowOpenCamera.boolValue; }
    if (options.allowGif != nil) { manager.configuration.lookGifPhoto = options.allowGif.boolValue; }
    if (options.videoMaximumDuration != nil) { manager.configuration.videoMaximumDuration = options.videoMaximumDuration.intValue; }
    if (options.videoMinimumDuration != nil) { manager.configuration.videoMinimumDuration = options.videoMinimumDuration.intValue; }
    manager.configuration.reverseDate = YES;
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
            if (model.subType == HXPhotoModelMediaSubTypePhoto) {
                NSString *tempDir = NSTemporaryDirectory();
                NSString *fileType = @"png";
                NSString *type = info[@"PHImageFileUTIKey"] ?: (__bridge NSString *)kUTTypePNG;
                if ([type isEqualToString:(__bridge NSString *)kUTTypeGIF]) {
                    fileType = @"gif";
                } else if ([type isEqualToString:(__bridge NSString *)kUTTypeJPEG]) {
                    fileType = @"jpg";
                } else if (![type isEqualToString:(__bridge NSString *)kUTTypePNG]) {
                    imageData = UIImagePNGRepresentation([[UIImage alloc] initWithData:imageData]);
                }
                NSString *filename = [NSString stringWithFormat:@"photo_picker_%@.%@", [[NSUUID UUID] UUIDString], fileType];
                NSString *filePath = [tempDir stringByAppendingPathComponent:filename];
                [[NSFileManager defaultManager] createFileAtPath:filePath contents:imageData attributes:nil];
                HJPhotoAsset *resultAsset = [HJPhotoAsset new];
                resultAsset.filePath = filePath;
                resultAsset.width = @(model.imageSize.width);
                resultAsset.height = @(model.imageSize.height);
                resultAssers[i] = resultAsset;
                dispatch_group_leave(group);
            } else if (model.subType == HXPhotoModelMediaSubTypeVideo) {
                [model exportVideoWithPresetName:AVAssetExportPresetMediumQuality startRequestICloud:nil iCloudProgressHandler:nil exportProgressHandler:nil success:^(NSURL * _Nullable videoURL, HXPhotoModel * _Nullable model) {
                    HJPhotoAsset *resultAsset = [HJPhotoAsset new];
                    resultAsset.filePath = videoURL.path;
                    resultAsset.width = @(model.imageSize.width);
                    resultAsset.height = @(model.imageSize.height);
                    resultAssers[i] = resultAsset;
                    dispatch_group_leave(group);
                } failed:^(NSDictionary * _Nullable info, HXPhotoModel * _Nullable model) {
                    isError = YES;
                    dispatch_group_leave(group);
                }];
            }
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
