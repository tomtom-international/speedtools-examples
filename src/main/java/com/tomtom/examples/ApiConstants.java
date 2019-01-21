/*
 * Copyright (C) 2012-2019. TomTom NV (http://tomtom.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomtom.examples;

import com.tomtom.speedtools.geometry.Geo;
import org.joda.time.DateTime;

/**
 * This utility class contains constants used in the Web services API.
 */
public final class ApiConstants {

    // Prevent instantiation.
    private ApiConstants() {
        super();
        assert false;
    }

    /**
     * General HTTP timeout for @Suspend() annotations.
     */
    public static final int SUSPEND_TIMEOUT = 30000;

    public static final int API_VERSION_MAX_LENGTH = 25;
    public static final int API_VERSION_MIN_LENGTH = 0;
    public static final int API_AGE_MIN = 1;
    public static final int API_AGE_MAX = 120;

    /**
     * Date limits.
     */
    public static final DateTime API_DATE_MAX = new DateTime(2200, 1, 1, 0, 0);
    public static final DateTime API_DATE_MIN = new DateTime(1900, 1, 1, 0, 0);

    /**
     * General limits for count and offset.
     */
    public static final int API_COUNT_MAX = Integer.MAX_VALUE;
    public static final int API_OFFSET_MAX = Integer.MAX_VALUE;

    public static final int API_VERSION_LEN_MIN = 1;
    public static final int API_VERSION_LEN_MAX = 250;

    public static final int API_NAME_LEN_MIN = 1;
    public static final int API_NAME_LEN_MAX = 250;

    public static final int API_HEART_RATE_MIN = 0;
    public static final int API_HEART_RATE_MAX = 250;

    public static final int API_OFFSET_SECS_MAX = 10 * 365 * 86400;
    public static final int API_OFFSET_SECS_MIN = 0;

    public static final double API_LAT_MAX = 90.0;
    public static final double API_LAT_MIN = -90.0;
    public static final double API_LON_MAX = Geo.LON180;
    public static final double API_LON_MIN = -180.0;

    public static final double API_ELEVATION_METERS_MAX = 100000.0;
    public static final double API_ELEVATION_METERS_MIN = -30000.0;
}
