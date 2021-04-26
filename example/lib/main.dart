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
                    (e) => Image.asset(
                      e.filePath ?? '',
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
