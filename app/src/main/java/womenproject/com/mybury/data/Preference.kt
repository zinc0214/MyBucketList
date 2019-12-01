package womenproject.com.mybury.data

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.content.Context.MODE_PRIVATE


class Preference {


    companion object {

        private const val Preference = "prefercne"
        private const val GOOGLE_ACCOUNT = "google_account"
        private const val MYBURY_LOGIN_COMPLETE = "mybury_login_complete"
        private const val USER_ID = "user_id"

        fun setAccountEmail(context: Context, nickname: String) {

            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(GOOGLE_ACCOUNT, nickname)
            editor.apply()
        }

        fun getAccountEmail(context: Context): String {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getString(GOOGLE_ACCOUNT, "")
            return data
        }

        fun setMyBuryLoginCompelete(context: Context, isComplete: Boolean) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(MYBURY_LOGIN_COMPLETE, isComplete)
            editor.apply()
        }

        fun getMyBuryLoginComplete(context: Context): Boolean {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getBoolean(MYBURY_LOGIN_COMPLETE, false)
            return data
        }

        fun setUserId(context: Context, userId: String) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(USER_ID, userId)
            editor.apply()
        }

        fun getUserId(context: Context): Boolean {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getBoolean(USER_ID, false)
            return data
        }

    }

}