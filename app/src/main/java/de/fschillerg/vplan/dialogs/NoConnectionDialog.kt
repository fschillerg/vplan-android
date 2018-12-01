package de.fschillerg.vplan.dialogs

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import de.fschillerg.vplan.R

class NoConnectionDialog {
    companion object {
        fun create(context: Context): MaterialDialog {
            return MaterialDialog(context)
                    .title(R.string.no_connection)
                    .message(R.string.no_connection_detailed)
                    .negativeButton(R.string.ok)
        }
    }
}
