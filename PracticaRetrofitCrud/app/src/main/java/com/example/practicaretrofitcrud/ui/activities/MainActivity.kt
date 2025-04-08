package com.example.practicaretrofitcrud.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicaretrofitcrud.R
import com.example.practicaretrofitcrud.api.dto.Book
import com.example.practicaretrofitcrud.api.dto.Books
import com.example.practicaretrofitcrud.databinding.ActivityMainBinding
import com.example.practicaretrofitcrud.ui.adapters.BookAdapter
import com.example.practicaretrofitcrud.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), BookAdapter.OnBookClickListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.data?.let {
                it.extras?.let { extras ->
                    activityResultCallback(extras)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        setupViewModelObservers()
        setupEventListeners()
        viewModel.getBookList()

    }
    private fun activityResultCallback(extras: Bundle) {
        val bookChanged =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getSerializable(PARAM_UPDATED_OBJECT, Book::class.java)
            } else {
                extras.getSerializable(PARAM_UPDATED_OBJECT) as Book
            }

        val inserted = extras.getBoolean(PARAM_INSERTED)
        Log.d("RESULT", "Book changed: $bookChanged")
        Log.d("RESULT", "Book inserted: $inserted")
        if (inserted) {
            dataInsertedWithId(bookChanged)
        } else {
            dataUpdatedWithId(bookChanged)
        }
    }

    private fun dataInsertedWithId(bookChanged: Book?) {
        val adapter = binding.rvBooks.adapter as BookAdapter
        adapter.addItem(bookChanged)
        if (adapter.itemCount > 0) {
            binding.lblEmptyText.visibility = View.GONE
            binding.rvBooks.visibility = View.VISIBLE
        }
    }

    private fun dataUpdatedWithId(bookChanged: Book?) {
        val adapter = binding.rvBooks.adapter as BookAdapter
        adapter.updateItem(bookChanged)
    }

    private fun setupEventListeners() {
        binding.fabAddBook.setOnClickListener {
            val intent = BookDetailActivity.createIntent(this)
            startForResult.launch(intent)
        }
    }


    private fun setupViewModelObservers() {
        viewModel.bookList.observe(this) {
            if (it == null || it.isEmpty()) {
                binding.lblEmptyText.visibility = View.VISIBLE
                binding.rvBooks.visibility = View.GONE
                return@observe
            }
            binding.lblEmptyText.visibility = View.GONE
            binding.rvBooks.visibility = View.VISIBLE
            val adapter = binding.rvBooks.adapter as BookAdapter
            adapter.setData(it as Books)
        }
        viewModel.bookDeleted.observe(this) {
            if (it == null) {
                return@observe
            }
            val adapter = binding.rvBooks.adapter as BookAdapter
            adapter.removeItem(it)
            if (adapter.itemCount == 0) {
                binding.lblEmptyText.visibility = View.VISIBLE
                binding.rvBooks.visibility = View.GONE
            } else {
                binding.lblEmptyText.visibility = View.GONE
                binding.rvBooks.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = BookAdapter(arrayListOf())
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvBooks.context,
            LinearLayoutManager.VERTICAL
        )
        binding.rvBooks.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            addItemDecoration(dividerItemDecoration)
        }
        adapter.setOnBookClickListener(this)

    }

    override fun onBookClick(book: Book) {
        val id = book.id
        val intent = BookDetailActivity.detailIntent(this, id)
        startForResult.launch(intent)
    }

    override fun onBookDeleteClick(book: Book) {
        viewModel.deleteBook(book)
    }

    companion object {
        private const val PARAM_INSERTED = "inserted"
        private const val PARAM_UPDATED_OBJECT = "updatedBook"
        fun returnIntent(inserted: Boolean, book: Book): Intent {
            return Intent().apply {
                putExtra(PARAM_INSERTED, inserted)
                putExtra(PARAM_UPDATED_OBJECT, book)
            }
        }
    }
}