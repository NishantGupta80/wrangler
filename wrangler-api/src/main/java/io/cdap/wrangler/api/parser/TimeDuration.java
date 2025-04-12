package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Represents a token for time duration values (e.g., "150ms", "2.5s").
 */
public class TimeDuration implements Token {
  private final long milliseconds;

  public TimeDuration(String value) {
    this.milliseconds = parseMilliseconds(value);
  }

  private long parseMilliseconds(String value) {
    if (value.endsWith("ms")) {
      return Long.parseLong(value.replace("ms", "").trim());
    } else if (value.endsWith("s")) {
      return (long) (Double.parseDouble(value.replace("s", "").trim()) * 1000);
    } else if (value.endsWith("m")) {
      return (long) (Double.parseDouble(value.replace("m", "").trim()) * 60 * 1000);
    } else if (value.endsWith("h")) {
      return (long) (Double.parseDouble(value.replace("h", "").trim()) * 60 * 60 * 1000);
    } else if (value.endsWith("d")) {
      return (long) (Double.parseDouble(value.replace("d", "").trim()) * 24 * 60 * 60 * 1000);
    } else {
      throw new IllegalArgumentException("Invalid time duration format: " + value);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("type", TokenType.TIME_DURATION.name());
    object.addProperty("value", milliseconds);
    return object;
  }
}