package com.example.practicarecyclerview.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicarecyclerview.R
import com.example.practicarecyclerview.databinding.ActivityPersonListBinding
import com.example.practicarecyclerview.models.Person
import com.example.practicarecyclerview.ui.adapters.NamesAdapter
import com.example.practicarecyclerview.ui.adapters.PersonAdapter
import com.example.practicarecyclerview.ui.viewmodels.PersonListViewModel

class PersonListActivity : AppCompatActivity(), PersonAdapter.PersonClickListener {
    private lateinit var binding: ActivityPersonListBinding
    private val viewModel: PersonListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPersonListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecylerView()
        setupViewModelObservers()
        viewModel.loadData()
    }

    private fun setupViewModelObservers() {
        viewModel.personList.observe(this) {
            val adapter = binding.lstPersons.adapter as PersonAdapter
            adapter.setData(it)
        }
    }

    private fun setupRecylerView() {
        val adapter = PersonAdapter(mutableListOf())
        adapter.setOnPersonClickListener(this)
//        adapter.setOnPersonClickListener {
//            Toast.makeText(this, it.id.toString() + " " + it.name, Toast.LENGTH_SHORT).show()
//        }
        val dividerItemDecoration = DividerItemDecoration(
            binding.lstPersons.context,
            LinearLayoutManager.VERTICAL
        )
        binding.lstPersons.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@PersonListActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            addItemDecoration(dividerItemDecoration)
        }

    }

    override fun onPersonClick(person: Person) {
//        Toast.makeText(this, person.id.toString() + " " + person.name, Toast.LENGTH_SHORT).show()
        val intent = PersonDetailActivity.detailIntent(this, person.id)
        startActivity(intent)
    }
}