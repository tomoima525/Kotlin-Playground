package com.github.kttinunf.playground.todo

import addTo
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.View
import com.github.kttinunf.playground.R
import com.github.kttinunf.playground.databinding.ActivityTodoBinding
import com.github.kttinunf.playground.todo.contract.TodoContract
import com.github.kttinunf.playground.todo.state.TodoFilter
import com.github.kttinunf.playground.todo.viewmodel.TodoViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class TodoActivity : AppCompatActivity(), TodoContract.Input {

    private val firstLoadSubject = PublishSubject.create<Unit>()
    override val firstLoads: Observable<Unit> = firstLoadSubject.hide()

    private lateinit var binding: ActivityTodoBinding

    private val viewModel by lazy { TodoViewModel(this) }
    private val adapter by lazy { TodoAdapter() }

    private val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo)

        setupViews()
        setupBindings()

        firstLoadSubject.onNext(Unit)
    }

    private fun setupViews() {
        setTitle(R.string.title_todo)

        binding.todoList.also {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    private fun setupBindings() {
        with(binding) {
            //items
            viewModel.items
                    .subscribe(adapter::updateItems)
                    .addTo(disposeBag)

            //fitering text
            viewModel.filterTexts
                    .subscribe(todoFilteringText::setText)
                    .addTo(disposeBag)

            //show/hide empty
            viewModel.showEmptyViews.map { if (it) View.VISIBLE else View.GONE }
                    .subscribe(todoEmptyLayout::setVisibility)
                    .addTo(disposeBag)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todo, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

    override val addTodos: Observable<String>
        get() = TODO()

    override val toggleTodoAtIndexes: Observable<Int>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val deleteTodoAtIndexes: Observable<Int>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val filterTodos: Observable<TodoFilter> by lazy {
        Observable.create<TodoFilter> { emitter ->
            binding.todoFilteringText.setOnClickListener {
                PopupMenu(this, it).apply {
                    inflate(R.menu.menu_filter)
                    setOnMenuItemClickListener {
                        emitter.onNext(TodoFilter.valueOf(it.title.toString()))
                        true
                    }
                }.show()
            }
        }
    }

}