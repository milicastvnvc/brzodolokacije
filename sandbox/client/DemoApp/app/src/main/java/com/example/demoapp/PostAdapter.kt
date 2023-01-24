package com.example.demoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.models.Blog
import com.example.demoapp.models.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.card_blog.view.*

class PostAdapter(val posts : MutableList<Post>, val clickListener : (Post) -> Unit) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(postView : View,  clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(postView)
    {
        var title : TextView
        var detail : TextView
        var author : TextView
        var deleteButton: FloatingActionButton

        init {
            title = postView.findViewById(R.id.postTitle)
            detail = postView.findViewById(R.id.postDetail)
            author = postView.findViewById(R.id.postAuthor)
            deleteButton = postView.findViewById(R.id.deleteBtn)

            deleteButton.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostAdapter.PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        ){
            clickListener(posts[it])
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val curPost = posts[position]
        holder.title.text = curPost.title
        holder.detail.text = curPost.text
        holder.author.text = curPost.author

    }

    override fun getItemCount(): Int {
        return posts.size
    }
}