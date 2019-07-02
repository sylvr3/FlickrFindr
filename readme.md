FlickrFinder -- Sylvia Barnai

Please run with an Android device that runs API 26 (Android 8.0) or higher.

Features:
- Search for photos on Flickr (using flickr.photos.search method) and view full size of photos (using photo source URLs, see following reference: https://www.flickr.com/services/api/misc.urls.html)
- Search by entering text or through voice recognition (see instructions below)
- Can specify any number of results (25 is the default)
- Displays recently searched terms in a spinner labeled Recent Searches for quick search suggestions
- Able to bookmark images and view them offline

Languages, Libraries, and Frameworks used:

- Kotlin
- Java
- AndroidX (Lifecycle, RecyclerView)
- Retrofit
- RxJava
- Android AppCompat/Design
- Gson
- Glide
- Room


Basic Instructions:

There are two ways to search for images using FlickrFindr: either through entering text or through voice recognition.

Search By Entering Text

Enter the search term (query) in the edit text, then enter the number of results you wish to see displayed. The default value is set to 25. 
If number of results textbox is left blank, user will be prompted to enter number of results (similarly, if the search term is left blank, user will be asked to enter the search term for validation/error handling).

Search Through Voice Recognition

To search by voice, state the search term, followed by a comma and the number of results you would like to specify. 
For example, dog comma 40 returns 40 results for the search term dog.

If you only say the search term, then by default, it will show 25 results.


