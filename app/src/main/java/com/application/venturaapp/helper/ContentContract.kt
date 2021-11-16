package com.application.venturaapp.helper

import android.net.Uri

class ContentContract {

    val AUTHORITY = "com.amazonaws.mobile.samples.notetaker.provider"

    /**
     * The content URI for the top-level content provider
     */
    public  val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
}