package com.yang.httpproxy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.OkHttpClient
import java.net.Proxy

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置不使用代理，防抓包
        val builder = OkHttpClient.Builder()
        builder.proxy(Proxy.NO_PROXY)
    }
}
