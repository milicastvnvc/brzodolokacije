package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.demoapp.apis.PostApiService
import com.example.demoapp.models.CustomResponse
import com.example.demoapp.models.Post
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostActivity : AppCompatActivity() {

    private lateinit var postApiService : PostApiService
    private lateinit var addPostBtn: ExtendedFloatingActionButton
    private var blogId: Long = 0
    private var blogTitle: String = ""
    private var postTitle: String = ""
    private var postDetails: String = ""
    private var postAuthor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        blogId = intent.getLongExtra("BLOG_ID",0)
        blogTitle = intent.getStringExtra("BLOG_TITLE").toString()
        val actionBar = supportActionBar

        actionBar!!.title = "Dodaj post na blog " + blogTitle

        actionBar.setDisplayHomeAsUpEnabled(true)

        postApiService = ServiceGenerator.buildService(PostApiService::class.java)

        addPostBtn = findViewById(R.id.addingBtn)
        addPostBtn.setOnClickListener {

            println("KLIK")
            postTitle = findViewById<EditText>(R.id.etPostTitle).text.toString()
            postDetails = findViewById<EditText>(R.id.etPostDetail).text.toString()
            postAuthor = findViewById<EditText>(R.id.etPostAuthor).text.toString()

            if (postTitle.isBlank() || postDetails.isBlank() || postAuthor.isBlank()){

                Toast.makeText(this@AddPostActivity,"Morate popuniti sva polja", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val call = postApiService.addPost(Post(0,postTitle,postDetails,postAuthor,blogId,null))

                postCall(call)
            }

        }

    }


    private fun postCall(call : Call<CustomResponse>) {

        call.enqueue(object : Callback<CustomResponse> {

            override fun onResponse(
                call: Call<CustomResponse>,
                response: Response<CustomResponse>
            ) {
                if (response.isSuccessful){
                    println("USPESNO DODATO!!!")

                    println(response.body()!!.inserted)

                    val intent = Intent(this@AddPostActivity,PostActivity::class.java).also{

                        it.putExtra("BLOG_ID",blogId)
                        it.putExtra("BLOG_TITLE", blogTitle)
                        it.putExtra("ADDING_FLAG", true)
                        startActivity(it)
                    }

                }

            }

            override fun onFailure(call: Call<CustomResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })

    }
}