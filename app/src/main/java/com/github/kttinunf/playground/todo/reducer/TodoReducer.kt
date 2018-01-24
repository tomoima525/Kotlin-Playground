package com.github.kttinunf.playground.todo.reducer

import com.github.kttinunf.playground.todo.state.AddTodoAction
import com.github.kttinunf.playground.todo.state.DeleteTodoAction
import com.github.kttinunf.playground.todo.state.FilterTodoAction
import com.github.kttinunf.playground.todo.state.SetTodoAction
import com.github.kttinunf.playground.todo.state.TodoAction
import com.github.kttinunf.playground.todo.state.TodoListState
import com.github.kttinunf.playground.todo.state.TodoState
import com.github.kttinunf.playground.todo.state.ToggleTodoAction

fun todoReducer(state: TodoState, action: TodoAction): TodoState {
    return when (action) {
        is SetTodoAction -> {
            val alls = action.items
            TodoState(state.filter, alls)
        }
        is FilterTodoAction -> {
            TodoState(action.setFilter, state.alls)
        }
        is ToggleTodoAction -> {
            val index = action.index
            val toggled = state.visibles[index]

            val newAlls = state.alls.toMutableList().apply {
                val originalIndex = indexOf(toggled)
                val original = this[originalIndex]
                this[originalIndex] = TodoListState(!original.completed, original.title)
            }

            TodoState(state.filter, newAlls)
        }
        is AddTodoAction -> {
            val newTodo = TodoListState(false, action.text)

            val newAlls = state.alls.toMutableList()
            newAlls.add(newTodo)

            TodoState(state.filter, newAlls)
        }
        is DeleteTodoAction -> {
            val index = action.index
            val deleted = state.visibles[index]

            val newAlls = state.alls.toMutableList().apply {
                val newIndex = indexOf(deleted)
                removeAt(newIndex)
            }

            TodoState(state.filter, newAlls)
        }
    }
}

