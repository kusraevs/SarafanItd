package ru.itd.sarafan.view.posts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.utils.FragmentUtils

class PostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categories = intent.getSerializableExtra(PostsFragment.ARG_CATEGORIES) as? Categories
        val tag = intent.getSerializableExtra(PostsFragment.ARG_TAG) as? Term
        val searchQuery = intent.getSerializableExtra(PostsFragment.ARG_SEARCH_QUERY) as? String

        FragmentUtils.replaceFragment(supportFragmentManager, PostsFragment.newInstance(categories, tag, searchQuery), FragmentUtils.POST_FRAGMENT_TAG)

        tag?.let {
            supportActionBar?.title = tag.name
        }
        if (searchQuery != null && searchQuery.length > 1)
            supportActionBar?.title = searchQuery.substring(0, 1).toUpperCase() + searchQuery.substring(1).toLowerCase()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            else -> false
        }
    }

}
