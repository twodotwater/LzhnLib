/**百度推送工具包使用说明**/

注册Manifest文件添加以下内容：

	<!-- Push service 运行需要的权限 -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
    <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
    <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

	<!-- 应用组件注册信息 -->
    <application
        android:name="此处注册的application需要继承自-->FrontiaApplication"
        ... >
        
		<!-- 自定义MainActivity -->
        <activity
            android:name="com.lzhn.lib.MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- 自定义百度账号登陆的Activity -->
        <activity
            android:name="com.lzhn.push.BaiduLoginActivity"
            android:configChanges="orientation|keyboardHidden" >
        </activity>

        <!-- push富媒体，不使用富媒体推送不需要 -->
        <activity
            android:name="com.baidu.android.pushservice.richmedia.MediaViewActivity"
            android:configChanges="orientation|keyboardHidden"
            android:label="MediaViewActivity" >
        </activity>
        <activity
            android:name="com.baidu.android.pushservice.richmedia.MediaListActivity"
            android:configChanges="orientation|keyboardHidden"
            android:label="MediaListActivity"
            android:launchMode="singleTask" >
        </activity>

        <!-- push应用自定义消息receiver -->
        <receiver android:name="com.lzhn.lib.BaiduPushReceiver" >
            <intent-filter>
                <!-- 接收push消息 -->
                <action android:name="com.baidu.android.pushservice.action.MESSAGE" />
                <!-- 接收bind,unbind,fetch,delete等反馈消息 -->
                <action android:name="com.baidu.android.pushservice.action.RECEIVE" />
                <action android:name="com.baidu.android.pushservice.action.notification.CLICK" />
            </intent-filter>
        </receiver>

        <!-- push必须的receviver和service -->
        <receiver
            android:name="com.baidu.android.pushservice.PushServiceReceiver"
            android:process=":bdservice_v1" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <action android:name="com.baidu.android.pushservice.action.notification.SHOW" />
                <action android:name="com.baidu.android.pushservice.action.media.CLICK" />
            </intent-filter>
        </receiver>
        <receiver
            android:name="com.baidu.android.pushservice.RegistrationReceiver"
            android:process=":bdservice_v1" >
            <intent-filter>
                <action android:name="com.baidu.android.pushservice.action.METHOD" />
                <action android:name="com.baidu.android.pushservice.action.BIND_SYNC" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_REMOVED" />

                <data android:scheme="package" />
            </intent-filter>
        </receiver>

        <service
            android:name="com.baidu.android.pushservice.PushService"
            android:exported="true"
            android:process=":bdservice_v1" >
            <intent-filter>
                <action android:name="com.baidu.android.pushservice.action.PUSH_SERVICE" />
            </intent-filter>
        </service>
        <!-- push结束 -->

        <!-- 在百度开发者中心查询应用的API Key -->
        <meta-data
            android:name="api_key"
            android:value="8ctyZLQbniuamGmwrsObVi5b" />
    </application>