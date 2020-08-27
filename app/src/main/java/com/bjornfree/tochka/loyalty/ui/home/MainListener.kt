package com.bjornfree.tochka.loyalty.ui.home

interface MainListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message : String)
}