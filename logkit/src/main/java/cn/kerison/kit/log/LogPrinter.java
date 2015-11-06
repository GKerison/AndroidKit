package cn.kerison.kit.log;

import android.util.Log;

/**
 * Created by kerison on 2015/11/6.
 */
public class LogPrinter {
    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;
    /**
     * 文本格式化偏移4个空格
     */
    private static final String TEXT_OFFSET = "    ";
    /**
     * 栈内有效函数偏移
     */
    private static final int STACK_OFFSET = 6;
    /**
     * 系统的默认换行符
     */
    private static final String LR = System.getProperty("line.separator");

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_BAR = '╔';
    private static final char BOTTOM_LEFT_BAR = '╚';
    private static final char MIDDLE_LEFT_BAR = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_BAR + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_BAR + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_LEFT_BAR + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private String mTag;
    private int mLevel;
    private int mTextLevel;
    private boolean isShowThread;
    private boolean isShowPackage;
    private int mStackCount;

    public void updateConfig(LogConfig config) {
        mTag = config.getTag();
        mLevel = config.getLevel();
        mTextLevel = config.getTextLevel();
        isShowThread = config.isShowThread();
        isShowPackage = config.isShowPackage();
        mStackCount = config.getStackCount();
    }

    public void v(String msg) {
        if (mLevel <= LogLevel.VERBOSE) {
            log(LogLevel.VERBOSE, msg);
        }
    }

    public void d(String msg) {
        if (mLevel <= LogLevel.DEBUG) {
            log(LogLevel.DEBUG, msg);
        }
    }

    public void i(String msg) {
        if (mLevel <= LogLevel.INFO) {
            log(LogLevel.INFO, msg);
        }
    }

    public void w(String msg) {
        if (mLevel <= LogLevel.WARN) {
            log(LogLevel.WARN, msg);
        }
    }

    public void e(String msg) {
        if (mLevel <= LogLevel.ERROR) {
            log(LogLevel.ERROR, msg);
        }
    }

    public void wtf(String msg) {
        if (mLevel <= LogLevel.ASSERT) {
            log(LogLevel.ASSERT, msg);
        }
    }

    public void text(String msg) {
        if (mLevel <= mTextLevel) {
            log(mTextLevel, msg);
        }
    }

    /**
     * 打印log信息
     * @param level
     * @param msg
     */
    private synchronized void log(int level, String msg) {

        showTopBar(level);

        if (this.isShowThread) {
            showTheadInfo(level);
            showDivider(level);
        }

        if (mStackCount > 0) {
            showStackTrace(level);
        }

        showContent(level, msg);

        showBottomBar(level);
    }

    /**
     * 打印顶部线
     *
     * @param level
     */
    private void showTopBar(int level) {
        print(level, mTag, TOP_BORDER);
    }

    /**
     * 打印底部线
     *
     * @param level
     */
    private void showBottomBar(int level) {
        print(level, mTag, BOTTOM_BORDER);
    }

    /**
     * 打印线程信息
     *
     * @param level
     */
    private void showTheadInfo(int level) {
        print(level, mTag, String.format("%sThread:%s[%s]", MIDDLE_LEFT_BAR, Thread.currentThread().getName(), Thread.currentThread().getId()));
    }


    /**
     * 打印分隔线
     *
     * @param level
     */
    private void showDivider(int level) {
        print(level, mTag, MIDDLE_BORDER);
    }

    /**
     * 打印栈信息
     *
     * @param level
     */
    private void showStackTrace(int level) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        //避免超出栈的有效范围
        int count = trace.length - STACK_OFFSET;
        if (count > mStackCount) {
            count = mStackCount;
        }

        int numOffset;
        for (int i = count; i > 0; i--) {
            numOffset = count - i;
            StackTraceElement element = trace[STACK_OFFSET + i - 1];

            StringBuilder builder = new StringBuilder();
            builder.append(MIDDLE_LEFT_BAR);

            while (numOffset-- > 0) {//打印偏移
                builder.append(TEXT_OFFSET);
            }

            builder.append(isShowPackage ? element.getClassName() : getSimpleClassName(element.getClassName()))
                    .append(".")
                    .append(element.getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(element.getFileName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")");
            print(level, mTag, builder.toString());
        }

        if (count > 0) {
            showDivider(level);
        }
    }

    /**
     * 获取类名
     *
     * @param name
     * @return
     */
    private String getSimpleClassName(String name) {
        if (name != null) {
            int lastIndex = name.lastIndexOf(".");
            if (lastIndex == -1) {
                return null;
            } else {
                return name.substring(lastIndex + 1);
            }
        } else {
            return null;
        }
    }

    /**
     * 显示内容
     *
     * @param level
     * @param msg
     */
    private void showContent(int level, String msg) {
        byte[] bytes = msg.getBytes();
        int length = bytes.length;

        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            formatContent(level, mTag, new String(bytes, i, count));
        }
    }

    /**
     * 格式化内容 主要是换行操作
     *
     * @param level
     * @param tag
     * @param content
     */
    private void formatContent(int level, String tag, String content) {
        String[] lines = content.split(LR);
        for (String line : lines) {
            print(level, tag, MIDDLE_LEFT_BAR + line);
        }
    }

    /**
     * 打印
     *
     * @param level 等级
     * @param tag   标签
     * @param msg   内容
     */
    private static void print(int level, String tag, String msg) {
        Log.println(level, tag, msg);
    }
}
