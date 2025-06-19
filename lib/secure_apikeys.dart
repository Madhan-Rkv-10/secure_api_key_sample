import 'package:flutter/services.dart';

class SecureApi {
  static const _chan = MethodChannel('secure_channel');
  static Future getFacebookAppId() async {
    final id = await _chan.invokeMethod('getFacebookAppId');
    return id!;
  }
}
