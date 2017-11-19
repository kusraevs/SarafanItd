package ru.itd.sarafan.view.post

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_post.*
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.view.posts.PostsFragment
import android.webkit.WebViewClient
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.app.Fragment
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import io.reactivex.subjects.PublishSubject
import okhttp3.Route
import ru.itd.sarafan.SarafanApplication
import ru.itd.sarafan.rest.model.Category
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.utils.ResourceFile
import ru.itd.sarafan.utils.RouterUtils
import ru.itd.sarafan.utils.goShare
import ru.itd.sarafan.view.adapter.PostTagsAdapter
import ru.itd.sarafan.view.adapter.TagsAdapter
import ru.itd.sarafan.view.posts.PostsActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import javax.inject.Inject


class PostActivity : MviActivity<PostView, PostPresenter>(), PostView, TagsAdapter.TagClickListener {

    @BindView(R.id.iv_post) lateinit var ivPost: ImageView
    @BindView(R.id.web_view) lateinit var webView: WebView
    @BindView(R.id.progress_bar) lateinit var progressBar: ProgressBar
    @BindView(R.id.rv_tags) lateinit var rvTags: EpoxyRecyclerView
    @BindView(R.id.tv_post_title) lateinit var tvPostTitle: TextView

    private val shareClickSubject = PublishSubject.create<Boolean>()

    private val webViewClient = object : WebViewClient() {
        override fun onPageStarted(webView: WebView, url: String, favicon: Bitmap?) {
            progressBar.visibility = View.VISIBLE
            webView.visibility = View.GONE
        }
        override fun onPageFinished(webView: WebView, url: String) {
            progressBar.visibility = View.GONE
            webView.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            return true
        }
    }
    private lateinit var tagsLayoutManager: FlexboxLayoutManager
    @Inject
    protected lateinit var navigatorHolder: NavigatorHolder



    override fun startIntent(): Observable<Boolean> = Observable.just(true)
    override fun shareClickIntent(): Observable<Boolean> = shareClickSubject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        SarafanApplication.getComponent().inject(this)

        tagsLayoutManager = FlexboxLayoutManager(rvTags.context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }

        rvTags.layoutManager = tagsLayoutManager
        rvTags.setHasFixedSize(true)
        rvTags.isNestedScrollingEnabled = false

    }

    override fun createPresenter(): PostPresenter = PostPresenter(intent.getSerializableExtra(PostsFragment.ARG_POST) as Post)


    override fun render(state: PostViewState) {
        val post = state.post
        Glide.with(applicationContext).load(post.embedded.medias[0].imageUrl).into(ivPost)
        post.content?.let {
            val prefix = ResourceFile.readTextFromFile(applicationContext, R.raw.html_prefix)
            val postfix = ResourceFile.readTextFromFile(applicationContext, R.raw.html_postfix)
            renderWebView(StringBuilder().append(prefix)
                    .append(post.content.rendered)
                    .append(postfix).toString())
        }
        renderTags(post.embedded.getTagTerms())
        tvPostTitle.text = Html.fromHtml(post.title?.rendered)

    }

    private fun renderWebView(html: String) {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = false
        webSettings.loadWithOverviewMode = true

        webView.webViewClient = webViewClient
        webView.webChromeClient = WebChromeClient()
        webView.setOnLongClickListener({ true })

        //webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
    }

    private fun renderTags(tags: List<Term>){
        val tagsAdapter = PostTagsAdapter(rvTags.context, tags)
        tagsAdapter.clickListener = this
        //tagsAdapter.clickListener = clickListener
        rvTags.swapAdapter(tagsAdapter, false)
    }

    override fun onTagClicked(tagTerm: Term) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(PostsFragment.ARG_TAG, tagTerm)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                true
            }
            R.id.action_share -> {
                shareClickSubject.onNext(true)
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    private val navigator = Navigator { command ->
        when (command) {
            is Forward -> {
                when (command.screenKey) {
                    RouterUtils.SHARE_SCREEN -> {
                        goShare(command.transitionData as String)
                    }
                }
            }
        }
    }
}