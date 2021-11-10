package com.example.composecocktails.util

import android.content.Context
import android.net.ConnectivityManager

class Utils(private val context: Context) {

    fun isOnlineCheck(): Boolean {

        //https://stackoverflow.com/a/68643147
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork

        return currentNetwork != null
    }
}