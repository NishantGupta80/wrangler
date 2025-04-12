package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Represents a token for byte size values (e.g., "10KB", "1.5MB").
 */
public class ByteSize implements Token {
  private final long bytes;

  public ByteSize(String value) {
    this.bytes = parseBytes(value);
  }

  private long parseBytes(String value) {
    if (value.endsWith("KB")) {
      return (long) (Double.parseDouble(value.replace("KB", "").trim()) * 1024);
    } else if (value.endsWith("MB")) {
      return (long) (Double.parseDouble(value.replace("MB", "").trim()) * 1024 * 1024);
    } else if (value.endsWith("GB")) {
      return (long) (Double.parseDouble(value.replace("GB", "").trim()) * 1024 * 1024 * 1024);
    } else if (value.endsWith("TB")) {
      return (long) (Double.parseDouble(value.replace("TB", "").trim()) * 1024L * 1024 * 1024 * 1024);
    } else if (value.endsWith("B")) {
      return Long.parseLong(value.replace("B", "").trim());
    } else {
      throw new IllegalArgumentException("Invalid byte size format: " + value);
    }
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    JsonObject object = new JsonObject();
    object.addProperty("type", TokenType.BYTE_SIZE.name());
    object.addProperty("value", bytes);
    return object;
  }
}