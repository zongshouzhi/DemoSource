package com.yang.maths

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (dl_main.isDrawerOpen(GravityCompat.START)) {
                dl_main.closeDrawer(GravityCompat.START)
            }else{
                dl_main.openDrawer(GravityCompat.START,true)
            }
        }
    }
}
