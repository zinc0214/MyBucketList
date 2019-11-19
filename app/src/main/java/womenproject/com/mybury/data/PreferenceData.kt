package womenproject.com.mybury.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceData {

    companion object {
        public val PREFERENCE_NAME = "preference"
        private val DEFAULT_STRING = ""
        public val GOOGLE_AUTH = ""

        private fun getPreference(context : Context) : SharedPreferences{
            return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun setString(context: Context, key : String, value : String) {
            val preferences = getPreference(context)
            val editor : SharedPreferences.Editor  = preferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getString(context: Context, key: String) : String {
            val preferences = getPreference(context)
            return preferences.getString(key, DEFAULT_STRING)
        }
    }
}