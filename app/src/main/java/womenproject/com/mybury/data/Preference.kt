package womenproject.com.mybury.data

import android.content.Context
import android.content.Context.MODE_PRIVATE


class Preference {


    companion object {

        private const val Preference = "prefercne"
        private const val GOOGLE_ACCOUNT = "google_account"
        private const val MYBURY_LOGIN_COMPLETE = "mybury_login_complete"
        private const val USER_ID = "user_id"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val SHOW_FILTER = "show_filter"
        private const val LISTUP_FILTER = "listup_filter"
        private const val SHOW_DDAY_STATE = "show_dday_state"
        private const val SHOW_DDAY_FILTER = "show_dday_filter"
        private const val CLOSE_ALARM_3_DAYS = "close_alarm_3_days"
        private const val IS_SHOWN_ALARM = "is_shown_alarm"

        fun setAccountEmail(context: Context, email: String) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(GOOGLE_ACCOUNT, email)
            editor.apply()
        }

        fun getAccountEmail(context: Context): String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(GOOGLE_ACCOUNT, "")
        }

        fun setMyBuryLoginComplete(context: Context, isComplete: Boolean) {
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

        fun getUserId(context: Context): String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(USER_ID, "")
        }


        fun setAccessToken(context: Context, token:String) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(ACCESS_TOKEN, token)
            editor.apply()
        }

        fun getAccessToken(context: Context): String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(ACCESS_TOKEN, "")
        }


        fun setRefreshToken(context: Context, token:String) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(REFRESH_TOKEN, token)
            editor.apply()
        }

        fun getRefreshToken(context: Context): String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(REFRESH_TOKEN, "")
        }


        fun setFilerForShow(context: Context, showFilter: ShowFilter) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(SHOW_FILTER, showFilter.toString())
            editor.apply()
        }

        fun getFilterForShow(context: Context) : String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(SHOW_FILTER, ShowFilter.all.toString())
        }

        fun setFilterListUp(context: Context, listUpFilter: ListUpFilter) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(LISTUP_FILTER, listUpFilter.toString())
            editor.apply()
        }

        fun getFilterListUp(context: Context) : String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(LISTUP_FILTER, ListUpFilter.updatedDt.toString())
        }

        fun setShowDdayFilter(context: Context, ddayShow : Boolean) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(SHOW_DDAY_STATE, ddayShow)
            editor.apply()
        }

        fun getShowDdayFilter(context: Context) : Boolean {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getBoolean(SHOW_DDAY_STATE, false)
            return data
        }

        fun setDdayFilerForShow(context: Context, showFilter: DdayShowFilter) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(SHOW_DDAY_FILTER, showFilter.toString())
            editor.apply()
        }

        fun getDdayFilterForShow(context: Context) : String? {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            return sp.getString(SHOW_DDAY_FILTER, DdayShowFilter.all.toString())
        }

        fun setCloseAlarm3Days(context : Context, closeTime : Long) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putLong(CLOSE_ALARM_3_DAYS, closeTime)
            editor.apply()
        }

        fun getCloseAlarm3Days(context: Context) : Long  {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getLong(CLOSE_ALARM_3_DAYS, 0)
            return data
        }

        fun setEnableShowAlarm(context: Context, isShowed: Boolean) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.putBoolean(IS_SHOWN_ALARM, isShowed)
            editor.apply()
        }

        fun getEnableShowAlarm(context: Context): Boolean {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val data = sp.getBoolean(IS_SHOWN_ALARM, true)
            return data
        }

        fun allClear(context: Context) {
            val sp = context.getSharedPreferences(Preference, MODE_PRIVATE)
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

    }

}