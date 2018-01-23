import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

//package com.github.kttinunf.playground
//
//import android.util.Log
//
//inline val <reified T> T.simpleName: String
//    get() = T::class.java.simpleName
//
//inline fun <reified T> T.logV(thing: Any? = null) {
//    log(simpleName, LogLevel.VERBOSE, thing)
//}
//
//inline fun <reified T> T.logD(thing: Any? = null) {
//    log(simpleName, LogLevel.DEBUG, thing)
//}
//
//inline fun <reified T> T.logE(thing: Any? = null) {
//    log(simpleName, LogLevel.ERROR, thing)
//}
//
//inline fun <reified T> T.log(tag: String, level: LogLevel = LogLevel.DEBUG, thing: Any? = null) {
//    when (level) {
//        LogLevel.VERBOSE -> Log.v(tag, thing.toString())
//        LogLevel.DEBUG -> Log.d(tag, thing.toString())
//        LogLevel.ERROR -> Log.e(tag, thing.toString())
//    }
//}
//
//enum class LogLevel {
//    VERBOSE,
//    DEBUG,
//    ERROR,
//}
//
//

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
