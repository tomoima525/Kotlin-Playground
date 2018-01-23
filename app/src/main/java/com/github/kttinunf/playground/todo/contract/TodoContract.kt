package com.github.kttinunf.playground.todo.contract

import com.github.kttinunf.playground.todo.state.TodoFilter
import com.github.kttinunf.playground.todo.state.TodoListState
import io.reactivex.Observable

interface TodoContract {

    interface Input {
        val firstLoads: Observable<Unit>

        val addTodos: Observable<String>

        val toggleTodoAtIndexes: Observable<Int>

        val deleteTodoAtIndexes: Observable<Int>

        val filterTodos: Observable<TodoFilter>
    }

    interface Output {
        val items: Observable<List<TodoListState>>

        val showEmptyViews: Observable<Boolean>

        val filterTexts: Observable<String>
    }
}