package com.test.fdjapp.di

/**
 * NetworkMonitor is a interface meant to act as the API to all network monitoring related issues
 *
 * @param isConnected a function used to determine if the application is connected to a network or not
 * */
interface NetworkMonitor {
    fun isConnected(): Boolean
}