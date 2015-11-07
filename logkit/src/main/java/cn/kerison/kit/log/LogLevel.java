package cn.kerison.kit.log;

/**
 * Log level  Android系统 android.util.Log
 * 增加 ALL、NONE
 */
public class LogLevel {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;
    /**
     * 输出所有日志
     */
    public static final int ALL = Integer.MIN_VALUE;
    /**
     * 屏蔽所有日志
     */
    public static final int NONE = Integer.MAX_VALUE;
}
