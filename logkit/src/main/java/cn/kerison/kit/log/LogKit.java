package cn.kerison.kit.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by kerison on 2015/5/19.
 */
public class LogKit {

    private static LogPrinter printer = new LogPrinter();
    private static LogConfig config = LogConfig.instance();

    static {
        printer.updateConfig(config);
    }

    private LogKit() {
    }

    public static void update(){
        printer.updateConfig(config);
    }

    public static LogConfig config() {
        return config;
    }

    public static void v(String content) {
        printer.v(content);
    }

    public static void v(String format, Object... args) {
        printer.v(String.format(format, args));
    }

    public static void v(Throwable tr) {
        printer.v(Log.getStackTraceString(tr));
    }

    public static void d(String content) {
        printer.d(content);
    }

    public static void d(String format, Object... args) {
        printer.d(String.format(format, args));
    }

    public static void d(Throwable tr) {
        printer.d(Log.getStackTraceString(tr));
    }

    public static void i(String content) {
        printer.i(content);
    }

    public static void i(String format, Object... args) {
        printer.i(String.format(format, args));
    }

    public static void i(Throwable tr) {
        printer.i(Log.getStackTraceString(tr));
    }

    public static void w(String content) {
        printer.w(content);
    }

    public static void w(String format, Object... args) {
        printer.w(String.format(format, args));
    }

    public static void w(Throwable tr) {
        printer.w(Log.getStackTraceString(tr));
    }

    public static void e(String content) {
        printer.e(content);
    }

    public static void e(String format, Object... args) {
        printer.e(String.format(format, args));
    }

    public static void e(Throwable tr) {
        printer.e(Log.getStackTraceString(tr));
    }

    public static void wtf(String content) {
        printer.wtf(content);
    }

    public static void wtf(String format, Object... args) {
        printer.wtf(String.format(format, args));
    }

    public static void wtf(Throwable tr) {
        printer.wtf(Log.getStackTraceString(tr));
    }

    public static void json(JSONObject jsonObject) {

        if (jsonObject == null) {
            printer.e("Log.json data is null");
            return;
        }
        try {
            printer.text(jsonObject.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
            printer.e("Log.json Error :\n" + e.getMessage());
        }
    }


    public static void json(JSONArray jsonArray) {
        if (jsonArray == null) {
            printer.e("Log.json data is null");
            return;
        }
        try {
            printer.text(jsonArray.toString(4));
        } catch (JSONException e) {
            printer.e("Log.json Error :\n" + e.getMessage());
        }
    }

    /**
     * 打印json
     *
     * @param jsonText
     */
    public static void json(String jsonText) {
        if (isTextEmpty(jsonText)) {
            printer.e("Log.json data is empty!");
            return;
        }
        jsonText = jsonText.trim();
        try {
            if (jsonText.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonText);
                printer.text(jsonObject.toString(4));
            } else if (jsonText.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonText);
                printer.text(jsonArray.toString(4));
            } else {
                printer.e("Log.json data is " + jsonText);
            }
        } catch (JSONException e) {
            printer.e("Log.json Error :\n" + e.getMessage());
        }
    }


    public static void xml(String xml) {
        if (isTextEmpty(xml)) {
            printer.e("LogKit.xml data is empty!");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            printer.text(xmlOutput.getWriter().toString());
        } catch (TransformerException e) {
            printer.e("Log.xml Error :\n" + e.getMessage());
        }
    }

    private static boolean isTextEmpty(String text) {
        return text == null || "".equals(text.trim()) || "null".equals(text.trim());
    }
}
