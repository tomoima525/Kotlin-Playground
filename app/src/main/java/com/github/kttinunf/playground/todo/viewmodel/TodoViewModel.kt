package com.github.kttinunf.playground.todo.viewmodel

import com.github.kttinunf.playground.todo.contract.TodoContract
import com.github.kttinunf.playground.todo.model.Todo
import com.github.kttinunf.playground.todo.reducer.todoReducer
import com.github.kttinunf.playground.todo.repository.FakeTodoRepository
import com.github.kttinunf.playground.todo.repository.TodoRepositoryType
import com.github.kttinunf.playground.todo.state.FilterTodoAction
import com.github.kttinunf.playground.todo.state.SetTodoAction
import com.github.kttinunf.playground.todo.state.TodoListState
import com.github.kttinunf.playground.todo.state.TodoState
import io.reactivex.Observable

class TodoViewModel(view: TodoContract.Input, repository: TodoRepositoryType = FakeTodoRepository()) : TodoContract.Output {

    private val states: Observable<TodoState>

    init {
        val firstLoad = view.firstLoads
                .flatMapSingle { repository.loadItems().map(List<Todo>::mapToDoListStates) }
                .map(::SetTodoAction)

        val filterTodos = view.filterTodos
                .map(::FilterTodoAction)

        states = Observable.mergeArray(firstLoad, filterTodos)
                .scan(TodoState(), ::todoReducer)
                .skip(1)
                .replay()
                .autoConnect()
    }

    override val items: Observable<List<TodoListState>>
        get() = states.map(TodoState::visibles)

    override val showEmptyViews: Observable<Boolean>
        get() = items.map(List<*>::isEmpty)

    override val filterTexts: Observable<String>
        get() = states.map { it.filter.name }
}

private fun List<Todo>.mapToDoListStates(): List<TodoListState> =
        map { TodoListState(it.completed, it.title) }