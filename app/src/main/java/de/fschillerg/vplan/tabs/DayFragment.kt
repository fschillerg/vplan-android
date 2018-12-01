package de.fschillerg.vplan.tabs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.evrencoskun.tableview.TableView
import de.fschillerg.vplan.MainActivity
import de.fschillerg.vplan.R
import de.fschillerg.vplan.dialogs.DialogManager
import de.fschillerg.vplan.dialogs.RequestErrorDialog
import de.fschillerg.vplan.table.Cell
import de.fschillerg.vplan.table.TableViewAdapter
import de.fschillerg.vplan.vplan.Cache
import de.fschillerg.vplan.vplan.VplanClient
import de.fschillerg.vplan.vplan.Weekday

@SuppressLint("ValidFragment")
class DayFragment(
    context: Context,
    private val cache: Cache,
    val weekday: Weekday,
    private val dialogManager: DialogManager
) : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var fragmentView: View
    private lateinit var tableView: TableView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var tableAdapter: TableViewAdapter

    @Suppress("MagicNumber")
    private val columnHeaders: List<Cell> = listOf(
            Cell(0, context.getString(R.string.lesson)),
            Cell(1, context.getString(R.string.subject)),
            Cell(2, context.getString(R.string.teacher)),
            Cell(3, context.getString(R.string.room)),
            Cell(4, context.getString(R.string.info))
    )

    fun update(force: Boolean) {
        try {
            val vplan = if (force) {
                cache.update(weekday)
            } else {
                cache.get(weekday)
            }

            val table = vplan.toTable()

            tableAdapter.setAllItems(columnHeaders, table.rowHeaders, table.rows)

            var info = ""
            vplan.info.forEach {
                info += it + "\n"
            }
            fragmentView.findViewById<TextView>(R.id.info).text = info

            if (force) {
                (activity as MainActivity).updateToolbar(weekday)
            }

            swipe.isRefreshing = false
        } catch (error: VplanClient.RequestError) {
            swipe.isRefreshing = false

            if (!dialogManager.requestErrorShowed) {
                dialogManager.requestErrorShowed = true
                RequestErrorDialog.create(fragmentView.context).show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        swipe = view?.findViewById(R.id.swipe)!!
        swipe.setOnRefreshListener(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentView = view

        tableView = view.findViewById(R.id.table)
        tableView.rowHeaderRecyclerView.isNestedScrollingEnabled = true
        tableView.cellRecyclerView.isNestedScrollingEnabled = true
        tableAdapter = TableViewAdapter(view.context)
        tableView.adapter = tableAdapter

        update(false)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRefresh() {
        update(true)
    }
}
