package io.flutter.plugins.firebase.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.EventChannel;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public abstract class EventsProxy {
  protected final EventChannel.EventSink eventSink;
  private final String eventType;

  protected EventsProxy(@NonNull EventChannel.EventSink eventSink, @NonNull String eventType) {
    this.eventSink = eventSink;
    this.eventType = eventType;
  }

  Map<String, Object> buildAdditionalParams(@NonNull String eventType, @Nullable String previousChildName) {
    final Map<String, Object> params = new HashMap<>();
    params.put(Constants.EVENT_TYPE, eventType);

    if (previousChildName != null) {
      params.put(Constants.PREVIOUS_CHILD_NAME, previousChildName);
    }

    return params;
  }

  protected void sendEvent(@NonNull String eventType, DataSnapshot snapshot, @Nullable String previousChildName) {
    if (!this.eventType.equals(eventType)) return;

    FlutterDataSnapshotPayload payload = new FlutterDataSnapshotPayload(snapshot);
    final Map<String, Object> additionalParams = buildAdditionalParams(eventType, previousChildName);

    eventSink.success(payload.withChildKeys().withAdditionalParams(additionalParams).toMap());
  }
}