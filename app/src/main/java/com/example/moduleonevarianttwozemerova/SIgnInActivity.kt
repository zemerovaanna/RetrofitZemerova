package com.example.moduleonevarianttwozemerova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.moduleonevarianttwozemerova.databinding.ActivitySignInBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SIgnInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.buttonSignIn.setOnClickListener {
            if(binding.editTextUsername.text.toString().isNotEmpty() && binding.editTextPassword.text.toString().isNotEmpty())
            {
                Request(mainApi)
            } else {
                Toast.makeText(this@SIgnInActivity, R.string.emptyfields, Toast.LENGTH_SHORT).show()
            }
        }

        binding.imageView.setOnClickListener{
            if(binding.buttonSignIn.text.toString() == getString(R.string.txtcontinue))
            {
                startActivity(Intent(this@SIgnInActivity, MainActivity::class.java))
            }
        }
    }

    fun Request(mainApi: MainApi) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = mainApi.auth(
                    AuthRequest(
                        binding.editTextUsername.text.toString(),
                        binding.editTextPassword.text.toString(),
                    )
                )
                runOnUiThread {
                    binding.apply {
                        Picasso.get().load(user.image).into(imageView)
                        val firstname = user.firstName
                        val lastname = user.lastName
                        title.text = "$firstname $lastname"
                        buttonSignIn.text = getString(R.string.txtcontinue)
                    }
                }
            } catch (e: HttpException) {
                runOnUiThread {
                    when (e.code()) {
                        400 -> {
                            // Handle HTTP 400 error
                            Toast.makeText(this@SIgnInActivity, R.string.badrequest, Toast.LENGTH_SHORT)
                                .show()
                        }
                        // Add more cases for other HTTP status codes if needed
                        else -> {
                            // Handle other HTTP status codes
                            Toast.makeText(this@SIgnInActivity, R.string.erroroccurred, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}