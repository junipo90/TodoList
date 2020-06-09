package me.junipo.todolist

import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.junipo.todolist.databinding.ActivityMainBinding
import me.junipo.todolist.databinding.ItemTodoBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val tempData = arrayListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tempData.add(Todo("코틀린"))
        tempData.add(Todo("자바", true))

        binding.itemRecyclerView.apply {
            layoutManager =LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(tempData,
                onClickDeleteIcon = {
                    deleteTodo(it)
                },
                onClickItem = {
                    toggleTodo(it)
                }
            )
        }

        binding.addButton.setOnClickListener {
            addTodo()
        }

        //setContentView(R.layout.activity_main)
    }

    private fun toggleTodo(todo: Todo) {
        todo.isDone = !todo.isDone
        binding.itemRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun addTodo() {
        val todo = Todo(binding.editText.text.toString())
        tempData.add(todo)
        binding.itemRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun deleteTodo(todo: Todo) {
        tempData.remove(todo)
        binding.itemRecyclerView.adapter?.notifyDataSetChanged()
    }
}

data class Todo(
    val text: String,
    var isDone: Boolean = false
)

class TodoAdapter(
    private val myDataset: List<Todo>,
    val onClickDeleteIcon: (todo: Todo) -> Unit,
    val onClickItem: (todo: Todo) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoAdapter.TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return TodoViewHolder(ItemTodoBinding.bind(view))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = myDataset[position]
        holder.binding.todoText.text = todo.text

        if(todo.isDone) {
            holder.binding.todoText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        }else{
            holder.binding.todoText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }

        holder.binding.deleteImageButton.setOnClickListener {
            onClickDeleteIcon.invoke(todo)
        }

        holder.binding.root.setOnClickListener {
            onClickItem.invoke(todo)
        }

    }

    override fun getItemCount() = myDataset.size
}

