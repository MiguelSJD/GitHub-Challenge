package br.com.challenge.githubchallenge.ui.details.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import br.com.challenge.githubchallenge.R
import br.com.challenge.githubchallenge.data.model.Item
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val item = intent.getSerializableExtra("item") as? Item
        showDetails(item)
    }

    private fun showDetails(item: Item?) {
        val ownerImageView = findViewById<ImageView>(R.id.ownerImageView)
        val ownerNameTextView = findViewById<TextView>(R.id.ownerNameTextView)
        val repositoryNameTextView = findViewById<TextView>(R.id.repositoryNameTextView)
        val repositoryStarsTextView = findViewById<TextView>(R.id.repositoryStarsTextView)
        val repositoryForksTextView = findViewById<TextView>(R.id.repositoryForksTextView)

        if (item != null) {
            ownerNameTextView?.text = item.owner.login

            repositoryNameTextView?.text = item.name
            repositoryStarsTextView?.text = item.stargazersCount.toString()
            repositoryForksTextView?.text = item.forks.toString()

            val requestOptions = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.circular_progress_bar)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .signature(ObjectKey(item.updatedAt))

            Glide.with(this)
                .load(item.owner.avatarUrl)
                .thumbnail(0.25f)
                .apply(requestOptions)
                .centerCrop()
                .into(ownerImageView)
        }
    }
}