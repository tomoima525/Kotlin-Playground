package com.github.kttinunf.playground.todo.reducer

import com.github.kttinunf.playground.todo.state.AddTodoAction
import com.github.kttinunf.playground.todo.state.FilterTodoAction
import com.github.kttinunf.playground.todo.state.SetTodoAction
import com.github.kttinunf.playground.todo.state.TodoAction
import com.github.kttinunf.playground.todo.state.TodoFilter
import com.github.kttinunf.playground.todo.state.TodoListState
import com.github.kttinunf.playground.todo.state.TodoState
import com.github.kttinunf.playground.todo.state.ToggleTodoAction

fun todoReducer(state: TodoState, action: TodoAction): TodoState {
    return when (action) {
        is SetTodoAction -> {
            val alls = action.items
            val visibles = alls.filter { it.isVisibleWithFilter(state.filter) }
            TodoState(state.filter, visibles, alls)
        }
        is FilterTodoAction -> {
            val visibles = state.alls.filter { it.isVisibleWithFilter(action.setFilter) }
            TodoState(action.setFilter, visibles, state.alls)
        }
        is ToggleTodoAction -> {
            val index = action.index
            val toggled = state.visibles[index]

            val newAlls = state.alls.toMutableList().apply {
                val originalIndex = indexOf(toggled)
                val original = this[originalIndex]
                this[originalIndex] = TodoListState(!original.completed, original.title)
            }

            val visibles = newAlls.filter { it.isVisibleWithFilter(state.filter) }
            TodoState(state.filter, visibles, newAlls)
        }
        is AddTodoAction -> {
            val newTodo = TodoListState(false, action.text)

            val newAlls = state.alls.toMutableList()
            newAlls.add(newTodo)

            val visibles = newAlls.filter { it.isVisibleWithFilter(state.filter) }
            TodoState(state.filter, visibles, newAlls)
        }
    }
}

private fun TodoListState.isVisibleWithFilter(filter: TodoFilter) = when(filter) {
    TodoFilter.All -> true
    TodoFilter.Active -> !completed
    TodoFilter.Completed -> completed
}