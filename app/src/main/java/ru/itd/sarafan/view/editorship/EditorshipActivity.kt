package ru.itd.sarafan.view.editorship

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.hannesdorfmann.mosby3.mvi.MviActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.app_bar_main.*
import ru.itd.sarafan.R
import ru.itd.sarafan.rest.EditorsLoader
import ru.itd.sarafan.view.SpacesItemDecoration

/**
 * Created by macbook on 28.11.2017.
 */
class EditorshipActivity : MviActivity<EditorshipView, EditorshipPresenter>(), EditorshipView, EditorsController.ItemClickListener {

    @BindView(R.id.rv_editors) lateinit var rvEditors: RecyclerView

    private lateinit var editorsController: EditorsController
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editorship)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpRecyclerView()
    }


    private fun setUpRecyclerView(){
        rvEditors.setHasFixedSize(true)
        layoutManager = GridLayoutManager(applicationContext, 2)
        rvEditors.layoutManager = layoutManager
        rvEditors.addItemDecoration(SpacesItemDecoration(24))
        editorsController = EditorsController(this)
        rvEditors.adapter = editorsController.adapter
    }

    override fun onEditorClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(state: EditorshipState) {
        editorsController.setData(state.editors)
    }

    override fun startIntent(): Observable<Boolean> = Observable.just(true)

    override fun createPresenter(): EditorshipPresenter = EditorshipPresenter(EditorsLoader(applicationContext))

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