package cn.kerison.kit.log;

/**
 * Created by kerison on 2015/11/6.
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

    public void apply(){
       LogKit.update();
    }

    public LogConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTag() {
        return this.tag;
    }

    public LogConfig setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getLevel() {
        return this.level;
    }

    public LogConfig setStackCount(int count) {
        this.stackCount = count;
        return this;
    }

    public int getStackCount() {
        return this.stackCount;
    }

    public LogConfig showPackage() {
        this.isShowPackage = true;
        return this;
    }

    public LogConfig hidePackage() {
        this.isShowPackage = false;
        return this;
    }

    public boolean isShowPackage() {
        return this.isShowPackage;
    }

    public LogConfig showThreadInfo() {
        this.isShowThread = true;
        return this;
    }

    public LogConfig hideTheadInfo() {
        this.isShowThread = false;
        return this;
    }

    public boolean isShowThread() {
        return this.isShowThread;
    }

    public LogConfig setTextLevel(int level) {
        this.textLevel = level;
        return this;
    }

    public int getTextLevel() {
        return this.textLevel;
    }
}
