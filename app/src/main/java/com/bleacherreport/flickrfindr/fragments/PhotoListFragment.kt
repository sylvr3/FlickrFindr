package com.bleacherreport.flickrfindr.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.activity.MainActivity
import com.bleacherreport.flickrfindr.adapter.PhotosAdapter
import com.bleacherreport.flickrfindr.model.PhotoSearchResponse
import com.bleacherreport.flickrfindr.model.PhotoSearchResult
import com.bleacherreport.flickrfindr.utils.UtilityFunctions
import com.bleacherreport.flickrfindr.viewmodel.PhotosViewModel
import kotlinx.android.synthetic.main.photo_list.*

class PhotoListFragment : Fragment(), PhotosAdapter.PhotoClickListener {

    companion object {
        const val PHOTO_LIST_FRAGMENT = "PHOTO_LIST_FRAGMENT"
        fun newInstance(query: String, perPage: Int) = PhotoListFragment().apply {
            arguments = Bundle().apply {
                putString(MainActivity.QUERY_STRING, query)
                putInt(MainActivity.NUM_OF_RESULTS_STRING, perPage)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.photo_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPhotos!!.layoutManager = LinearLayoutManager(activity)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            activity?.onBackPressed()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel =
            ViewModelProviders.of(this)
                .get(PhotosViewModel::class.java)
        arguments?.getString(MainActivity.QUERY_STRING)?.let {
            arguments?.getInt(MainActivity.NUM_OF_RESULTS_STRING)?.let { it1 ->
                viewModel.initialize(
                    it,
                    it1
                )
            }
        }
        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: PhotosViewModel) {
        viewModel.searchResponse.observe(this, Observer<PhotoSearchResponse> { response ->

            progressBar?.visibility = View.GONE
            if (response != null) {
                rvPhotos?.adapter = PhotosAdapter(response.photos?.photo!!)
                (rvPhotos?.adapter as PhotosAdapter).itemClickListener = this
                rvPhotos?.visibility = if (response.photos?.photo.isEmpty()) View.GONE else View.VISIBLE
            }
        })
    }

    override fun onItemClick(photoId: Long) {
        val viewModel = ViewModelProviders.of(this).get(PhotosViewModel::class.java)
        val photo = viewModel.getPhoto(photoId)!!
        val url = UtilityFunctions.generatePhotoURL(photo.farm.toString(), photo.id, photo.secret, photo.server)
        val photoDetailsFragment = PhotoDetailsFragment(requireContext())
        if (photo.title.isNullOrEmpty())
            photo.title = ""
        val fragment = photoDetailsFragment.newInstance(photo.title, url)
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment, "PHOTO_DETAILS_FRAGMENT")
        ft.addToBackStack("PHOTO_DETAILS_FRAGMENT")
        ft.commit()
    }

}