package cn.kerison.kit.log;

import android.test.AndroidTestCase;

/**
 * Created by gaoxi on 2015/11/7 0007.
 */
public class LogTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LogKit.config().showPackage().showThreadInfo().setLevel(LogLevel.ALL).setStackCount(3).setTextLevel(LogLevel.VERBOSE).apply();
    }

    public void testLog(){

        LogKit.i("Hi !");
        LogKit.i("Hi %s !","kerison");
        LogKit.i(new NullPointerException("Null in test !"));
    }

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
        LogKit.d("testAndroidTestCaseSetupProperly");
    }
}
