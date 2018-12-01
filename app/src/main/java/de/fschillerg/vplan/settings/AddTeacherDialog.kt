package de.fschillerg.vplan.settings

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.fschillerg.vplan.R
import de.fschillerg.vplan.preferences.Preferences

class AddTeacherDialog {
    companion object {
        private const val tag = "AddTeacherDialog"

        fun create(context: Context, adapter: FilterAdapter): MaterialDialog {
            Log.d(tag, "creating dialog")

            @Suppress("InflateParams")
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_teacher, null)

            val teacher = view.findViewById<EditText>(R.id.custom_teacher)

            return MaterialDialog(context)
                    .title(R.string.notifications)
                    .customView(view = view)
                    .noAutoDismiss()
                    .positiveButton(R.string.add) {
                        val preferences = Preferences(context)
                        val text = teacher.text.toString()
                        if (text.isNotEmpty()) {
                            preferences.teachers = preferences.teachers.plusElement(text)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(context, context.getString(R.string.added), Toast.LENGTH_SHORT).show()
                            it.dismiss()
                        } else {
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.teacher_no_empty),
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.negativeButton(R.string.cancel) {
                        it.dismiss()
                    }.also {
                        Log.d(tag, "dialog created")
                    }
        }
    }
}
