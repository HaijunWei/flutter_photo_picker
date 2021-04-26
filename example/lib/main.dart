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

  void _pickPhoto(PhotoPickerOptions options) async {
    final picker = PhotoPicker();
    final result = await picker.pickPhoto(options);
    final assets = result.assets;
    if (assets == null || assets.isEmpty) return;
    _assets = assets as List<PhotoAsset>;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('PhotoPicker'),
        ),
        body: ListView(
          children: [
            Wrap(
              children: _assets
                  .map(
                    (e) => Image.file(
                      File(e.filePath ?? ''),
                      fit: BoxFit.cover,
                      width: 100,
                      height: 100,
                    ),
                  )
                  .toList(),
            ),
            _Tile(
              title: '打开相册',
              onTap: () {
                _pickPhoto(PhotoPickerOptions());
              },
            ),
            _Tile(
              title: '选择单图',
              onTap: () {
                _pickPhoto(PhotoPickerOptions()..maxAssetsCount = 1);
              },
            ),
            _Tile(
              title: '选择单图并编辑',
              onTap: () {
                _pickPhoto(PhotoPickerOptions()
                  ..maxAssetsCount = 1
                  ..singleJumpEdit = true);
              },
            ),
            _Tile(
              title: '选择圆形头像',
              onTap: () {
                _pickPhoto(
                  PhotoPickerOptions()
                    ..maxAssetsCount = 1
                    ..singleJumpEdit = true
                    ..isRoundCliping = true,
                );
              },
            ),
            _Tile(
              title: '选择视频',
              onTap: () {
                _pickPhoto(PhotoPickerOptions()..type = 1);
              },
            ),
          ],
        ),
      ),
    );
  }
}

class _Tile extends StatelessWidget {
  const _Tile({
    Key? key,
    required this.title,
    this.onTap,
  }) : super(key: key);

  final String title;
  final VoidCallback? onTap;

  @override
  Widget build(BuildContext context) {
    return TextButton(
      onPressed: onTap,
      child: Container(
        height: 40,
        padding: EdgeInsets.symmetric(horizontal: 15),
        child: Row(
          children: [
            Expanded(child: Text(title)),
            Icon(
              Icons.arrow_forward_ios_rounded,
              size: 15,
            ),
          ],
        ),
      ),
    );
  }
}
