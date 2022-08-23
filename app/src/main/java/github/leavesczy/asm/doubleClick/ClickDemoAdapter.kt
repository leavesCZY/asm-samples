package github.leavesczy.asm.doubleClick

import android.graphics.Color
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import github.leavesczy.asm.R

/**
 * @Author: leavesCZY
 * @Date: 2021/12/8 22:33
 * @Desc:
 */
class ClickDemoAdapter(dataList: MutableList<Int>) :
    BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_card, dataList) {

    companion object {

        private val randomColors = listOf(
            "#FF018786",
            "#FF03DAC5",
            "#DA9C27B0",
            "#D300BCD4",
            "#DAFFEB3B",
        )

        operator fun invoke(): ClickDemoAdapter {
            val dataList = mutableListOf<Int>()
            for (i in 1..3) {
                randomColors.forEach {
                    dataList.add(Color.parseColor(it))
                }
            }
            return ClickDemoAdapter(dataList)
        }

    }

    override fun convert(holder: BaseViewHolder, item: Int) {
        val viewRoot = holder.getView<CardView>(R.id.viewRoot)
        viewRoot.setCardBackgroundColor(item)
        holder.addOnClickListener(R.id.viewChild)
    }

}