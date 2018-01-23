package com.github.kttinunf.playground.todo

import addTo
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.kttinunf.playground.databinding.ListItemTodoBinding
import com.github.kttinunf.playground.todo.state.TodoListState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates

class TodoViewHolder(private val binding: ListItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {

    val itemClicks by lazy {
        Observable.create<Int> { emitter ->
            val view = binding.root
            view.setOnClickListener {
                emitter.onNext(adapterPosition)
            }

            emitter.setCancellable { view.setOnClickListener(null) }
        }
    }

    val itemLongClicks by lazy { Observable.empty<Int>() }

    fun bind(state: TodoListState) {
        with(binding) {
            listTodoComplete.isChecked = state.completed
            listTodoTitle.text = state.title

            val paintFlags = listTodoTitle.paintFlags
            listTodoTitle.paintFlags =
                    if (state.completed) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

class TodoAdapter : RecyclerView.Adapter<TodoViewHolder>() {

    private val itemToggleSubject = PublishSubject.create<Int>()
    val itemToggles = itemToggleSubject.hide()

    private val compositeDisposable = CompositeDisposable()

    private var items by Delegates.observable(emptyList<TodoListState>()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TodoViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = ListItemTodoBinding.inflate(layoutInflater, parent, false)
        val holder = TodoViewHolder(binding)
        holder.itemClicks.subscribe(itemToggleSubject::onNext).addTo(compositeDisposable)
        return holder
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(list: List<TodoListState>) {
        items = list
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}