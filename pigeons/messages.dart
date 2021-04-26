import 'package:pigeon/pigeon_lib.dart';

class PhotoPickerOptions {
  /// 选择照片类型，0 = 图片，1 = 视频，2 = 混合
  int? type;

  /// 可选资源最大数
  int? maxAssetsCount;

  /// 是否可以编辑资源
  bool? allowEdit;

  /// 相机视频录制最大秒数
  int? videoMaximumDuration;

  /// 相机视频录制最小秒数
  int? videoMinimumDuration;

  /// 单选模式下选择图片时是否直接跳转到编辑界面
  bool? singleJumpEdit;

  /// 是否使用圆形剪裁
  bool? isRoundCliping;

  /// 照片列表倒序
  bool? reverseDate;
}

class PhotoPickerResult {
  List<String> assets = [];
}

@HostApi()
abstract class PhotoPicker {
  @async
  PhotoPickerResult pickPhoto(PhotoPickerOptions options);
}

void configurePigeon(PigeonOptions opts) {
  opts.dartOut = 'lib/messages.dart';
  opts.objcHeaderOut = 'ios/Classes/messages.h';
  opts.objcSourceOut = 'ios/Classes/messages.m';
  opts.objcOptions?.prefix = 'HJ';
  opts.javaOut =
      'android/src/main/kotlin/com/haijunwei/photo_picker/Messages.java';
  opts.javaOptions?.package = 'io.flutter.plugins.photopicker';
}
