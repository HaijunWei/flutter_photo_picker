import 'dart:io';

import 'package:flutter/material.dart';

import 'package:photo_picker/photo_picker.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<PhotoAsset> _assets = [];

  //选择照片类型
  PhotoPickerType type = PhotoPickerType.all;

  //最大图片数
  int maxAssetsCount = 9;

  //是否可以编辑资源
  bool allowEdit = true;

  //编辑 - 单选模式下选择图片时是否直接跳转到编辑界面
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

  void _pickPhoto(PhotoPickerOptions options) async {
    final picker = PhotoPicker();
    final result = await picker.pickPhoto(options);
    final assets = result.assets;

    _assets.map((e) => print("============================${e.filePath}"));
    picker.revertSystemUIOverlayStyle(Brightness.light);

    if (assets.isEmpty) return;
    _assets = assets;
    setState(() {});
  }

  void _pickPhotoFromCamera(PhotoPickerOptions options) async {
    final picker = PhotoPicker();
    final result = await picker.pickPhotoFromCamera(options);
    final assets = result.assets;

    _assets.map((e) => print("============================${e.filePath}"));

    if (assets.isEmpty) return;
    _assets = assets;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          drawer: Drawer(
            child: ListView(
              children: [
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "Type",
                    style: TextStyle(color: Colors.black38, fontSize: 16),
                  ),
                ),
                Row(
                  children: <Widget>[
                    _Radio(
                      text: "All",
                      groupValue: type,
                      value: PhotoPickerType.all,
                      onChanged: (value) {
                        setState(() {
                          this.type = value;
                        });
                      },
                    ),
                    _Radio(
                      text: "Image",
                      groupValue: type,
                      value: PhotoPickerType.image,
                      onChanged: (value) {
                        setState(() {
                          this.type = value;
                        });
                      },
                    ),
                    _Radio(
                      text: "Video",
                      groupValue: type,
                      value: PhotoPickerType.video,
                      onChanged: (value) {
                        setState(() {
                          this.type = value;
                        });
                      },
                    ),
                  ],
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "MaxAssetsCount(min:1)",
                    style: TextStyle(color: Colors.black38, fontSize: 16),
                  ),
                ),
                _NumberTextField(
                  value: maxAssetsCount.toString(),
                  onChanged: (value) {
                    setState(() {
                      this.maxAssetsCount = int.parse(value);
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "Edit",
                    style: TextStyle(color: Colors.black38, fontSize: 16),
                  ),
                ),
                _CheckBox(
                  text: "AllowEdit",
                  value: this.allowEdit,
                  onChanged: (value) {
                    setState(() {
                      this.allowEdit = value;
                    });
                  },
                ),
                _CheckBox(
                  text: "SingleJumpEdit",
                  value: this.singleJumpEdit,
                  onChanged: (value) {
                    setState(() {
                      this.singleJumpEdit = value;
                    });
                  },
                ),
                _CheckBox(
                  text: "IsRoundCliping",
                  value: this.isRoundCliping,
                  onChanged: (value) {
                    setState(() {
                      this.isRoundCliping = value;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "Crop Proportion(Width&Height)",
                  ),
                ),
                Row(
                  children: <Widget>[
                    _NumberTextField(
                      value: photoEditCustomRatioW.toString(),
                      onChanged: (value) {
                        setState(() {
                          this.photoEditCustomRatioW = int.parse(value);
                        });
                      },
                    ),
                    _NumberTextField(
                      value: photoEditCustomRatioW.toString(),
                      onChanged: (value) {
                        setState(() {
                          this.photoEditCustomRatioH = int.parse(value);
                        });
                      },
                    ),
                  ],
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "Other",
                    style: TextStyle(color: Colors.black38, fontSize: 16),
                  ),
                ),
                _CheckBox(
                  text: "AllowOpenCamera",
                  value: this.allowOpenCamera,
                  onChanged: (value) {
                    setState(() {
                      this.allowOpenCamera = value;
                    });
                  },
                ),
                _CheckBox(
                  text: "AllowGif",
                  value: this.allowGif,
                  onChanged: (value) {
                    setState(() {
                      this.allowGif = value;
                    });
                  },
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, top: 16),
                  child: Text(
                    "ImageSpanCount",
                  ),
                ),
                _NumberTextField(
                  value: imageSpanCount.toString(),
                  onChanged: (value) {
                    setState(() {
                      this.imageSpanCount = int.parse(value);
                    });
                  },
                ),
              ],
            ),
          ),
          body: Builder(
            builder: (context) => ListView(
              children: [
                Padding(
                  padding: EdgeInsets.all(16),
                  child: Text(
                    "Photos",
                    style: TextStyle(color: Colors.black38, fontSize: 16),
                  ),
                ),
                Padding(
                  padding: EdgeInsets.only(left: 16, right: 16),
                  child: Wrap(
                    children: _assets
                        .map(
                          (e) => Image.file(
                            File(e.filePath),
                            fit: BoxFit.cover,
                            width: 100,
                            height: 100,
                          ),
                        )
                        .toList(),
                  ),
                ),
                SizedBox(
                  height: 20,
                ),
                _Button(
                  title: "CONFIG",
                  color: Colors.blue,
                  onTap: () {
                    Scaffold.of(context).openDrawer();
                  },
                ),
                SizedBox(
                  height: 20,
                ),
                _Button(
                  title: "OPEN PHOTO PICKER",
                  color: Colors.blueAccent,
                  onTap: () {
                    PhotoPickerOptions options = PhotoPickerOptions();
                    options.imageSpanCount = this.imageSpanCount;
                    options.maxAssetsCount = this.maxAssetsCount;
                    options.allowGif = this.allowGif;
                    options.allowOpenCamera = this.allowOpenCamera;
                    options.isRoundCliping = this.isRoundCliping;
                    options.singleJumpEdit = this.singleJumpEdit;
                    options.allowEdit = this.allowEdit;
                    options.type = this.type;
                    options.photoEditCustomRatioH = this.photoEditCustomRatioH;
                    options.photoEditCustomRatioW = this.photoEditCustomRatioW;
                    _pickPhoto(options);
                  },
                ),
                _Button(
                  title: "OPEN PHOTO CAMERA",
                  color: Colors.blueAccent,
                  onTap: () {
                    PhotoPickerOptions options = PhotoPickerOptions();
                    options.imageSpanCount = this.imageSpanCount;
                    options.maxAssetsCount = this.maxAssetsCount;
                    options.allowGif = this.allowGif;
                    options.allowOpenCamera = this.allowOpenCamera;
                    options.isRoundCliping = this.isRoundCliping;
                    options.singleJumpEdit = this.singleJumpEdit;
                    options.allowEdit = this.allowEdit;
                    options.type = this.type;
                    options.photoEditCustomRatioH = this.photoEditCustomRatioH;
                    options.photoEditCustomRatioW = this.photoEditCustomRatioW;
                    //to camera
                    _pickPhotoFromCamera(options);
                  },
                ),
              ],
            ),
          )),
    );
  }
}

