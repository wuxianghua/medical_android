# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\adtx86_64-20130729\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class tvName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontwarn  #dontwarn去掉警告
-dontskipnonpubliclibraryclassmembers

-dontwarn com.palmaplus.nagrand.**
-keep class com.palmaplus.nagrand.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#-dontwarn cn.jpush.**
#-keep class cn.jpush.** { *; }

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** {*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v4.app.Fragment

-keep public class * extends android.support.v7.**

#-keep class m.framework.** {*;}
#shareSDK
#-keep class cn.sharesdk.**{*;}
#-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
#-dontwarn cn.sharesdk.**
-dontwarn **.R$*
#-keep class m.framework.**{*;}

#-dontwarn com.handmark.pulltorefresh.library.internal.**
#-keep class com.handmark.pulltorefresh.library.internal.** { *;}

#-dontwarn com.handmark.pulltorefresh.**
#-keep class com.handmark.pulltorefresh.** { *;}


#高德地图包
#-dontwarn com.amap.api.**
#-dontwarn com.a.a.**
#-dontwarn com.autonavi.**
#-keep class com.amap.api.** {*;}
#-keep class com.autonavi.**  {*;}
#-keep class com.a.a.**  {*;}
#-keep class com.amap.api.location.**{*;}
#-keep class com.amap.api.maps2d.**{*;}
#-keep class com.amap.api.mapcore2d.**{*;}

-keep class **.R$* {   *;  }
-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆

#微信分享
#-keep class com.tencent.mm.sdk.** {
#   *;
#}

-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}

# 友盟统计添加 开始 ====================
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
# 友盟统计添加 结束 ====================

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }

#-keep class de.greenrobot.event.** { *; }
#-keep class de.greenrobot.event.util { *; }

#picasso
#-keepattributes SourceFile,LineNumberTable
#-keep class com.parse.*{ *; }
#-dontwarn com.parse.**
#-dontwarn com.squareup.picasso.**

#Glide
-dontwarn com.bumptech.**
-keep class com.bumptech.** {*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#alipay
#-dontwarn com.taobao.**
#-dontwarn com.alipay.**

#ActiveAndroid
#-dontwarn com.activeandroid.**
#-keep class * extends com.activeandroid.Model{ *; }


#rx
-dontwarn rx.**
-keep class * extends rx.**{ *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep class com.palmap.exhibition.launcher.**{*;}

#dagger2
-dontwarn dagger.**
-keep class dagger.**{*;}

-keep class com.facebook.**{*;}

-keep class com.google.**{*;}
-keep class com.android.**{*;}
-keep class com.vividsolutions.jts.**{*;}
-keep class com.balysv.**{*;}
-keep class com.jakewharton.**{*;}
-keep class javax.**{*;}
#-keep class com.palmap.library.base.UncaughtHandler{*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#retrofit
-dontwarn retrofit.**
-dontwarn retrofit2.**
-keep class retrofit.** { *; }
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-keep class okio.** {*;}

-keep class org.jetbrains.** {*;}
-keep interface org.jetbrains.** {*;}

#greenDao
-keep class de.**{*;}
-keep interface de.**{*;}
-keep class com.palmap.exhibition.dao.**{*;}
-keep class com.palmap.exhibition.view.**{*;}

#给展图用的keep
-keep class com.palmap.library.utils.** { *;}
-keep class com.palmap.library.presenter.** { *;}
-keep class com.palmap.library.view.** { *;}
-keep class com.palmap.library.base.** { *;}
-keep class com.palmap.library.rx.** { *;}
-keep class com.palmap.library.model.** { *;}
-keep class com.palmap.library.manager.** { *;}
-keep class com.palmap.library.delegate.** { *;}

-keep class com.palmap.exhibition.model.** { *;}
-keep class com.palmap.exhibition.launcher.** { *;}
-keep interface com.palmap.exhibition.launcher.** { *;}
-keep class com.palmap.exhibition.config.** { *;}
-keep class com.palmap.exhibition.widget.** { *;}
-keep class com.palmap.exhibition.di.** { *;}
-keep class com.palmap.exhibition.factory.** { *;}
-keep class com.palmap.exhibition.other.** { *;}
-keep class com.palmap.exhibition.navigator.** { *;}
-keep class com.palmap.exhibition.service.** { *;}
-keep class com.palmap.exhibition.AndroidApplication { *;}

-keep class me.imid.swipebacklayout.lib.** { *;}
#end
-keep class android.** { *;}
-keep class org.apache.** { *;}

#讯飞
-keep class com.palmap.iflyteklibrary.** {*;}
-keep class com.iflytek.** {*;}
#shardSDK
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.mob.tools.utils.R{*;}
-keep class m.framework.**{*;}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}