package com.programmers.android.apps.line.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.MemoListAdapter
import com.programmers.android.apps.line.adapters.viewholders.MemoItemClick
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.utilities.PermissionUtil
import com.programmers.android.apps.line.viewmodels.MemoListViewModel
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var memosListViewModel: MemoListViewModel
    private lateinit var memoListAdapter: MemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        setSupportActionBar(toolbar)

        ActivityCompat.requestPermissions(
            this,
            PermissionUtil.REQUIRED_PERMISSIONS,
            PermissionUtil.REQUEST_CODE_PERMISSIONS
        )
        fab.setOnClickListener(this)

        memosListViewModel = ViewModelProvider(this).get(MemoListViewModel::class.java)
        memosListViewModel.allMemos.value?.forEach { memo ->
            loge("${memo.memoId}, ${memo.memoTitle}, ${memo.memoDescription}, ${memo.memoImages.size}")
        }

        memoListAdapter = MemoListAdapter(memoItemClickListener)

        memosListViewModel.allMemos.observe(this, Observer { memos ->
            memoListAdapter.memoList = memos
            memoListAdapter.notifyDataSetChanged()
        })

        memoRecyclerView.apply {
            adapter = memoListAdapter
        }
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

    private val memoItemClickListener = object : MemoItemClick {
        override fun onClick(id: Int) {
            memosListViewModel.callActivity(
                Intent(
                    this@MemoListActivity,
                    MemoDetailActivity::class.java)
                    .apply { putExtra("id", id) })
        }
    }
}