class _NumberTextField extends StatelessWidget {
  const _NumberTextField({
    Key? key,
    required this.onChanged,
    required this.value,
  }) : super(key: key);

  final ValueChanged onChanged;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Row(children: <Widget>[
      Padding(
        padding: EdgeInsets.only(left: 16, top: 10, bottom: 10),
        child: Container(
          width: 100,
          constraints: BoxConstraints(
            maxHeight: 100.0,
            minHeight: 40.0,
          ),
          decoration: BoxDecoration(
              color: Colors.grey[200],
              borderRadius: BorderRadius.all(Radius.circular(5))),
          padding: EdgeInsets.only(left: 10, right: 10, top: 10, bottom: 0),
          child: TextField(
            maxLines: null,
            keyboardType: TextInputType.number,
            controller: new TextEditingController(text: value),
            autofocus: false,
            decoration: InputDecoration.collapsed(
              hintText: "",
            ),
            onChanged: onChanged,
          ),
        ),
      ),
    ]);
  }
}

class _CheckBox extends StatelessWidget {
  const _CheckBox({
    Key? key,
    required this.text,
    required this.value,
    required this.onChanged,
  }) : super(key: key);

  final String text;
  final bool value;
  final ValueChanged onChanged;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Padding(
          padding: EdgeInsets.only(left: 16),
          child: Text(text),
        ),
        Checkbox(
          value: this.value,
          onChanged: onChanged,
        ),
      ],
    );
  }
}

class _Radio extends StatelessWidget {
  const _Radio({
    Key? key,
    required this.text,
    required this.groupValue,
    required this.value,
    required this.onChanged,
  }) : super(key: key);

  final String text;
  final Object groupValue;
  final Object value;
  final ValueChanged onChanged;

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Radio(
          value: this.value,
          groupValue: this.groupValue,
          onChanged: onChanged,
        ),
        Padding(
          padding: EdgeInsets.only(right: 10),
          child: Text(text),
        ),
      ],
    );
  }
}

class _Button extends StatelessWidget {
  const _Button({
    Key? key,
    required this.title,
    required this.color,
    this.onTap,
  }) : super(key: key);

  final String title;
  final Color color;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onTap,
      child: Container(
        height: 40,
        width: 200,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.all(Radius.circular(5)),
        ),
        padding: EdgeInsets.symmetric(horizontal: 15),
        child: Text(title, style: TextStyle(color: Colors.white)),
      ),
    );
  }
}
