package cn.kerison.kit.log;

import android.util.Log;

/**
 * Created by kerison on 2015/11/6.
 */
public class LogLevel {
    /**
     * Log level value {@link Log}
     */
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    public static final int ALL = Integer.MIN_VALUE;
    public static final int NONE = Integer.MAX_VALUE;
}
