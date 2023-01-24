package com.example.demoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.models.Blog
import kotlinx.android.synthetic.main.card_blog.view.*

class BlogAdapter(val blogs : MutableList<Blog>, val clickListener : (Blog) -> Unit) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    class BlogViewHolder(itemView : View, clickAtPosition: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_blog, parent,false)){

            clickListener(blogs[it])

        }
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val curPost = blogs[position]

        holder.itemView.apply {
            tvTitle.text = curPost.title;
        }
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

}