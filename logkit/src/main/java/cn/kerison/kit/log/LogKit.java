package cn.kerison.kit.log;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by kerison on 2015/5/19.
 */
public class LogKit {

	/**
	 * Log level value {@link Log}
	 */
	public static final int LOG_VERBOSE = 2;
	public static final int LOG_DEBUG = 3;
	public static final int LOG_INFO = 4;
	public static final int LOG_WARN = 5;
	public static final int LOG_ERROR = 6;
	public static final int LOG_ASSERT = 7;
	public static final int NO_LOG = Integer.MAX_VALUE;

	private static int DEFAULT_LEVEL = LOG_VERBOSE;
	private static String DEFAULT_TAG = null;

	private LogKit() {
	}

	public static void setLevel(int level) {
		DEFAULT_LEVEL = level;
	}

	public static void setTag(String defaultTag) {
		DEFAULT_TAG = defaultTag;
	}

	public static void v(String content) {

		if (DEFAULT_LEVEL <= LOG_VERBOSE) {
			LogPrint(LOG_VERBOSE, obtainTag(), content);
		}
	}

	public static void v(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_VERBOSE) {
			LogPrint(LOG_VERBOSE, obtainTag(), String.format(format, args));
		}
	}

	public static void v(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_VERBOSE) {
			LogPrint(LOG_DEBUG, obtainTag(), content, tr);
		}
	}


	public static void d(String content) {

		if (DEFAULT_LEVEL <= LOG_DEBUG) {
			LogPrint(LOG_DEBUG, obtainTag(), content);
		}
	}

	public static void d(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_DEBUG) {
			LogPrint(LOG_DEBUG, obtainTag(), String.format(format, args));
		}
	}

	public static void d(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_DEBUG) {
			LogPrint(LOG_DEBUG, obtainTag(), content, tr);
		}
	}

	public static void i(String content) {

		if (DEFAULT_LEVEL <= LOG_INFO) {
			LogPrint(LOG_INFO, obtainTag(), content);
		}
	}

	public static void i(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_INFO) {
			LogPrint(LOG_INFO, obtainTag(), String.format(format, args));
		}
	}

	public static void i(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_INFO) {
			LogPrint(LOG_INFO, obtainTag(), content, tr);
		}
	}

	public static void w(String content) {

		if (DEFAULT_LEVEL <= LOG_WARN) {
			LogPrint(LOG_WARN, obtainTag(), content);
		}
	}

	public static void w(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_WARN) {
			LogPrint(LOG_WARN, obtainTag(), String.format(format, args));
		}
	}

	public static void w(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_WARN) {
			LogPrint(LOG_WARN, obtainTag(), content, tr);
		}
	}

	public static void e(String content) {

		if (DEFAULT_LEVEL <= LOG_ERROR) {
			LogPrint(LOG_ERROR, obtainTag(), content);
		}
	}

	public static void e(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_ERROR) {
			LogPrint(LOG_ERROR, obtainTag(), String.format(format, args));
		}
	}

	public static void e(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_ERROR) {
			LogPrint(LOG_ERROR, obtainTag(), content, tr);
		}
	}

	public static void wft(String content) {

		if (DEFAULT_LEVEL <= LOG_ASSERT) {

			LogPrint(LOG_ASSERT, obtainTag(), content);
		}
	}

	public static void wft(String format, Object... args) {

		if (DEFAULT_LEVEL <= LOG_ASSERT) {
			LogPrint(LOG_ASSERT, obtainTag(), String.format(format, args));
		}
	}

	public static void wft(String content, Throwable tr) {

		if (DEFAULT_LEVEL <= LOG_ASSERT) {

			LogPrint(LOG_ASSERT, obtainTag(), content, tr);
		}
	}

	private static String obtainTag() {

		if (!TextUtils.isEmpty(DEFAULT_TAG)) {
			return DEFAULT_TAG;
		}

		StackTraceElement caller = Thread.currentThread().getStackTrace()[4];

		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName
				.lastIndexOf(".") + 1);

		return String.format(
				Locale.ENGLISH,
				"%s.%s(L:%d)",
				new Object[]{callerClazzName, caller.getMethodName(),
						Integer.valueOf(caller.getLineNumber())});
	}

	private static void LogPrint(int logLevel, String tag, String msg) {

		Log.println(logLevel, tag, msg);
	}

	private static void LogPrint(int logLevel, String tag, String msg,
								 Throwable tr) {
		Log.println(logLevel, tag, msg + Log.getStackTraceString(tr));
	}
}
