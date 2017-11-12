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
import ru.itd.sarafan.R
import ru.itd.sarafan.di.DependencyUtils
import ru.itd.sarafan.rest.interactors.GetCategoriesInteractor
import ru.itd.sarafan.rest.interactors.GetTagInteractor
import ru.itd.sarafan.rest.model.Categories
import ru.itd.sarafan.rest.model.Post
import ru.itd.sarafan.rest.model.tags.Term
import ru.itd.sarafan.view.SpacesItemDecoration



class PostsFragment : MviFragment<PostsView, PostsPresenter>(), PostsView, PostsController.ItemClickListener {

    @BindView(R.id.rv_posts) lateinit var rvPosts: RecyclerView

    private lateinit var postsController: PostsController
    private lateinit var layoutManager: LinearLayoutManager
    private var presenterHolder: PostsPresenter.MainPresenterHolder? = null

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
        return PostsPresenter(GetTagInteractor(tagTerm),
                GetCategoriesInteractor(categories))
    }

    override fun loadFirstPageIntent(): Observable<Int> {
        return Observable.just(postsController.postsCount)
    }
    override fun startInitIntent(): Observable<Boolean> = Observable.just(true)

    override fun loadNextPageIntent(): Observable<Int> {
        return RxRecyclerView.scrollStateChanges(rvPosts)
                .map { postsController.postsCount }
               // .filter { event -> event == RecyclerView.SCROLL_STATE_IDLE }
                .filter({ layoutManager.findLastVisibleItemPosition() > postsController.adapter.itemCount - 5 })
    }

    override fun render(state: PostsViewState) {
        renderData(state.data, state.loading, state.error != null)
    }

    private fun renderData(data: List<Post>, isLoadingMore: Boolean, isError: Boolean){
        postsController.setData(data, isLoadingMore, isError)
    }


    private fun setUpRecyclerView(){
        rvPosts.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        rvPosts.layoutManager = layoutManager
        rvPosts.addItemDecoration(SpacesItemDecoration(24))
        postsController = PostsController()
        postsController.clickListener = this
        rvPosts.adapter = postsController.adapter
        rvPosts.adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    layoutManager.scrollToPosition(0)
                }
            }
        })
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PostsPresenter.MainPresenterHolder) {
            presenterHolder = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        presenterHolder = null
    }

    override fun onPostClick(post: Post) {
    }
    override fun onTagClicked(tagTerm: Term) {
        val intent = Intent(activity, PostsActivity::class.java)
        intent.putExtra(ARG_TAG, tagTerm)
        startActivity(intent)
    }



    companion object {
        val ARG_CATEGORIES = "categories"
        val ARG_TAG = "tag"

        fun newInstance(categories: Categories? = null, tag: Term? = null): PostsFragment {
            val fragment = PostsFragment()
            val args = Bundle()
            if (categories != null)
                args.putSerializable(ARG_CATEGORIES, categories)
            if (tag != null)
                args.putSerializable(ARG_TAG, tag)
            fragment.arguments = args
            return fragment
        }
    }
}
