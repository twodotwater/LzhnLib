/**USB辅助类使用说明**/

AndroidManifest.xml文件添加内容如下：

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.lzhn.lib"
    android:versionCode="1"
    android:versionName="1.0" >

	<!-- 添加使用功能说明 -->
    <uses-feature android:name="android.hardware.usb.accessory" />
    <uses-feature android:name="android.hardware.usb.host" />

    <application
        android:name="com.lzhn.utils.os.CatchExcepApplication_v4"
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
        
        <!-- 添加使用library说明 -->
        <uses-library android:name="com.android.future.usb.accessory" />
        <!-- <uses-library android:name="com.android.future.usb.host" /> -->

        <activity
            android:name="com.lzhn.lib.MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                
                <!-- 添加USB连接响应过滤器 -->
                <!--
                <action android:name="android.hardware.usb.action.USB_ACCESSORY_ATTACHED" />
                <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
                -->

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

			<!-- 添加自定义USB设备信息过滤 -->
            <meta-data
                android:name="android.hardware.usb.action.USB_ACCESSORY_ATTACHED"
                android:resource="@xml/accessory_filter" />
            <meta-data
                android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
                android:resource="@xml/device_filter" />
        </activity>
       
    </application>

</manifest>