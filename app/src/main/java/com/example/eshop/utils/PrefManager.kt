package com.example.eshop.utils

import android.content.Context
import android.content.SharedPreferences

object PrefManager {

    private const val onBoardPref = "OnBoardKey"
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences

    fun setOnBoardFlag(privacyBit: Boolean, context: Context) {
        editor = context.getSharedPreferences(onBoardPref, Context.MODE_PRIVATE).edit()
        editor.putBoolean(onBoardPref, privacyBit)
        editor.apply()
    }

    fun getOnBoardFlag(context: Context): Boolean {
        sharedPreferences = context.getSharedPreferences(onBoardPref, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(onBoardPref, false)
    }

}