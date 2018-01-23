package com.github.kttinunf.playground.todo.repository

import com.github.kttinunf.playground.todo.model.Todo
import io.reactivex.Observable
import io.reactivex.Single

interface TodoRepositoryType {

    fun loadItems(): Single<List<Todo>>
}

class FakeTodoRepository : TodoRepositoryType {

    override fun loadItems(): Single<List<Todo>> =
            Observable.just(
                    Todo("1", false, "Buy some milk"),
                    Todo("2", true, "Go to post office"),
                    Todo("3", false, "Dinner with friends at Shibuya"))
                    .toList()
}