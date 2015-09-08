package cn.kerison.kit.log;

import android.content.Context;
import android.util.StringBuilderPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by kerison on 2015/9/8.
 */
public class UExceptionKit implements Thread.UncaughtExceptionHandler {


    private String path;
    private boolean isAppend;
    private OnAfterExceptionListener listener;

    public UExceptionKit(Context context, String path, boolean isAppend) {

        this.path = path;
        this.isAppend = isAppend;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        String result = collectInfo();
        LogKit.e(result, ex);
        if (!isAppend) {
            path = String.format("%s_%tF", path, new Date());
        }
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, isAppend));
            pw.println(result);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.listener != null) {
            listener.onAfter();
        }
    }

    private String collectInfo() {
        StringBuilder result = new StringBuilder();
        StringBuilderPrinter printer = new StringBuilderPrinter(result);


        return result.toString();
    }

    public void setOnAfterExceptionListener(OnAfterExceptionListener listener) {
        this.listener = listener;
    }


    public  interface OnAfterExceptionListener {
         void onAfter();
    }
}
