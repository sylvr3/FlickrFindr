package com.bleacherreport.flickrfindr.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bleacherreport.flickrfindr.R
import com.bleacherreport.flickrfindr.db.AppDatabase
import com.bleacherreport.flickrfindr.db.DatabaseInitializer
import com.bleacherreport.flickrfindr.repository.PhotoRepository
import com.bleacherreport.flickrfindr.utils.Constants
import com.bleacherreport.flickrfindr.utils.UtilityFunctions

class MainActivity : AppCompatActivity() {
    private var appDatabase: AppDatabase? = null

    companion object {
        const val QUERY_STRING = "query_string"
        const val NUM_OF_RESULTS_STRING = "num_of_results_string"
        const val REQUEST_SPEECH_RECOGNIZER = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var searchText: String
        appDatabase = AppDatabase.getInstance(this)

        getRecentSearches()

        btnSearch.setOnClickListener {

            try {
                if (UtilityFunctions.isNetworkAvailable(this)) {
                    if (etQuery.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please enter search term", Toast.LENGTH_SHORT).show()
                    }
                    if (numberOfResultsText.text.toString().isEmpty()) {
                        Toast.makeText(this, "Please enter number of results", Toast.LENGTH_SHORT).show()
                        intent.putExtra(NUM_OF_RESULTS_STRING, Constants.DEFAULT_NUMBER_OF_RESULTS)

                    } else {
                        searchText = etQuery.text.toString()
                        val intent = Intent(this, PhotoActivity::class.java)
                        intent.putExtra(QUERY_STRING, searchText)
                        intent.putExtra(NUM_OF_RESULTS_STRING, numberOfResultsText.text.toString().toInt())
                        startActivity(intent)
                    }

                } else {
                    searchText = etQuery.text.toString()
                    Toast.makeText(
                        this,
                        "You are offline. Please connect to the internet to continue searching.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        ivVoiceSearch.setOnClickListener {
            if (UtilityFunctions.isNetworkAvailable(this)) {

                // Enable button if the isRecognitionAvailable() supported
                if (SpeechRecognizer.isRecognitionAvailable(this)) {
                    ivVoiceSearch.isEnabled = true

                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    }
                    startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER)

                } else {
                    Toast.makeText(this, "Speech recognition is not available", Toast.LENGTH_SHORT).show()
                }

            } else {
                searchText = etQuery.text.toString()
                Toast.makeText(
                    this,
                    "You are offline. Please connect to the internet to continue searching.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }


        btnViewBookmarks.setOnClickListener {
            val intent = Intent(this, BookmarkActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var numOfResults = 0

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val intent = Intent(this, PhotoActivity::class.java)
                    var searchText = results[0].split(",").get(0).trim()
                    intent.putExtra(QUERY_STRING, searchText)
                    if (numberOfResultsText.text.toString().isEmpty())
                        numOfResults = Constants.DEFAULT_NUMBER_OF_RESULTS
                    Log.i("Results size", results.size.toString());
                    for (result in results) {
                        if (!(result.contains(","))) { // if no comma and number stated, then default number of results is 25
                            numOfResults = Constants.DEFAULT_NUMBER_OF_RESULTS

                        } else {
                            numOfResults = results[0].split(",").get(1).trim().toInt()
                        }
                    }

                    intent.putExtra(NUM_OF_RESULTS_STRING, numOfResults)

                    PhotoRepository(this).getSearchResults(
                        searchText,
                        numOfResults
                    )
                    startActivity(intent)

                }
            }
        }
    }

    fun getRecentSearches() {
        val populatedSpinnerValues = DatabaseInitializer.getRecentSearches(appDatabase)

        var spinner = findViewById<Spinner>(R.id.recentSearchesSpinner)

        if (spinner != null) {

            val arrayAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, populatedSpinnerValues.asReversed())
            spinner.adapter = arrayAdapter

        }
    }
}









