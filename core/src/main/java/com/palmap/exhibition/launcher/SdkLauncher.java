package com.palmap.exhibition.launcher;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.exception.SdCardNotFoundException;
import com.palmap.library.cache.ACache;
import com.palmap.library.utils.FileUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.core.Engine;
import com.palmaplus.nagrand.io.FileCacheMethod;

import java.io.File;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by 王天明 on 2016/7/18.
 * <p/>
 * 启动室内地图sdk模块的启动器
 * <p/>
 * 1.提供双语启动(中文、英文)
 * 2.支持多参数启动,[building,floorId,featureId] 如果传入参数,则加载指定建筑的指定楼层,并选中指定的poi
 * 3.无参数启动,则启动 [app_key] 内默认的第一张地图的默认楼层
 * 4.该sdk中英文切换在内部完成,对外面主体接入app的语言环境没有影响
 */
public final class SdkLauncher {

    private static boolean engineInit = false;
    private static String KEY_CACHE = "key_cache";
    private static final long cacheTime = 12 * 60 * 60 * 1000;//12h
//    private static final long cacheTime =  1000;//测试 1s

    static {
        Engine.getInstance();
    }

    private static Observable<Boolean> startEngine(final Context context) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                if (engineInit) {
                    emitter.onNext(true);
                    emitter.onComplete();
                }
                try {
                    ACache aCache = ACache.get(context);
                    String cacheTimeStr = aCache.getAsString(KEY_CACHE);
                    if (!TextUtils.isEmpty(cacheTimeStr)) {
                        try {
                            long cacheTimeL = Long.parseLong(cacheTimeStr);
                            if (System.currentTimeMillis() - cacheTimeL >= cacheTime) {
                                clearCache();
                            }
                        } catch (Exception e) {
                        }
                    }
                    aCache.put(KEY_CACHE, System.currentTimeMillis() + "");
                    if (FileUtils.checkoutSDCard()) {
                        FileUtils.copyDirToSDCardFromAsserts(context, Config.LUR_NAME, "font", false);
                        FileUtils.copyDirToSDCardFromAsserts(context, Config.LUR_NAME, Config.LUR_NAME, true);
                    } else {
                        emitter.onNext(false);
                        return;
                    }
                    emitter.onNext(true);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) {
                        if (engineInit) return true;
                        if (aBoolean) {
                            Engine engine = Engine.getInstance(); //初始化引擎
                            LogUtil.e("初始化appKey:" + Config.APP_KEY);
                            engine.startWithLicense(Config.APP_KEY, AndroidApplication.getInstance());
                            engineInit = true;
                        }
                        return aBoolean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static void changeLanguage(Context context, Config.Language language) {
        Resources resources = context.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        Config.oldLanguage = config.locale;
        if (Config.Language.SIMPLIFIED_CHINESE.equals(language)) {
            config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
            Config.setLanguage(Config.Language.SIMPLIFIED_CHINESE);
        } else {
            config.locale = Locale.ENGLISH; //英文
            Config.setLanguage(Config.Language.ENGLISH);
        }
        resources.updateConfiguration(config, dm);
    }

    private static void launcher(final Context context, final LauncherListener listener, final Consumer<Integer> doOnNext, final LauncherModel launcherModel) {
        startEngine(context).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                if (listener != null) {
                    listener.onEngineLoading();
                }
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (!aBoolean) {
                    if (listener != null) {
                        listener.onError(new SdCardNotFoundException());
                    }
                    return;
                }
                if (doOnNext != null) {
                    try {
                        doOnNext.accept(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (launcherModel != null) {
//                    PalMapViewActivity.navigatorThis(context, launcherModel);
                    AndroidApplication.getInstance().getNavigator().toPalMapView(context,launcherModel);

                } else {
//                    PalMapViewActivity.navigatorThis(context);
                    AndroidApplication.getInstance().getNavigator().toPalMapView(context);
                }
//                if (buildingId > 0) {
//                    PalMapViewActivity.navigatorThis(context, title, buildingId, floorId, featureId);
//                } else {
//                    PalMapViewActivity.navigatorThis(context);
//                }
                if (listener != null) {
                    listener.onLauncherComplete();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                if (listener != null) {
                    listener.onError(throwable);
                }
            }
        });
    }

    /**
     * 以手机默认语言环境启动
     *
     * @param context
     * @param listener
     */
    public static void launcher(final Context context, final LauncherListener listener) {
        launcher(context, listener, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                Resources resources = context.getResources();//获得res资源对象
                Configuration config = resources.getConfiguration();//获得设置对象
                DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
                Config.oldLanguage = config.locale;
                if (config.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                    //config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
                    Config.setLanguage(Config.Language.SIMPLIFIED_CHINESE);
                } else {
                    Config.setLanguage(Config.Language.ENGLISH);
                }
                resources.updateConfiguration(config, dm);
            }
        }, null);
    }

    public static void launcher(final Context context, final LauncherListener listener, String title) {
        launcher(context, listener, new Consumer<Integer>() {
            @Override
            public void accept(Integer aVoid) {
                Resources resources = context.getResources();//获得res资源对象
                Configuration config = resources.getConfiguration();//获得设置对象
                DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
                Config.oldLanguage = config.locale;
                if (config.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
                    //config.locale = Locale.SIMPLIFIED_CHINESE; //简体中文
                    Config.setLanguage(Config.Language.SIMPLIFIED_CHINESE);
                } else {
                    Config.setLanguage(Config.Language.ENGLISH);
                }
                resources.updateConfiguration(config, dm);
            }
        }, new LauncherModel(-1, -1, -1, title));
    }

    public static void launcher(final Context context, final Config.Language language, final LauncherListener listener, final LauncherModel launcherModel) {
        launcher(context, listener, new Consumer<Integer>() {
            @Override
            public void accept(Integer aVoid) {
                changeLanguage(context, language);
            }
        }, launcherModel);
//                launcherModel.getTitle(), launcherModel.getPoiId(), launcherModel.getFloorId(), launcherModel.getFeatureId());
    }


    public static boolean clearCache() {
        try {
            new FileCacheMethod(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + Config.CACHE_FILE_PATH + File.separator).removeAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean clearCacheAtTime(long time) {
        try {
            new FileCacheMethod(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + Config.CACHE_FILE_PATH + File.separator).remove(time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
