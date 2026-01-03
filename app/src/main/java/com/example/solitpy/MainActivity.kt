package com.example.solitpy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class MainActivity : AppCompatActivity() {
    private var lastSelectedId: Int? = null // To keep track of the last selected item
    private var currentFragment: Fragment? = null // To keep track of the currently displayed fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set status and navigation bar colors
        window.statusBarColor = getColor(R.color.colorPrimary)
        window.navigationBarColor = getColor(R.color.md_white_1000)

        val bottomNavigationView = findViewById<ChipNavigationBar>(R.id.bottom_navigation)

        // Load the first fragment by default
        currentFragment = ConversionFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, currentFragment!!, "ConversionFragment")
            .commit()
        lastSelectedId = R.id.nav_Conversion // Set the initial selected ID
        bottomNavigationView.setItemSelected(lastSelectedId!!)

        // Set the item selected listener
        bottomNavigationView.setOnItemSelectedListener { itemId ->
            if (itemId != lastSelectedId) {
                // If a new item is selected, update the fragment
                lastSelectedId = itemId
                val newFragment = getFragmentForItemId(itemId)
                loadFragment(newFragment)
            }
            // No action needed for reselection, fragment remains the same
        }
    }

    private fun getFragmentForItemId(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.nav_ai -> AiFragment()
            R.id.nav_Conversion -> ConversionFragment()
            R.id.nav_call -> CallFragment()
            else -> null
        }
    }

    private fun loadFragment(newFragment: Fragment?) {
        if (newFragment != null) {
            // Check if the fragment is already added
            val fragmentTag = newFragment.javaClass.simpleName
            val existingFragment = supportFragmentManager.findFragmentByTag(fragmentTag)

            if (existingFragment != null) {
                // If the fragment already exists, show it
                supportFragmentManager.beginTransaction()
                    .hide(currentFragment!!) // Hide the currently displayed fragment
                    .show(existingFragment) // Show the existing fragment
                    .commit()
                currentFragment = existingFragment // Update the current fragment reference
            } else {
                // If the fragment doesn't exist, add it
                supportFragmentManager.beginTransaction()
                    .hide(currentFragment!!) // Hide the currently displayed fragment
                    .add(R.id.fragment_container, newFragment, fragmentTag) // Add the new fragment
                    .commit()
                currentFragment = newFragment // Update the current fragment reference
            }
        }
    }
}