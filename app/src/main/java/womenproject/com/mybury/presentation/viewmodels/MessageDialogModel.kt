package womenproject.com.mybury.presentation.viewmodels

import io.reactivex.subjects.CompletableSubject

/**
 * Created by HanAYeon on 2018. 12. 5..
 */

class MessageDialogModel(val title: () -> CharSequence,
                         val context: () -> CharSequence,
                         val positiveButton: Button,
                         val negativeButton: Button? = null,
                         val cancelable: Boolean = negativeButton != null) {


    val completeEvent: CompletableSubject = CompletableSubject.create()

    @Suppress("NOTHING_TO_INLINE")
    inline fun isComplete() = completeEvent.hasComplete()

    class Button(val text: () -> CharSequence, val onClick: () -> Unit)
}