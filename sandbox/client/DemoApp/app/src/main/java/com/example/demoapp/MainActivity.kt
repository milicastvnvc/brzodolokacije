package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.apis.BlogApiService
import com.example.demoapp.models.Blog
import com.example.demoapp.models.CustomResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var addsBtn: FloatingActionButton
    //private lateinit var blogAdapter: BlogAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var blogApiService : BlogApiService
    private var blogTitle : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<RecyclerView>(R.id.rvBlogs)

        recyclerView.apply{
            layoutManager =  LinearLayoutManager(this@MainActivity)
        }

        addsBtn = findViewById(R.id.addingBtn)
        addsBtn.setOnClickListener { openDialog() }


        blogApiService = ServiceGenerator.buildService(BlogApiService::class.java)
        val call = blogApiService.getBlogs()

        getCall(call)

    }

    private fun openDialog() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_blog,null)

        val blogTitle = v.findViewById<EditText>(R.id.etBlogTitle)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Dodaj"){
                dialog,_->
            this.blogTitle = blogTitle.text.toString()


            val pCall = blogApiService.addBlog(Blog(0,this.blogTitle, mutableListOf()))

            postCall(pCall)

            dialog.dismiss()


        }
        addDialog.setNegativeButton("Otkazi"){
                dialog,_->
            dialog.dismiss()

        }
        addDialog.create()
        addDialog.show()
    }

    private fun getCall(call : Call<MutableList<Blog>>) {

        call.enqueue(object : Callback<MutableList<Blog>>{

            override fun onResponse(
                call: Call<MutableList<Blog>>,
                response: Response<MutableList<Blog>>
            ) {
                if (response.isSuccessful){
//                    println("USPESNO DOSTAVLJENO!!!")
                    recyclerView.apply{
                        adapter = BlogAdapter(response.body()!!) {
                            val blogToOpen = it;
                            //Toast.makeText(this@MainActivity, it.title, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity,PostActivity::class.java).also{

                                it.putExtra("BLOG_ID",blogToOpen.id)
                                it.putExtra("BLOG_TITLE", blogToOpen.title)
                                startActivity(it)
                            }

                        }


                    }
                }

            }

            override fun onFailure(call: Call<MutableList<Blog>>, t: Throwable) {
                t.printStackTrace()
                Log.e("error", t.message.toString())
            }

        })

    }

    private fun postCall(call : Call<CustomResponse>) {

        call.enqueue(object : Callback<CustomResponse>{

            override fun onResponse(
                call: Call<CustomResponse>,
                response: Response<CustomResponse>
            ) {
                if (response.isSuccessful){
                    println("USPESNO DODATO!!!")

                    println(response.body()!!.inserted)

                    if (response.body()!!.inserted){
                        val call = blogApiService.getBlogs()
                        getCall(call)

                        Toast.makeText(this@MainActivity,"Uspesno ste dodali blog '$blogTitle'", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this@MainActivity,"Blog sa zadatim nazivom vec postoji", Toast.LENGTH_SHORT).show()
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