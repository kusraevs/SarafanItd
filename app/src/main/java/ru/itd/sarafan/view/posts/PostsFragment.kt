package ru.itd.sarafan.view.posts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviFragment
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.interactors.GetSearchQueryInteractor
import ru.itd.sarafan.rest.interactors.GetTagInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.view.SpacesItemDecoration
import ru.itd.sarafan.view.post.PostActivity


class PostsFragment : MviFragment<PostsView, PostsPresenter>(), PostsView, PostsController.ItemClickListener {
    override fun loadAgainIntent(): Observable<Boolean> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val visibleThreshold = 5

    @BindView(R.id.rv_posts) lateinit var rvPosts: RecyclerView

    private lateinit var postsController: PostsController
    private lateinit var layoutManager: LinearLayoutManager

    private val loadAgainPublishSubject = PublishSubject.create<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_list, container, false)
        ButterKnife.bind(this, view)
        setUpRecyclerView()
        return view
    }

    override fun createPresenter(): PostsPresenter {
        val categories = arguments?.getSerializable(ARG_CATEGORIES) as? Categories
        val tagTerm = arguments?.getSerializable(ARG_TAG) as? Term
        val searchQuery = arguments?.getString(ARG_SEARCH_QUERY)
        return PostsPresenter(GetTagInteractor(tagTerm),
                GetCategoriesInteractor(categories),
                GetSearchQueryInteractor(searchQuery))
    }

    override fun loadFirstPageIntent(): Observable<Boolean> {
        return Observable.just(true)
    }
    override fun startInitIntent(): Observable<Boolean> = Observable.just(true)

    override fun loadNextPageIntent(): Observable<Boolean> {
        return RxRecyclerView.scrollStateChanges(rvPosts)
                .filter { event -> event == RecyclerView.SCROLL_STATE_IDLE }
                .filter({ layoutManager.findLastVisibleItemPosition() > postsController.adapter.itemCount - visibleThreshold })
                .filter { postsController.loader.isShown }
                .map { true }
                .mergeWith(loadAgainPublishSubject)
    }

    override fun render(state: PostsViewState) {
        renderData(state.data, state.hasMore && state.error == null, state.error != null)
    }

    private fun renderData(data: List<Post>, showLoading: Boolean, showError: Boolean){
        postsController.setData(data, showLoading, showError)
    }


    private fun setUpRecyclerView(){
        rvPosts.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        rvPosts.layoutManager = layoutManager
        rvPosts.addItemDecoration(SpacesItemDecoration(24))
        postsController = PostsController(this)
        rvPosts.adapter = postsController.adapter
        rvPosts.adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    layoutManager.scrollToPosition(0)
                }
            }
        })
    }

    //Controller callbacks

    override fun onPostClick(post: Post) {
        val intent = Intent(activity, PostActivity::class.java)
        intent.putExtra(ARG_POST, post)
        startActivity(intent)
    }
    override fun onTagClicked(tagTerm: Term) {
        val intent = Intent(activity, PostsActivity::class.java)
        intent.putExtra(ARG_TAG, tagTerm)
        startActivity(intent)
    }
    override fun onLoadingPostErrorClick() {
        loadAgainPublishSubject.onNext(true)
    }



    companion object {
        val ARG_CATEGORIES = "categories"
        val ARG_POST = "post"
        val ARG_TAG = "tag"
        val ARG_SEARCH_QUERY = "searchQuery"

        fun newInstance(categories: Categories? = null, tag: Term? = null, search: String? = null): PostsFragment {
            val fragment = PostsFragment()
            val args = Bundle()
            if (categories != null)
                args.putSerializable(ARG_CATEGORIES, categories)
            if (tag != null)
                args.putSerializable(ARG_TAG, tag)
            if (search != null)
                args.putString(ARG_SEARCH_QUERY, search)
            fragment.arguments = args
            return fragment
        }
    }
}
