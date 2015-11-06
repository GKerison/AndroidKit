//package cn.kerison.kit.log;
//
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.util.Date;
//
///**
// * Created by kerison on 2015/9/8.
// */
//public class UExceptionKit implements Thread.UncaughtExceptionHandler {
//    private String mVersionName;
//    private int mVersionCode;
//    private String mPackageName;
//
//    private String mPath;
//    private boolean isAppend;
//    private OnAfterExceptionListener mListener;
//
//    public  static void init(Context context,String path, boolean isAppend,OnAfterExceptionListener listener) {
//        UExceptionKit handler = new UExceptionKit(context,path, isAppend);
//        handler.setOnAfterExceptionListener(listener);
//        Thread.setDefaultUncaughtExceptionHandler(handler);
//    }
//
//    public UExceptionKit(Context context,String path, boolean isAppend) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
//            mVersionName = info.versionName;
//            mVersionCode = info.versionCode;
//            mPackageName = info.packageName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        this.mPath = path;
//        this.isAppend = isAppend;
//    }
//
//
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//
//        String result = collectInfo();
//        LogKit.e(result, ex);
//        if (!isAppend) {
//            mPath = String.format("%s_%tF", mPath, new Date());
//        }
//        File file = new File(mPath);
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            PrintWriter pw = new PrintWriter(new FileOutputStream(file, isAppend));
//            pw.println(result);
//            pw.println("------Exption-------");
//            ex.printStackTrace(pw);
//            pw.println("\n\n\n\n");
//            pw.flush();
//            pw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (this.mListener != null) {
//            mListener.onAfter();
//        }
//    }
//
//    private String collectInfo() {
//        StringBuilder result = new StringBuilder();
//        result.append("------ UncaughtException LOG ------");
//        result.append("\n\n");
//
//        Date date = new Date();
//        result.append(String.format("DateTime:%tF%tT", date, date));
//        result.append("\n\n");
//
//        result.append("App packageName: ");
//        result.append(mPackageName);
//        result.append("\n");
//
//        result.append("App versionName: ");
//        result.append(mVersionName);
//        result.append("\n");
//
//        result.append("App versionCode: ");
//        result.append(mVersionCode);
//        result.append("\n");
//
//        result.append("OS Version: ");
//        result.append(Build.VERSION.RELEASE);
//        result.append("_");
//        result.append(Build.VERSION.SDK_INT);
//        result.append("\n\n");
//
//
//        result.append("Vendor: ");
//        result.append(Build.MANUFACTURER);
//        result.append("\n");
//
//        result.append("Model: ");
//        result.append(Build.MODEL);
//        result.append("\n");
//
//        return result.toString();
//    }
//
//    public void setOnAfterExceptionListener(OnAfterExceptionListener listener) {
//        this.mListener = listener;
//    }
//
//
//    public  interface OnAfterExceptionListener {
//         void onAfter();
//    }
//}
