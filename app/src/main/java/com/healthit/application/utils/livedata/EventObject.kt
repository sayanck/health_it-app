package com.healthit.application.utils.livedata

class EventObject(key:Int, vararg value:Any) {
    private var key:Int?=null
    var value:Array<Any>
    init{
        this.key = key
        this.value = value as Array<Any>
    }
}