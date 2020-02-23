package com.programmers.android.apps.line.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.MemoListAdapter
import com.programmers.android.apps.line.adapters.viewholders.MemoItemClick
import com.programmers.android.apps.line.databinding.ActivityMemoListBinding
import com.programmers.android.apps.line.viewmodels.MemoListViewModel
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var memosListViewModel: MemoListViewModel
    private lateinit var memoListAdapter: MemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memosListViewModel = ViewModelProvider(this).get(MemoListViewModel::class.java)
        val binding: ActivityMemoListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memo_list)
        binding.memoListViewModel = memosListViewModel
        setSupportActionBar(toolbar)
        fab.setOnClickListener(this)
        memoListAdapter = MemoListAdapter(memoItemClickListener)

        memosListViewModel.allMemos.observe(this, Observer { memos ->
            memoListAdapter.memoList = memos
            memoListAdapter.notifyDataSetChanged()
        })

        memoRecyclerView.adapter = memoListAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_allmemo -> {
                memosListViewModel.deleteAllMemo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            fab -> {
                memosListViewModel.callActivity(
                    Intent(this, MemoDetailActivity::class.java))
            }
        }
    }

    /**
     * 메모 리스트중 아이템 클릭시 intent를 이용해 memoId를 전달
     * MemoDetatilActivity는 intent에 저장되어있는 정보를 통해 작성모드인지 읽기모드인지 판단
     */
    private val memoItemClickListener = object : MemoItemClick {
        override fun onClick(id: String) {
            memosListViewModel.callActivity(
                Intent(
                    this@MemoListActivity,
                    MemoDetailActivity::class.java)
                    .apply { putExtra("id", id) })
        }
    }
}
