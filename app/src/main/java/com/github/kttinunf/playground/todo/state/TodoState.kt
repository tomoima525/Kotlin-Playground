package com.github.kttinunf.playground.todo.state

enum class TodoFilter {
    All,
    Active,
    Completed,
}

data class TodoState(
        val filter: TodoFilter = TodoFilter.All,
        val alls: List<TodoListState> = emptyList()) {
    val visibles: List<TodoListState> = alls.filter { it.isVisibleWithFilter(filter) }
}

private fun TodoListState.isVisibleWithFilter(filter: TodoFilter) = when(filter) {
    TodoFilter.All -> true
    TodoFilter.Active -> !completed
    TodoFilter.Completed -> completed
}

sealed class TodoAction

class SetTodoAction(val items: List<TodoListState>) : TodoAction()
class FilterTodoAction(val setFilter: TodoFilter) : TodoAction()
class ToggleTodoAction(val index: Int) : TodoAction()
class AddTodoAction(val text: String) : TodoAction()
class DeleteTodoAction(val index: Int) : TodoAction()