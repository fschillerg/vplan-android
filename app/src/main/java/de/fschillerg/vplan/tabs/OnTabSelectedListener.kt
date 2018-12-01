package de.fschillerg.vplan.tabs

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

class OnTabSelectedListener(
    private val viewPager: ViewPager,
    private val onSelect: (position: Int) -> Unit
) : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
        onSelect(tab.position)
    }

    override fun onTabUnselected(tab: TabLayout.Tab) { }

    override fun onTabReselected(tab: TabLayout.Tab) { }
}
