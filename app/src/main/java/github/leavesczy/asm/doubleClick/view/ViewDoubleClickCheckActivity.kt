package github.leavesczy.asm.doubleClick.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import github.leavesczy.asm.R
import github.leavesczy.asm.doubleClick.UncheckViewOnClick

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 14:39
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class ViewDoubleClickCheckActivity : AppCompatActivity() {

    private var clickIndex = 1

    @Suppress("ObjectLiteralToLambda")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_double_click_check)
        title = "View 双击防抖"
        findViewById<TextView>(R.id.tvObjectUnCheck).setOnClickListener(object :
            View.OnClickListener {
            @UncheckViewOnClick
            override fun onClick(view: View) {
                onClickView()
            }
        })
        findViewById<TextView>(R.id.tvObjectCheck).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(view: View) {
                onClickView()
            }
        })
        findViewById<TextView>(R.id.tvLambda).setOnClickListener {
            onClickView()
        }
        val clickDemoAdapter = ClickDemoAdapter()
        clickDemoAdapter.addChildClickViewIds(R.id.viewChild)
        clickDemoAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                onClickView()
            }
        })
        clickDemoAdapter.setOnItemChildClickListener(object : OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>,
                view: View,
                position: Int
            ) {
                if (view.id == R.id.viewChild) {
                    onClickView()
                }
            }
        })
        val rvList = findViewById<RecyclerView>(R.id.rvList)
        rvList.adapter = clickDemoAdapter
        rvList.layoutManager = LinearLayoutManager(this)
    }

    //    @CheckViewOnClick
    fun onClickByXml(view: View) {
        onClickView()
    }

    private fun onClickView() {
        findViewById<TextView>(R.id.tvIndex).text = (clickIndex++).toString()
    }

}