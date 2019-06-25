package com.example.moviesapp.api.adapter

import com.example.moviesapp.db.entities.Video
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class VideosJsonAdapter : JsonDeserializer<Video> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Video {
        var video = Video("")

        if (json != null && !json.isJsonNull) {

            val videoResults = json.asJsonObject?.get("results")?.asJsonArray

            videoResults?.forEach { jsonElement ->

                val videoOb = jsonElement.asJsonObject as JsonObject?

                val type = videoOb?.get("type")?.asString
                val site = videoOb?.get("site")?.asString
                if (type.equals("trailer", true) &&
                    site.equals("youtube", true)
                ) {
                    video = Video(videoOb?.get("key")?.asString)
                }
            }
        }

        return video
    }
}