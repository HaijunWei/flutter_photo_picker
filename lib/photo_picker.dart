
import 'dart:async';

import 'package:flutter/services.dart';

class PhotoPicker {
  static const MethodChannel _channel =
      const MethodChannel('photo_picker');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
