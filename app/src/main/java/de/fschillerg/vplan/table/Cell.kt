package de.fschillerg.vplan.table

import com.evrencoskun.tableview.filter.IFilterableModel
import com.evrencoskun.tableview.sort.ISortableModel

class Cell(private val id: String) : ISortableModel, IFilterableModel {
    var data: String? = null

    private var filterKeyword: String? = null

    constructor(id: Int, data: String): this(id.toString()) {
        this.data = data
        this.filterKeyword = data
    }

    override fun getId(): String {
        return id
    }

    override fun getContent(): Any? {
        return data
    }

    override fun getFilterableKeyword(): String? {
        return filterKeyword
    }
}
