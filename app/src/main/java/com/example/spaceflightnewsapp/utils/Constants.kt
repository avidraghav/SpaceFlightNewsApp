package com.example.spaceflightnewsapp.utils

class Constants {
    companion object{
        const val BASE_URL_SPACEFLIGHT = "https://api.spaceflightnewsapi.net/v3/"
        const val BASE_URL_LAUNCHLIBRARY = "https://ll.thespacedevs.com/2.2.0/launch/"
        const val QUERY_PAGE_SIZE = 10
        const val DELAY_TIME = 700L
        const val ARTICLE_DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val LAUNCH_DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val DATE_OUTPUT_FORMAT = "MMMM dd,yyyy HH:mm z"
        const val CHANNEL_ID = "id"
        const val CHANNEL_NAME = "channel"
        const val NOTIFICATION_ID : Int = 0
        const val MinutestoMiliseconds : Long =900000 //15 minutes


    }
}