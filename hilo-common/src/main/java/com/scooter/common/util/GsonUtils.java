package com.scooter.common.util;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import lombok.NonNull;

public class GsonUtils {

  private static Gson GSON = new Gson();

  /**
   * From json t.
   *
   * @param <T> the type parameter
   * @param json the json
   * @param clz the clz
   * @return the t
   */
  public static <T> T fromJson(@NonNull String json, @NonNull Class<T> clz) {
    return GSON.fromJson(json, clz);
  }

  /**
   * From json t.
   *
   * @param <T> the type parameter
   * @param json the json
   * @param type the type
   * @return the t
   */
  public static <T> T fromJson(@NonNull String json, @NonNull Type type) {
    return GSON.fromJson(json, type);
  }

  /**
   * To json string.
   *
   * @param obj the obj
   * @return the string
   */
  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }

}
