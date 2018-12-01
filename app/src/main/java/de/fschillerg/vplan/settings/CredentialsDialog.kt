package de.fschillerg.vplan.settings

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.fschillerg.vplan.R
import de.fschillerg.vplan.preferences.Preferences

class CredentialsDialog {
    companion object {
        private const val tag = "CredentialsDialog"

        @SuppressLint("CheckResult")
        fun create(context: Context, cancelable: Boolean): MaterialDialog {
            Log.d(tag, "creating dialog")

            @Suppress("InflateParams")
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_credentials, null)

            val username = view.findViewById<EditText>(R.id.edit_username)
            val password = view.findViewById<EditText>(R.id.edit_password)

            val dialog = MaterialDialog(context)
                    .title(R.string.credentials)
                    .customView(view = view)
                    .noAutoDismiss()
                    .positiveButton(R.string.save) {
                        if (username.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                            Toast.makeText(context, context.getString(R.string.fill_out), Toast.LENGTH_LONG).show()
                        } else {
                            val preferences = Preferences(context)
                            preferences.username = username.text.toString()
                            preferences.password = password.text.toString()

                            if (!cancelable) {
                                preferences.setup = true
                            }

                            Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT).show()
                            it.dismiss()
                        }
                    }

            dialog.setCancelable(cancelable)

            if (cancelable) {
                Log.d(tag, "dialog set cancelable")
                dialog.negativeButton(R.string.cancel) {
                    it.dismiss()
                }
            } else {
                Log.d(tag, "dialog set non-cancelable")
            }

            Log.d(tag, "dialog created")
            return dialog
        }
    }
}
