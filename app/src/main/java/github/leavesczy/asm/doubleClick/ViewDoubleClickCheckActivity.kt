package github.leavesczy.asm.doubleClick

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife
import butterknife.OnClick
import com.chad.library.adapter.base.BaseQuickAdapter
import github.leavesczy.asm.R

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 14:39
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class ViewDoubleClickCheckActivity : AppCompatActivity() {

    private var clickIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_double_click_check)
        findViewById<TextView>(R.id.tvObjectUnCheck).setOnClickListener(object :
            View.OnClickListener {
            @UncheckViewOnClick
            override fun onClick(v: View) {
                onClickView()
            }
        })
        findViewById<TextView>(R.id.tvObjectCheck).setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View) {
                onClickView()
            }
        })
        findViewById<TextView>(R.id.tvLambda).setOnClickListener {
            onClickView()
        }
        ButterKnife.bind(this)

        val clickDemoAdapter = ClickDemoAdapter()
        val rvList = findViewById<RecyclerView>(R.id.rvList)
        rvList.adapter = clickDemoAdapter
        rvList.layoutManager = LinearLayoutManager(this)
        clickDemoAdapter.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                onClickView()
            }
        clickDemoAdapter.onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
            override fun onItemChildClick(
                adapter: BaseQuickAdapter<*, *>?,
                view: View,
                position: Int
            ) {
                if (view.id == R.id.viewChild) {
                    onClickChildView()
                }
            }
        }
    }

    @OnClick(R.id.tvButterKnife)
    fun onClickViewByButterKnife(view: View) {
        val viewId = view.id
        if (viewId == R.id.tvButterKnife) {
            onClickView()
        }
    }

    @CheckViewOnClick
    fun onClickByXml(view: View) {
        onClickView()
    }

    private fun onClickView() {
        findViewById<TextView>(R.id.tvIndex).text = (clickIndex++).toString()
    }

    @SuppressLint("SetTextI18n")
    private fun onClickChildView() {
        findViewById<TextView>(R.id.tvIndex).text = "onClickChildView " + (clickIndex++)
    }

}