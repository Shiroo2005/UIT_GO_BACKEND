package com.se360.UIT_Go.driver_service.constants;

import lombok.Getter;

public interface RedisKey {
    public static final String KEY_GEO_LOCATIONS = "drivers:available:locations";
    public static final String KEY_DRIVER_STATUS_PREFIX = "driver:status:";
}
