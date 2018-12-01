package de.fschillerg.vplan.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import de.fschillerg.vplan.R

class RequestErrorDialog {
    companion object {
        fun create(context: Context): MaterialDialog {
            return MaterialDialog(context)
                    .title(R.string.request_error)
                    .message(R.string.request_error_detailed)
                    .negativeButton(R.string.ok)
        }
    }
}
