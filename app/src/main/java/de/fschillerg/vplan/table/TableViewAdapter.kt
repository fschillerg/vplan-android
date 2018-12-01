package de.fschillerg.vplan.table

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import de.fschillerg.vplan.R

class TableViewAdapter(private val context: Context) : AbstractTableAdapter<Cell, Cell, Cell>(context) {
    class CellViewHolder(view: View) : AbstractViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.cell_text)
    }

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.table_cell, parent, false)

        return CellViewHolder(layout)
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder?,
        cellItemModel: Any?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        val cell = cellItemModel as Cell
        val viewHolder = holder as CellViewHolder
        viewHolder.textView.text = cell.data

        viewHolder.itemView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        viewHolder.textView.requestLayout()
    }

    class ColumnHeaderViewHolder(view: View) : AbstractViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.column_header_text)
    }

    override fun onCreateColumnHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.table_column_header, parent, false)

        return ColumnHeaderViewHolder(layout)
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder?,
        columnHeaderItemModel: Any?,
        columnPosition: Int
    ) {
        val columnHeader = columnHeaderItemModel as Cell
        val viewHolder = holder as ColumnHeaderViewHolder
        viewHolder.textView.text = columnHeader.data

        viewHolder.itemView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        viewHolder.textView.requestLayout()
    }

    class RowHeaderViewHolder(view: View) : AbstractViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.row_header_text)
    }

    override fun onCreateRowHeaderViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.table_row_header, parent, false)

        return RowHeaderViewHolder(layout)
    }

    override fun onBindRowHeaderViewHolder(holder: AbstractViewHolder?, rowHeaderItemModel: Any?, columnPosition: Int) {
        val rowHeader = rowHeaderItemModel as Cell
        val viewHolder = holder as RowHeaderViewHolder
        viewHolder.textView.text = rowHeader.data

        viewHolder.itemView.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        viewHolder.textView.requestLayout()
    }

    override fun onCreateCornerView(): View {
        @Suppress("InflateParams")
        return LayoutInflater.from(context).inflate(R.layout.table_corner, null)
    }

    override fun getCellItemViewType(position: Int): Int {
        return 0
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }
}
