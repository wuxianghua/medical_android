package com.palmap.library.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.palmap.library.utils.DateUtil;
import com.palmap.library.utils.FileUtils;
import com.palmap.library.utils.IOUtils;
import com.palmap.library.utils.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class UncaughtHandler implements UncaughtExceptionHandler {

    private Context mContext;
    private static final String FILE_SUFFIX_NAME = ".log";

    private volatile static UncaughtHandler uncaughtHandler;
    private UncaughtExceptionHandler mDefaultCrashHandler;

    /**
     * 是否可以使用默认的处理器 默认使用
     */
    public static boolean canUseDefaultHandler = false;

    public static String LOGPATH = "crash";

    private UncaughtHandler(Context context) {
        mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static UncaughtHandler getInstance(Context context) {
        if (uncaughtHandler == null) {
            synchronized (UncaughtHandler.class) {
                if (uncaughtHandler == null) {
                    uncaughtHandler = new UncaughtHandler(context);
                }
            }
        }
        return uncaughtHandler;
    }

    /**
     * 全局捕获异常
     */
    @Override
    public final void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        try {
            // 保存错误到sd卡
            saveToSDCard(ex);
        } catch (Exception e) {//使用默认，即弹出对话框
            e.printStackTrace();
        } finally {
            // if (canUseDefaultHandler) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
            // } else {
            //    WActivityManager.getInstance().appExit(mContext);
            //}
        }
    }

    private final void saveToSDCard(Throwable ex) throws Exception {
        File fileDir = FileUtils.createSDDir(LOGPATH);
        File file = new File(fileDir, DateUtil.getFormatDate("yyyy-MM-dd_HH_mm_ss") + FILE_SUFFIX_NAME);
        LogUtil.d(file.getName());
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(
                    file)));
            // 获取当前系统时间
            pw.println(DateUtil.getCurrentDate());
            // 打印错误信息到流
            dumpPhoneInfo(pw);
            pw.println();
            // 保存到文件中
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeIO(pw);
        }
    }

    /**
     * 打印错误信息
     *
     * @param pw
     * @throws NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw)
            throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }

}
