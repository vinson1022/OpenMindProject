package com.vinson.datamodel

import com.google.gson.*
import java.lang.reflect.Type

object GsonUtil {
    val gson by lazy {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(String::class.java, StringConverter())
        gsonBuilder.create()
    }

    fun cloneObject(obj: Any): Any {
        val json = gson.toJson(obj)
        return gson.fromJson(json, obj.javaClass)
    }

    class StringConverter : JsonSerializer<String?>, JsonDeserializer<String> {
        override fun serialize(src: String?, typeOfSrc: Type,
                               context: JsonSerializationContext): JsonElement {
            return src?.let { JsonPrimitive(it) } ?: JsonPrimitive("")
        }

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type,
                                 context: JsonDeserializationContext): String {
            return json.asJsonPrimitive.asString
        }
    }
}