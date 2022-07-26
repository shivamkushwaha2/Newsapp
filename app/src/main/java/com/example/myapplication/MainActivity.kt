package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        tabLayout = findViewById<TabLayout>(R.id.sliding_tabs)
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val titlesarray = arrayOf("My feed","Discover")
            tab.text = titlesarray[position]
        }.attach()

    }

    override fun onBackPressed() {
        if (tabLayout.selectedTabPosition != 0) {
            tabLayout.getTabAt(0)?.select()
        } else {
            super.onBackPressed()
        }
    }

}