package ru.itd.sarafan.view.posts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        val tag = intent.getSerializableExtra(PostsFragment.ARG_TAG) as Term
        FragmentUtils.replaceFragment(supportFragmentManager, PostsFragment.newInstance(categories, tag), FragmentUtils.POST_FRAGMENT_TAG)

        supportActionBar?.title = tag.name
    }
}
