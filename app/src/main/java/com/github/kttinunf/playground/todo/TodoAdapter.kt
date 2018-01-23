package com.github.kttinunf.playground.todo

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.kttinunf.playground.databinding.ListItemTodoBinding
import com.github.kttinunf.playground.todo.state.TodoListState
import io.reactivex.Observable
import kotlin.properties.Delegates

class TodoViewHolder(private val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

    val itemClicks by lazy { Observable.empty<Int>() }

    val itemCheckToggles by lazy { Observable.empty<Int>() }
    
    val itemLongClicks by lazy { Observable.empty<Int>() }

    fun bind(state: TodoListState) {
        with(state) {
            binding.listTodoComplete.isChecked = completed
            binding.listTodoTitle.text = title

            val paintFlags = binding.listTodoTitle.paintFlags
            binding.listTodoTitle.paintFlags =
                    if (completed) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

class TodoAdapter : RecyclerView.Adapter<TodoViewHolder>() {

    private var items by Delegates.observable(emptyList<TodoListState>()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = ListItemTodoBinding.inflate(layoutInflater, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(list: List<TodoListState>) {
        items = list
    }
}