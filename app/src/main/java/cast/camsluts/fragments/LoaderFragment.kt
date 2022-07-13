package cast.camsluts.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cast.camsluts.R
import cast.camsluts.adapters.LoaderAdapter
import cast.camsluts.adapters.addons.EndlessRecyclerOnScrollListener
import cast.camsluts.models.Model
import cast.camsluts.viewmodels.ViewModel
import kotlinx.android.synthetic.main.content_main.*

const val QUERY_PER_PAGE = 91

class LoaderFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[ViewModel::class.java] }
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:LoaderAdapter
    private lateinit var swipeToRefresh:SwipeRefreshLayout

    private var urlString:String = "https://chaturbate.com/?page="
    private var paginationCount:Int = 1

    companion object {
        fun newInstance(Url: String): LoaderFragment {
            return LoaderFragment().apply { arguments = Bundle().apply { putString("Key", Url) } }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            if (arguments!!.containsKey("Key")) urlString = arguments!!.getString("Key").toString()
        }
        adapter = LoaderAdapter(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_loader, container, false)
        swipeToRefresh = view.findViewById<View>(R.id.swipeToRefresh) as SwipeRefreshLayout
        recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView

        screenRotateWithGridManager()
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = DefaultItemAnimator()
        handleFirstLoad()
        handleInfinityScroll()
        handleSwipeToRefreshing()
        return view.rootView
    }

    private fun handleFirstLoad(){
        requireActivity().progressBar.visibility = View.VISIBLE
        viewModel.fetchData(urlString,paginationCount).observe(viewLifecycleOwner, Observer {
            adapter.addMore(it)
            recyclerView.adapter = adapter
            requireActivity().progressBar.visibility = View.GONE
        })
        requireActivity().progressBar.visibility = View.GONE
    }

    private fun handleInfinityScroll() {
        requireActivity().progressBar.visibility = View.VISIBLE
        recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(QUERY_PER_PAGE) {
            override fun onLoadMore() {
                paginationCount =+1
                viewModel.fetchData(urlString,paginationCount).observe(viewLifecycleOwner, Observer {
                    recyclerView.adapter.apply { adapter.addMore(it) }
                })
            }
        })
        requireActivity().progressBar.visibility = View.GONE
    }

    private fun handleSwipeToRefreshing(){
        swipeToRefresh.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_dark)
        swipeToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                var modelsList:MutableList<Model> = mutableListOf()
                recyclerView.adapter.apply {
                    modelsList.addAll(adapter.getModels())
                    adapter.clearAll()
                }
                adapter.addMore(modelsList)
                recyclerView.adapter = adapter
                swipeToRefresh.isRefreshing = false
            }
        })
    }

    private fun screenRotateWithGridManager(){
        if (recyclerView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        } else if (recyclerView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(requireActivity(), 4)
        }
    }

}