package com.github.kttinunf.playground.todo.state

enum class TodoFilter {
    All,
    Active,
    Completed,
}

data class TodoState(
        val filter: TodoFilter = TodoFilter.All,
        val visibles: List<TodoListState> = emptyList(),
        val alls: List<TodoListState> = emptyList()
)

sealed class TodoAction

class SetTodoAction(val items: List<TodoListState>) : TodoAction()
class FilterTodoAction(val setFilter: TodoFilter) : TodoAction()