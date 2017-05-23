package com.github.kttinunf.playground

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.rx.rx_string
import com.github.kittinunf.result.Result
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.floatingActionButton
import kotlinx.android.synthetic.main.content_main.editText
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bulletPoint1Immutability()
        bulletPoint2NullSafety()
        bulletPoint3Lambda()
        bulletPoint4Extension()
        bulletPoint5Functional()
        bulletPoint6DataClass()
        bulletPoint7DelegatesProperties()
        bulletPoint8Conciseness(floatingActionButton, editText,
                getPreferences(Context.MODE_PRIVATE))
        bulletPoint9Niceties()
    }

    /**
     * 1. Immutability
     *
     * Make you feel safe, when deal with Kotlin code
     *
     */
    fun bulletPoint1Immutability() {
        val i = 0
//        i = 7 //error

        var j = 0
        j = 17

        val k = listOf("1", "2", "3")
//        k = listOf("4", "5", "6")
        logD(k)

        val l = mutableListOf("1", "2", "3")
        l.add("4")
        l.add("5")
        l.add("6")
        logD(l)
    }

    /**
     * 2. Null safety
     *
     * Being able to specify things that are not null is huge
     *
     */

    fun countCharacters(s: String): Int {
        var count = 0
        for (value in s.withIndex()) {
            count = value.index
        }
        return count + 1
    }

    fun bulletPoint2NullSafety() {
        val stringWithoutSpecifiedType = "hello world"

        val nullableString: String? = null

        countCharacters("hello world")
//        countCharacters(null)

        nullableString?.length
        nullableString?.let {
            logD(it)
        }

        val nullableTypeButNotnullString: String? = "Step 2, null safety"
        nullableTypeButNotnullString?.length
        nullableTypeButNotnullString?.let {
            nullableTypeButNotnullString.length
            logD(nullableTypeButNotnullString)
        }
    }

    /**
     * 3. First class lambda
     *
     * Lambda is a first class citizen, say good bye to retro-lambda plugin
     *
     */

    fun <T> doOnEach(iter: Iterable<T>, action: ((T) -> Unit)? = null) {
        for (element in iter) action?.invoke(element)
    }

    fun bulletPoint3Lambda() {
        val a = listOf(1, 2, 3, 4, 5)
        doOnEach(a) { logV(it) }
    }

    /**
     * 4. Extension function
     *
     * If you know Ruby's 'open class', you can do that with Kotlin too
     *
     */

    fun Int.fib(): Int {
        var last = 0
        var prev = 1

        var result = 0
        for (i in 2..this) {
            result = last + prev
            last = prev
            prev = result
        }
        return if (this == 1) 1 else result
    }

    fun bulletPoint4Extension() {
        class Foo {
            init {
                logE("i'm in foo")
            }
        }

        val f = Foo()
        val i = 20.fib()
        val j = null ?: 20
        logD(i)
        logD(j)
        logE("Hello world")
    }


    /**
     * 5. Functional, functional everywhere
     *
     * If you know Ruby's 'open class', you can do that with Kotlin too
     *
     */

    fun doThingsFunctionally(): Triple<List<Int>, List<Int>, Int> {
        val divisibleBy3: (Int) -> Boolean = { it % 3 == 0 }

        val i = (0..100).filter(divisibleBy3)
        val j = (0..100).filter(divisibleBy3).map { it * it }
        val k = (0..100).filter(divisibleBy3).map(::powerOf2).reduce(Int::plus)
        return Triple(i, j, k)
    }

    fun bulletPoint5Functional() {
        val t = doThingsFunctionally()

        logV(t.first)
        logD(t.second)
        logE(t.third)
    }

    /**
     * 6. Data class and DSL for builder
     *
     * Say no to do the builder pattern by hand, Kotlin has this support built-in
     *
     */

    data class User(val first: String, val last: String)

    data class Address(var number: Int, var road: String, var zip: String)

    fun dataClassIsFun(): Pair<User, Address> {
        val u = User("Cookpad", "Love Cooking")
        val ad = Address(123, "山手通り", "153-00045")

        return Pair(u, ad)
    }

    fun builderBuilder(build: (Address) -> Unit): Address {
        val a = Address(345, "表参道通り", "153-0023")
        build(a)
        return a
    }

    fun bulletPoint6DataClass() {
        val (user, address) = dataClassIsFun()

        logE(user)
        logE(address)

        val newAddress = builderBuilder {
            it.number = 999
            it.road = "烏丸通"
            it.zip = "400-5654"
        }
        logV(newAddress)
    }

    /**
     * 7. Delegated properties
     *
     * Customized behaviour of properties in class, all is coming for free
     *
     */

    class Bar {
        val lazyValue by lazy {
            logD("This will be created on first use")
        }

        var observableValue by Delegates.observable(-999) { p, old, new ->
            logE("Value was changed from $old -> $new")
        }
    }

    fun bulletPoint7DelegatesProperties() {
        val b = Bar()
        b.lazyValue
        b.lazyValue
        b.lazyValue

        b.observableValue = 1
        b.observableValue = 2
        b.observableValue = 3
    }

    /**
     * 8. Conciseness
     *
     * If you feel that you hurt your fingers too much by typing in Java, try Kotlin
     *
     */

    fun bulletPoint8Conciseness(view: View, editText: EditText, pref: SharedPreferences) {
        view.setOnClickListener {
            view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        editText.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                logE(charSequence)
            }
        }

        //usage
        pref.edit {
            put("username" to "foo")
            put("userId" to "1234")
        }
    }

    inline fun SharedPreferences.edit(
            build: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.build()
        editor.apply()
    }

    fun SharedPreferences.Editor.put(p: Pair<String, String>) {
        val (first, second) = p
        putString(first, second)
    }

    fun bulletPoint9Niceties() {
        val responses = "https://httpbin.org/get".httpGet().rx_string()
        responses
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {
                    when (it) {
                        is Result.Success -> logD(it.value)
                        is Result.Failure -> logE(it.error)
                    }
                })
    }
}

fun powerOf2(i: Int) = i * i


