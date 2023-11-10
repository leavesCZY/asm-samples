package github.leavesczy.trace.click.view

import android.graphics.Color
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import github.leavesczy.trace.click.R

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal class ViewClickAdapter(dataList: MutableList<Int>) :
    BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_view_click, dataList) {

    companion object {

        operator fun invoke(): ViewClickAdapter {
            val dataList = mutableListOf<Int>()
            val colors = listOf(
                "#FF0277BD",
                "#FF0277BD"
            )
            colors.forEach {
                dataList.add(Color.parseColor(it))
            }
            return ViewClickAdapter(dataList)
        }

    }

    override fun convert(holder: BaseViewHolder, item: Int) {
        val viewRoot = holder.getView<CardView>(R.id.viewRoot)
        viewRoot.setCardBackgroundColor(item)
    }

}