package com.awesome.notes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.awesome.notes.ui.fragments.NotesListFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val noteListFragment = NotesListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noteListFragment)
            .commit()
    }
}