import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

enum PhotoPickerType {
  all,
  image,
  video,
}

class PhotoPickerOptions {
  /// 选择照片类型，0 = 图片，1 = 视频，2 = 混合
  PhotoPickerType type = PhotoPickerType.all;

  /// 可选资源最大数
  int maxAssetsCount = 9;

  /// 是否可以编辑资源
  bool allowEdit = true;

  /// 编辑 - 单选模式下选择图片时是否直接跳转到编辑界面
  bool singleJumpEdit = true;

  /// 编辑 - 是否使用圆形剪裁
  bool isRoundCliping = false;

  /// 编辑 - 自定义剪裁比例，宽度
  int photoEditCustomRatioW = 0;

  /// 编辑 - 自定义剪裁比例，高度
  int photoEditCustomRatioH = 0;

  /// 列表每行显示个数
  int imageSpanCount = 4;

  /// 是否允许在相册打开相机
  bool allowOpenCamera = true;

  /// 是否允许选择Gif
  bool allowGif = false;

  int? videoMaximumDuration;
  int? videoMinimumDuration;

  PhotoPickerOptions({
    this.type = PhotoPickerType.all,
    this.maxAssetsCount = 9,
    this.allowEdit = true,
    this.singleJumpEdit = true,
    this.isRoundCliping = false,
    this.photoEditCustomRatioW = 0,
    this.photoEditCustomRatioH = 0,
    this.imageSpanCount = 4,
    this.allowOpenCamera = true,
    this.allowGif = false,
    this.videoMaximumDuration,
    this.videoMinimumDuration,
  });

  Map<String, dynamic> toMap() {
    int type;
    switch (this.type) {
      case PhotoPickerType.image:
        type = 0;
        break;
      case PhotoPickerType.video:
        type = 1;
        break;
      default:
        type = 2;
        break;
    }
    return {
      'type': type,
      'maxAssetsCount': maxAssetsCount,
      'allowEdit': allowEdit,
      'singleJumpEdit': singleJumpEdit,
      'isRoundCliping': isRoundCliping,
      'photoEditCustomRatioW': photoEditCustomRatioW,
      'photoEditCustomRatioH': photoEditCustomRatioH,
      'imageSpanCount': imageSpanCount,
      'allowOpenCamera': allowOpenCamera,
      'allowGif': allowGif,
      'videoMaximumDuration': videoMaximumDuration,
      'videoMinimumDuration': videoMinimumDuration,
    };
  }

  factory PhotoPickerOptions.fromMap(Map<String, dynamic> map) {
    int type = map['type'] as int;
    PhotoPickerType _type;
    if (type == 0) {
      _type = PhotoPickerType.image;
    } else if (type == 1) {
      _type = PhotoPickerType.video;
    } else {
      _type = PhotoPickerType.all;
    }
    return PhotoPickerOptions(
      type: _type,
      maxAssetsCount: map['maxAssetsCount'],
      allowEdit: map['allowEdit'],
      singleJumpEdit: map['singleJumpEdit'],
      isRoundCliping: map['isRoundCliping'],
      photoEditCustomRatioW: map['photoEditCustomRatioW'],
      photoEditCustomRatioH: map['photoEditCustomRatioH'],
      imageSpanCount: map['imageSpanCount'],
      allowOpenCamera: map['allowOpenCamera'],
      allowGif: map['allowGif'],
      videoMaximumDuration: map['videoMaximumDuration'],
      videoMinimumDuration: map['videoMinimumDuration'],
    );
  }

  String toJson() => json.encode(toMap());

  factory PhotoPickerOptions.fromJson(String source) =>
      PhotoPickerOptions.fromMap(json.decode(source));
}

class PhotoPickerResult {
  PhotoPickerResult({
    required this.assets,
  });

  final List<PhotoAsset> assets;

  Map<String, dynamic> toMap() {
    return {
      'assets': assets.map((x) => x.toMap()).toList(),
    };
  }

  factory PhotoPickerResult.fromMap(Map<String, dynamic> map) {
    return PhotoPickerResult(
      assets: List<PhotoAsset>.from(map['assets']
          ?.map((x) => PhotoAsset.fromMap(Map<String, dynamic>.from(x)))),
    );
  }

  String toJson() => json.encode(toMap());

  factory PhotoPickerResult.fromJson(String source) =>
      PhotoPickerResult.fromMap(json.decode(source));
}

class PhotoAsset {
  PhotoAsset({
    required this.filePath,
    required this.width,
    required this.height,
  });

  final String filePath;
  final double width;
  final double height;

  @override
  String toString() =>
      'PhotoAsset(filePath: $filePath, width: $width, height: $height)';

  Map<String, dynamic> toMap() {
    return {
      'filePath': filePath,
      'width': width,
      'height': height,
    };
  }

  factory PhotoAsset.fromMap(Map<String, dynamic> map) {
    return PhotoAsset(
      filePath: map['filePath'],
      width: map['width'],
      height: map['height'],
    );
  }

  String toJson() => json.encode(toMap());

  factory PhotoAsset.fromJson(String source) =>
      PhotoAsset.fromMap(json.decode(source));
}

class PhotoPicker {
  /// Constructor for [PhotoPicker].  The [binaryMessenger] named argument is
  /// available for dependency injection.  If it is left null, the default
  /// BinaryMessenger will be used which routes to the host platform.
  PhotoPicker({BinaryMessenger? binaryMessenger})
      : _binaryMessenger = binaryMessenger;

  final BinaryMessenger? _binaryMessenger;

  Future<PhotoPickerResult> pickPhoto(PhotoPickerOptions arg) async {
    final Object encoded = arg.toMap();
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.PhotoPicker.pickPhoto',
        const StandardMessageCodec(),
        binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(encoded) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
        details: null,
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error =
          (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return PhotoPickerResult.fromMap(
          Map<String, dynamic>.from(replyMap['result'] as Map));
    }
  }

  Future<PhotoPickerResult> pickPhotoFromCamera(PhotoPickerOptions arg) async {
    final Object encoded = arg.toMap();
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.PhotoPicker.pickPhotoFromCamera',
        const StandardMessageCodec(),
        binaryMessenger: _binaryMessenger);
    final Map<Object?, Object?>? replyMap =
        await channel.send(encoded) as Map<Object?, Object?>?;
    if (replyMap == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
        details: null,
      );
    } else if (replyMap['error'] != null) {
      final Map<Object?, Object?> error =
          (replyMap['error'] as Map<Object?, Object?>?)!;
      throw PlatformException(
        code: (error['code'] as String?)!,
        message: error['message'] as String?,
        details: error['details'],
      );
    } else {
      return PhotoPickerResult.fromMap(
          Map<String, dynamic>.from(replyMap['result'] as Map));
    }
  }

  /// 还原状态栏颜色
  void revertSystemUIOverlayStyle(Brightness style) {
    SystemChannels.platform.invokeMethod<void>(
      'SystemChrome.setSystemUIOverlayStyle',
      {'statusBarBrightness': style.toString()},
    );
  }
}
