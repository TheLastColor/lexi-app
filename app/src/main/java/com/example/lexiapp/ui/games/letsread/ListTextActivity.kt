package com.example.lexiapp.ui.games.letsread

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityListTextBinding
import com.example.lexiapp.ui.adapter.TextAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListTextActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListTextBinding

    //Should be inject
    private val viewModel: TextViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTextBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setListener()
        setRecyclerView()
    }

    private fun setListener() {
        binding.btnArrowBack.setOnClickListener {
            finish()
        }
        binding.btnRandomText.setOnClickListener {
            //startActivity(Intent(this, LetsReadActivity::class.java))
        }
    }

    private fun setRecyclerView() {
        binding.rvText.layoutManager = LinearLayoutManager(this)
        suscribeToVM()
    }

    private fun suscribeToVM() {
        val gson = Gson()
        viewModel.listText.observe(this) { list ->
            binding.rvText.adapter = TextAdapter(list) {
                val intent = Intent(this, LetsReadActivity::class.java)
                intent.putExtra("TextToRead", gson.toJson(it))
                startActivity(intent)
            }
        }
    }


    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

}