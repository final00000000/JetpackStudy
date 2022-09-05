package com.example.jetpackstudy.im

interface IEventListener {
    fun dispatchEvent(aEventID: String?, success: Boolean, eventObj: Any?)
}