package cn.kerison.kit.log;

/**
 * 日志打印的配置
 * 支持链式调用，一般采用默认就够了
 */
public class LogConfig {

    private String tag;
    private int level;
    private int textLevel;
    private int stackCount;
    private boolean isShowThread;
    private boolean isShowPackage;

    private static final LogConfig DEFAULT = new LogConfig();

    public static LogConfig instance() {
        return DEFAULT;
    }

    private LogConfig() {
        this.tag = "LogKit㉿☞";
        this.level = LogLevel.ALL;
        this.textLevel = LogLevel.INFO;
        this.stackCount = 1;
        this.isShowPackage = false;
        this.isShowThread = false;
    }

    /**
     * 应用修改的配置
     */
    public void apply() {
        LogKit.update();
    }

    /**
     * 日志的Log
     *
     * @param tag TAG
     * @return 配置项本身
     */
    public LogConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() {
        return this.tag;
    }

    /**
     * 日志输出等级 {@link cn.kerison.kit.log.LogLevel}，默认ALL
     *
     * @param level 日志等级
     * @return 配置项本身
     */
    public LogConfig setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getLevel() {
        return this.level;
    }

    /**
     * 设置调用栈的个数
     *
     * @param count 调用栈的个数，默认1
     * @return 配置项本身
     */
    public LogConfig setStackCount(int count) {
        this.stackCount = count;
        return this;
    }

    public int getStackCount() {
        return this.stackCount;
    }

    /**
     * 显示日志类中的包名，默认隐藏
     *
     * @return 配置项本身
     */
    public LogConfig showPackage() {
        this.isShowPackage = true;
        return this;
    }

    /**
     * 隐藏日志类中的包名 ，默认隐藏
     *
     * @return 配置项本身
     */
    public LogConfig hidePackage() {
        this.isShowPackage = false;
        return this;
    }

    public boolean isShowPackage() {
        return this.isShowPackage;
    }

    /**
     * 显示调用线程信息
     *
     * @return 配置项本身
     */
    public LogConfig showThreadInfo() {
        this.isShowThread = true;
        return this;
    }

    /**
     * 隐藏调用线程信息
     *
     * @return 配置项本身
     */
    public LogConfig hideTheadInfo() {
        this.isShowThread = false;
        return this;
    }

    public boolean isShowThread() {
        return this.isShowThread;
    }

    /**
     * 只是默认Json/xml的输出级别 默认INFO级别
     *
     * @param level 显示的日志级别
     * @return 配置项本身
     */
    public LogConfig setTextLevel(int level) {
        this.textLevel = level;
        return this;
    }

    public int getTextLevel() {
        return this.textLevel;
    }
}
