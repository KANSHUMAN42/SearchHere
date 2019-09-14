package com.example.searchhere

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.items.view.*


 class GitHubAdapter(private val githubUsers:ArrayList<GithubUser>) : RecyclerView.Adapter<GitHubAdapter.GithubViewHolder>() {


     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            GithubViewHolder(LayoutInflater.from(parent?.context).inflate(
            R.layout.items,parent,false))


    override fun getItemCount()=githubUsers.size


    override fun onBindViewHolder(holder: GithubViewHolder, position: Int){
             holder?.bind(githubUsers[position])
    }

    class GithubViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
       fun bind (githubUser: GithubUser){
           itemView.tviewLogin?.text=githubUser.login;
           itemView.tviewUrl?.text=githubUser.html_url;
           itemView.tviewscore?.text=githubUser.score.toString();
           Picasso.get().load(githubUser.avatar_url).into(itemView?.imageprofile);

       }


    }


}