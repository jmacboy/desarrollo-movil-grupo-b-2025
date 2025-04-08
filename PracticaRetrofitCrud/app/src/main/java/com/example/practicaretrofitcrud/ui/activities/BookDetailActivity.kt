package com.example.practicaretrofitcrud.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practicaretrofitcrud.R
import com.example.practicaretrofitcrud.api.dto.Book
import com.example.practicaretrofitcrud.databinding.ActivityBookDetailBinding
import com.example.practicaretrofitcrud.ui.viewmodels.BookDetailViewModel

class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private val viewModel: BookDetailViewModel by viewModels()
    private var id: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModelObservers()
        setupEventListeners()

        id = intent.getLongExtra(PARAM_ID, 0)
        if (id == 0L) {
            return
        }

        viewModel.loadBook(id)
    }

    private fun setupEventListeners() {
        binding.btnBookDetailSave.setOnClickListener { doSave() }
        binding.btnBookDetailCancel.setOnClickListener { finish() }
    }

    private fun doSave() {
        val book = viewModel.book.value ?: Book()

        book.apply {
            nombre = binding.txtBookName.editText?.text.toString()
            autor = binding.txtBookAuthor.editText?.text.toString()
            editorial = binding.txtBookEditorial.editText?.text.toString()
            sinopsis = binding.txtBookSynopsis.editText?.text.toString()
            imagen = binding.txtBookImage.editText?.text.toString()
            isbn = binding.txtBookIsbn.editText?.text.toString()
        }
        viewModel.saveBook(book)
    }

    private fun setupViewModelObservers() {
        viewModel.book.observe(this) {
            if (it == null) {
                return@observe
            }
            binding.txtBookName.editText?.setText(it.nombre)
            binding.txtBookAuthor.editText?.setText(it.autor)
            binding.txtBookEditorial.editText?.setText(it.editorial)
            binding.txtBookSynopsis.editText?.setText(it.sinopsis)
            binding.txtBookImage.editText?.setText(it.imagen)
            binding.txtBookIsbn.editText?.setText(it.isbn)
        }
        viewModel.hasErrorSaving.observe(this) {
            if (it) {
                Toast.makeText(this, getString(R.string.error_saving_book), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.bookSaved.observe(this) {
            if (it != null) {
                val isInsert = id == 0L
                Log.d("RESULT", "Is insert sending $isInsert")
                val resultIntent = MainActivity.returnIntent(isInsert, it)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }

    }

    companion object {
        fun detailIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra(PARAM_ID, id)
            return intent
        }

        fun createIntent(context: Context): Intent {
            return Intent(context, BookDetailActivity::class.java)
        }

        private const val PARAM_ID = "id"
    }


}