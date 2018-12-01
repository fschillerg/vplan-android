package de.fschillerg.vplan.settings

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import de.fschillerg.vplan.R
import de.fschillerg.vplan.preferences.Preferences

class AddFormDialog {
    companion object {
        private const val tag = "AddFormDialog"

        private const val sekIIPosition = 5

        fun create(context: Context, adapter: FilterAdapter): MaterialDialog {
            Log.d(tag, "creating dialog")

            @Suppress("InflateParams")
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_form, null)

            val grade = view.findViewById<Spinner>(R.id.dropdown_grade)
            val form = view.findViewById<Spinner>(R.id.dropdown_form)
            val custom = view.findViewById<Switch>(R.id.custom)
            val customForm = view.findViewById<EditText>(R.id.custom_form)

            customForm.isEnabled = false

            ArrayAdapter.createFromResource(
                    context,
                    R.array.grades,
                    android.R.layout.simple_spinner_item
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                grade.adapter = it
            }

            val sekIAdapter = ArrayAdapter.createFromResource(
                    context,
                    R.array.formsSekI,
                    android.R.layout.simple_spinner_item
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            val sekIIAdapter = ArrayAdapter.createFromResource(
                    context,
                    R.array.formsSekII,
                    android.R.layout.simple_spinner_item
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            grade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position > sekIIPosition) {
                        form.adapter = sekIIAdapter
                    } else {
                        form.adapter = sekIAdapter
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }

            custom.setOnCheckedChangeListener { _, isChecked ->
                customForm.isEnabled = isChecked
                grade.isEnabled = !isChecked
                form.isEnabled = !isChecked
            }

            return MaterialDialog(context)
                .title(R.string.notifications)
                .customView(view = view)
                .noAutoDismiss()
                .positiveButton(R.string.add) {
                    val preferences = Preferences(context)
                    if (custom.isChecked) {
                        val text = customForm.text.toString()
                        if (text.isNotEmpty()) {
                            preferences.forms = preferences.forms.plusElement(text)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(context, context.getString(R.string.added), Toast.LENGTH_SHORT).show()
                            it.dismiss()
                        } else {
                            Toast.makeText(context, context.getString(R.string.custom_no_empty), Toast.LENGTH_SHORT)
                                    .show()
                        }
                    } else {
                        preferences.forms = preferences.forms.plusElement(
                                grade.selectedItem.toString() + "." + form.selectedItem.toString()
                        )
                        adapter.notifyDataSetChanged()
                        Toast.makeText(context, context.getString(R.string.added), Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                }.negativeButton(R.string.cancel) {
                    it.dismiss()
                }.also {
                        Log.d(tag, "dialog created")
                }
        }
    }
}
