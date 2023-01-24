package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.apis.PostApiService
import com.example.demoapp.models.Blog
import com.example.demoapp.models.CustomResponse
import com.example.demoapp.models.DeleteResponse
import com.example.demoapp.models.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    private lateinit var postApiService : PostApiService
    private lateinit var addsPostBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var deleteButton : FloatingActionButton
    private var blogId : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        recyclerView = findViewById(R.id.rvPosts)
        recyclerView.apply{
            layoutManager =  LinearLayoutManager(this@PostActivity)
        }

        //Staviti umesto ovoga intent.getLongExtra("BLOG_ID",0)
        blogId = intent.getLongExtra("BLOG_ID",0)

        postApiService = ServiceGenerator.buildService(PostApiService::class.java)

        val call = postApiService.getPostByBlog(blogId)

        getCall(call);

        //za naslov activitija
        val blogTitle = intent.getStringExtra("BLOG_TITLE")

        val postAddFlag = intent.getBooleanExtra("ADDING_FLAG", false)

        if (postAddFlag){
           Toast.makeText(this, "Uspesno dodat post",Toast.LENGTH_SHORT).show()
        }

        val actionBar = supportActionBar

        actionBar!!.title = blogTitle

        actionBar.setDisplayHomeAsUpEnabled(true)

        addsPostBtn = findViewById(R.id.addingPostBtn)
        addsPostBtn.setOnClickListener {

            val intent = Intent(this@PostActivity,AddPostActivity::class.java).also{

                it.putExtra("BLOG_ID",blogId)
                it.putExtra("BLOG_TITLE", blogTitle)
                startActivity(it)
            }
        }
    }

    private fun getCall(call : Call<MutableList<Post>>) {

        call.enqueue(object : Callback<MutableList<Post>>{

            override fun onResponse(
                call: Call<MutableList<Post>>,
                response: Response<MutableList<Post>>
            ) {
                if (response.isSuccessful) {
                    println("USPESNO DOSTAVLJENO!!!")
                    println(response.body()!!.size)
                    for (post: Post in response.body()!!) {
                        println("nazvi posta" + post.title)
                    }
                    recyclerView.apply {
                        adapter = PostAdapter(response.body()!!){

                            val call = postApiService.deletePost(it.id)

                            deleteCall(call);
                        }
                    }
                }


            }

            override fun onFailure(call: Call<MutableList<Post>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })

    }

    private fun deleteCall(call : Call<DeleteResponse>) {

        call.enqueue(object : Callback<DeleteResponse>{

            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful){
                    println("USPESNO OBRISANP!!!")

                    println(response.body()!!.deleted)

                    if (response.body()!!.deleted){
                        val call = postApiService.getPostByBlog(blogId)

                        getCall(call);

                        Toast.makeText(this@PostActivity,"Uspesno ste obrisali post", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@PostActivity,"Doslo je do greske pri brisanju", Toast.LENGTH_SHORT).show()
                    }

                }

            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })

    }
}