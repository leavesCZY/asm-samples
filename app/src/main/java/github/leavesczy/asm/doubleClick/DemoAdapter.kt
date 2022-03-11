package github.leavesczy.asm.doubleClick

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import github.leavesczy.asm.R

/**
 * @Author: leavesCZY
 * @Date: 2021/12/8 22:33
 * @Desc:
 */
class DemoAdapter(dataList: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_string, dataList) {

    companion object {

        fun get(): DemoAdapter {
            val dataList = mutableListOf<String>()
            for (i in 1..20) {
                dataList.add(i.toString())
            }
            return DemoAdapter(dataList)
        }

    }

    private val randomColors =
        listOf(Color.BLUE, Color.YELLOW, Color.GREEN, Color.LTGRAY, Color.CYAN)

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvIndex, item)
        holder.setBackgroundColor(
            R.id.tvIndex,
            randomColors[holder.adapterPosition % randomColors.size]
        )
        holder.addOnClickListener(R.id.viewChild)
    }

}