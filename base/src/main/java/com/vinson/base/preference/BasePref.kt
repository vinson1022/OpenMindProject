package com.vinson.base.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class BasePref {
    abstract val repository: PreferenceRepository

    fun clear() = repository.clear()
}

class PreferenceRepository(context: Context, name: String) {
    val prefs: SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    val cache = HashMap<String, Any?>()

    fun clear() {
        cache.clear()
        prefs.edit().clear().apply()
    }
}

open class PreferenceProperty<T : Any?>(
    protected val repository: PreferenceRepository,
    protected val key: String,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    protected open fun read(): T {
        return repository.prefs.run {
            when (defaultValue) {
                is Int -> getInt(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                is Boolean -> getBoolean(key, defaultValue)
                is String -> getString(key, defaultValue) ?: defaultValue
                is String? -> getString(key, defaultValue) ?: defaultValue
                else -> {
                    getString(key, null)?.let { jsonString ->
                        Gson().fromJson<T>(jsonString, object : TypeToken<T>() {}.type)
                    } ?: defaultValue
                }
            } as T
        }
    }

    protected open fun save(value: T) {
        repository.prefs.edit().run {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> {
                    putString(key, if (value != null) Gson().toJson(value) else null)
                }
            }
        }.apply()
    }

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return try {
            with(repository.cache) {
                if (!containsKey(key)) {
                    read().also { put(key, it) }
                } else {
                    get(key) as T
                }
            }
        } catch (e: ClassCastException) {
            /**
             * Just for class cast error.
             * Just in case for default value class is diff from SharedPreference value.
             * Issue Link : https://play.google.com/apps/publish/?account=7968647459325366872#AndroidMetricsErrorsPlace:p=co.appedu.snapask&appid=4975305283606594527&appVersion=PRODUCTION&clusterName=apps/co.appedu.snapask/clusters/3fad9b61&detailsAppVersion&detailsSpan=7
             */
            defaultValue
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (value != repository.cache[key]) {
            save(value)
            repository.cache[key] = value
        }
    }
}

class ListPreferenceProperty<T: Any>(
    repository: PreferenceRepository,
    key: String,
    private val clazz: Class<T>
) : PreferenceProperty<List<T>>(repository, key, listOf()) {

    override fun read(): List<T> {
        val typeToken = TypeToken.getParameterized(List::class.java, clazz)
        return repository.prefs.getString(key, null)?.takeIf { it.isNotEmpty() }
                ?.let { Gson().fromJson<List<T>>(it, typeToken.type) }
                ?: listOf()
    }

    override fun save(value: List<T>) {
        repository.prefs.edit().putString(key, Gson().toJson(value)).apply()
    }
}

// OneShotBooleanPreference default value is false and will set true if someone try to get it
class OneShotBooleanPreference(
    repository: PreferenceRepository,
    key: String
) : UsageTimesLimitProperty<Boolean>(repository, key) {

    override fun read()
            = (if (!repository.prefs.getBoolean(key, false)) 0 else 1).also { repository.cache[key] = it }
    override fun save(lastRound: Int) {
        repository.prefs.edit().putBoolean(key, true).apply()
        repository.cache[key] = true
    }

    override fun isUnderLimit(round: Int) = round < 1

    // In this case return revert value mean it's first time to do something or toast not being seen before
    // And UsageTimesLimitPropertyCache will return is it under limit or not.
    override fun getValue(thisRef: Any, property: KProperty<*>) = !super.getValue(thisRef, property)
}

class LimitShotBooleanPreference(
    repository: PreferenceRepository,
    key: String,
    private val limitTimes: Int
) : UsageTimesLimitProperty<Int>(repository, key) {

    override fun read() = repository.prefs.getInt(key, 0).also { repository.cache[key] = it }
    override fun save(lastRound: Int) {
        repository.prefs.edit().putInt(key, lastRound + 1).apply()
        repository.cache[key] = lastRound + 1
    }

    override fun isUnderLimit(round: Int) = round < limitTimes
}

abstract class UsageTimesLimitProperty<T>(
    val repository: PreferenceRepository,
    val key: String
) : ReadOnlyProperty<Any, Boolean> {

    private var times = -1

    abstract fun read(): Int
    abstract fun save(lastRound: Int)

    abstract fun isUnderLimit(round: Int): Boolean

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        if (times < 0 || !repository.cache.containsKey(key)) times = read()
        return isUnderLimit(times).also { show ->
            if (show) save(times++)
        }
    }
}