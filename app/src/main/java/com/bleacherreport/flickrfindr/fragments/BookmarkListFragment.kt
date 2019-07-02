package com.bleacherreport.flickrfindr.fragments

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.activity.MainActivity
import com.bleacherreport.flickrfindr.adapter.BookmarksAdapter
import com.bleacherreport.flickrfindr.db.Bookmark
import com.bleacherreport.flickrfindr.utils.Injector
import com.bleacherreport.flickrfindr.viewmodel.BookmarksViewModel


class BookmarkListFragment : Fragment() {

    private lateinit var viewModel: BookmarksViewModel

    private val bookmarksAdapter: BookmarksAdapter =
        BookmarksAdapter {
            onItemClick(it)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bookmark_list, container, false)
        val btnBackBookmarks = view.findViewById<Button>(R.id.btnBackBookmarks)
        btnBackBookmarks.setOnClickListener {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            activity?.onBackPressed()
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rvBookmarks).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = bookmarksAdapter
        }

        //setup ViewModel
        val factory = Injector.provideBookmarkViewModelFactory(context!!)
        viewModel = ViewModelProviders.of(activity!!, factory).get(BookmarksViewModel::class.java)

        viewModel.getAllBookmarks().observe(viewLifecycleOwner, Observer {
            bookmarksAdapter.loadItems(it)
        })
    }

    private fun onItemClick(model: Bookmark) {
        viewModel.updateSelectedBookmark(model)
        val bookmarkDetailsFragment = BookmarkDetailsFragment(requireContext()).newInstance(model.title, model.url, model.id)
        fragmentManager!!.beginTransaction().replace(R.id.fragmentContainer, bookmarkDetailsFragment).addToBackStack(null).commit()
    }
}