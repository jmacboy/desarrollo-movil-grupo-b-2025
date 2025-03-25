package com.example.practicarecyclerview.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicarecyclerview.R
import com.example.practicarecyclerview.databinding.ActivityPersonListBinding
import com.example.practicarecyclerview.models.Person
import com.example.practicarecyclerview.ui.adapters.PersonAdapter
import com.example.practicarecyclerview.ui.viewmodels.PersonListViewModel


class PersonListActivity : AppCompatActivity(), PersonAdapter.OnPersonClickListener {
    private lateinit var binding: ActivityPersonListBinding
    private val viewModel: PersonListViewModel by viewModels()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.data?.let {
                it.extras?.let { extras ->
                    val personChanged = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        extras.getSerializable(PARAM_UPDATED_OBJECT, Person::class.java)
                    } else {
                        extras.getSerializable(PARAM_UPDATED_OBJECT) as Person
                    }
                    val inserted = extras.getBoolean(PARAM_INSERTED)
                    if (inserted) {
                        personInserted(personChanged)
                    } else {
                        personChanged(personChanged)
                    }
                }
            }
        }

    private fun personInserted(personChanged: Person?) {
        val adapter = binding.lstPersons.adapter as PersonAdapter
        adapter.addItem(personChanged)
    }

    private fun personChanged(personChanged: Person?) {
        val adapter = binding.lstPersons.adapter as PersonAdapter
        adapter.updateItem(personChanged)
    }


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
        setupRecyclerView()
        setupViewModelObservers()
        setupEventListeners()
        viewModel.loadData()
    }

    private fun setupEventListeners() {
        binding.fabCreatePerson.setOnClickListener {
            val intent = PersonDetailActivity.createIntent(this)
            startForResult.launch(intent)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.personList.observe(this) {
            if (it == null || it.isEmpty()) {
                binding.lblEmptyText.visibility = View.VISIBLE
                binding.lstPersons.visibility = View.GONE
                return@observe
            }
            binding.lblEmptyText.visibility = View.GONE
            binding.lstPersons.visibility = View.VISIBLE
            val adapter = binding.lstPersons.adapter as PersonAdapter
            adapter.setData(it)
        }

    }

    private fun setupRecyclerView() {
        val adapter = PersonAdapter(mutableListOf())
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
        adapter.setOnPersonClickListener(this)

    }

    override fun onPersonClick(person: Person) {
        val id = person.id
        val intent = PersonDetailActivity.detailIntent(this, id)
        startForResult.launch(intent)
    }

    override fun onPersonDeleteClick(person: Person) {
        val index = viewModel.deletePerson(person)
        if (index == -1) {
            Toast.makeText(this, "Error al eliminar desde la lista", Toast.LENGTH_SHORT).show()
            return
        }
        val adapter = binding.lstPersons.adapter as PersonAdapter
        adapter.removeItem(index)
        if (adapter.itemCount == 0) {
            binding.lblEmptyText.visibility = View.VISIBLE
            binding.lstPersons.visibility = View.GONE
        } else {
            binding.lblEmptyText.visibility = View.GONE
            binding.lstPersons.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val PARAM_INSERTED = "inserted"
        private const val PARAM_UPDATED_OBJECT = "updatedPerson"
        fun returnIntent(isInsert: Boolean, person: Person): Intent {
            return Intent().apply {
                putExtra(PARAM_INSERTED, isInsert)
                putExtra(PARAM_UPDATED_OBJECT, person)
            }
        }
    }

}