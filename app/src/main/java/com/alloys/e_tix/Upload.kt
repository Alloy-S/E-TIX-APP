package com.alloys.e_tix

class Upload {
    private var mName: String? = null
    private var mImageUrl: String? = null

    constructor() {

    }

    constructor(name : String, imageUrl : String) {
        if (name.trim().equals("")){
            mName = "No Name"
        }
        mName = name
        mImageUrl = imageUrl
    }

    fun getName(): String? {
        return mName
    }

    fun setName(name : String) {
        mName = name
    }

    fun getImageUrl(): String? {
        return mImageUrl
    }

    fun setImageUrl(imageUrl : String) {
        mImageUrl = imageUrl
    }
}