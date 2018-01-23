package com.github.kttinunf.playground

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class RxJavaActivity : AppCompatActivity() {

    val cities = Observable.just("Warsaw", "Paris", "London", "Madrid", "Tokyo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        concatMap()
//        flatMap()
//        concatMapEagerly()
    }

    fun concatMap() {
        cities.concatMap(::populationOf)
                .subscribe(this::print)
    }

    fun flatMap() {
        cities.flatMap(::populationOf)
                .subscribe(this::print)
    }

    fun concatMapEagerly() {
        cities.concatMapEager(::populationOf)
                .subscribe(this::print)
    }

    fun print(p: Int) {
        println("Population: $p")
    }
}

fun populationOf(city: String): Observable<Int> {
    println("Request --> ../population?q=$city")
    val map = mapOf("Warsaw" to 1702139, "Paris" to 2138551, "London" to 7556900, "Madrid" to 3255944, "Tokyo" to 27394596)
    return Observable.just(map[city]!!)
            .delay((500..1000).rand().toLong(), TimeUnit.MILLISECONDS)
            .doOnNext {
                println("Response <-- { $city : $it }")
            }
}

fun IntRange.rand(): Int {
    val r = Random()
    return start + r.nextInt(last - start)
}