
package com.haijunwei.photo_picker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.haijunwei.photo_picker.bean.PhotoAsset;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/** Generated class from Pigeon. */
@SuppressWarnings({"unused", "unchecked", "CodeBlock2Expr", "RedundantSuppression"})
public class Messages {

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class PhotoPickerResult {
    private List<PhotoAsset> assets;
    public List<PhotoAsset> getAssets() { return assets; }
    public void setAssets(List<PhotoAsset> setterArg) { this.assets = setterArg; }

    Map<String, List<Object>> toMap() {
      Map<String, List<Object>> toMapResult = new HashMap<>();
      List<Object> objects = new ArrayList();
      if(assets==null){
          toMapResult.put("assets", null);
          return toMapResult;
      }

      for (int i =0;i<assets.size();i++){
        objects.add(assets.get(i).toMap());
      }
      toMapResult.put("assets", objects);
      return toMapResult;
    }
//    static PhotoPickerResult fromMap(Map<String, Object> map) {
//      PhotoPickerResult fromMapResult = new PhotoPickerResult();
//      Object assets = map.get("assets");
//
//      fromMapResult.assets = (List<Object>)assets;
//      return fromMapResult;
//    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class PhotoPickerOptions {
    private Long type;
    public Long getType() { return type; }
    public void setType(Long setterArg) { this.type = setterArg; }

    private Long maxAssetsCount;
    public Long getMaxAssetsCount() { return maxAssetsCount; }
    public void setMaxAssetsCount(Long setterArg) { this.maxAssetsCount = setterArg; }

    private Boolean allowEdit;
    public Boolean getAllowEdit() { return allowEdit; }
    public void setAllowEdit(Boolean setterArg) { this.allowEdit = setterArg; }

    private Long videoMaximumDuration;
    public Long getVideoMaximumDuration() { return videoMaximumDuration; }
    public void setVideoMaximumDuration(Long setterArg) { this.videoMaximumDuration = setterArg; }

    private Long videoMinimumDuration;
    public Long getVideoMinimumDuration() { return videoMinimumDuration; }
    public void setVideoMinimumDuration(Long setterArg) { this.videoMinimumDuration = setterArg; }

    private Boolean singleJumpEdit;
    public Boolean getSingleJumpEdit() { return singleJumpEdit; }
    public void setSingleJumpEdit(Boolean setterArg) { this.singleJumpEdit = setterArg; }

    private Boolean isRoundCliping;
    public Boolean getIsRoundCliping() { return isRoundCliping; }
    public void setIsRoundCliping(Boolean setterArg) { this.isRoundCliping = setterArg; }

    private Boolean reverseDate;
    public Boolean getReverseDate() { return reverseDate; }
    public void setReverseDate(Boolean setterArg) { this.reverseDate = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("type", type);
      toMapResult.put("maxAssetsCount", maxAssetsCount);
      toMapResult.put("allowEdit", allowEdit);
      toMapResult.put("videoMaximumDuration", videoMaximumDuration);
      toMapResult.put("videoMinimumDuration", videoMinimumDuration);
      toMapResult.put("singleJumpEdit", singleJumpEdit);
      toMapResult.put("isRoundCliping", isRoundCliping);
      toMapResult.put("reverseDate", reverseDate);
      return toMapResult;
    }
    static PhotoPickerOptions fromMap(Map<String, Object> map) {
      PhotoPickerOptions fromMapResult = new PhotoPickerOptions();
      Object type = map.get("type");
      fromMapResult.type = (type == null) ? null : ((type instanceof Integer) ? (Integer)type : (Long)type);
      Object maxAssetsCount = map.get("maxAssetsCount");
      fromMapResult.maxAssetsCount = (maxAssetsCount == null) ? null : ((maxAssetsCount instanceof Integer) ? (Integer)maxAssetsCount : (Long)maxAssetsCount);
      Object allowEdit = map.get("allowEdit");
      fromMapResult.allowEdit = (Boolean)allowEdit;
      Object videoMaximumDuration = map.get("videoMaximumDuration");
      fromMapResult.videoMaximumDuration = (videoMaximumDuration == null) ? null : ((videoMaximumDuration instanceof Integer) ? (Integer)videoMaximumDuration : (Long)videoMaximumDuration);
      Object videoMinimumDuration = map.get("videoMinimumDuration");
      fromMapResult.videoMinimumDuration = (videoMinimumDuration == null) ? null : ((videoMinimumDuration instanceof Integer) ? (Integer)videoMinimumDuration : (Long)videoMinimumDuration);
      Object singleJumpEdit = map.get("singleJumpEdit");
      fromMapResult.singleJumpEdit = (Boolean)singleJumpEdit;
      Object isRoundCliping = map.get("isRoundCliping");
      fromMapResult.isRoundCliping = (Boolean)isRoundCliping;
      Object reverseDate = map.get("reverseDate");
      fromMapResult.reverseDate = (Boolean)reverseDate;
      return fromMapResult;
    }
  }

  public interface Result<T> {
    void success(T result);
  }

  /** Generated interface from Pigeon that represents a handler of messages from Flutter.*/
  public interface PhotoPicker {
    void pickPhoto(PhotoPickerOptions arg, Result<PhotoPickerResult> result);
  }

  /** Sets up an instance of `PhotoPicker` to handle messages through the `binaryMessenger`. */
  public static void setup(BinaryMessenger binaryMessenger, PhotoPicker api) {
    {
      BasicMessageChannel<Object> channel =
              new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.PhotoPicker.pickPhoto", new StandardMessageCodec());
      if (api != null) {
        channel.setMessageHandler((message, reply) -> {
          Map<String, Object> wrapped = new HashMap<>();
          try {
            @SuppressWarnings("ConstantConditions")
            PhotoPickerOptions input = PhotoPickerOptions.fromMap((Map<String, Object>)message);
            api.pickPhoto(input, result -> { wrapped.put("result", result.toMap()); reply.reply(wrapped); });
          }
          catch (Error | RuntimeException exception) {
            wrapped.put("error", wrapError(exception));
            reply.reply(wrapped);
          }
        });
      } else {
        channel.setMessageHandler(null);
      }
    }
  }

  private static Map<String, Object> wrapError(Throwable exception) {
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("message", exception.toString());
    errorMap.put("code", exception.getClass().getSimpleName());
    errorMap.put("details", null);
    return errorMap;
  }
}
