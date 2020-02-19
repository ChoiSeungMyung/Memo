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
import androidx.recyclerview.widget.LinearLayoutManager
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.MemoListAdapter
import com.programmers.android.apps.line.adapters.viewholders.MemoItemClick
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.utilities.PermissionUtil
import com.programmers.android.apps.line.viewmodels.MemoViewModel
import kotlinx.android.synthetic.main.activity_memo_list.*

class MemoListActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var memosViewModel: MemoViewModel
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

        memosViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)

        fab.setOnClickListener(this)

        memoListAdapter = MemoListAdapter(memoItemClickListener)

        memosViewModel.allMemos.observe(this, Observer { memos ->

            memos.forEach { memo ->
                loge("${memo.memoId}, ${memo.memoTitle}, ${memo.memoText}, ${memo.memoImages.size}")
            }
            memoListAdapter.memoList = memos
            memoListAdapter.notifyDataSetChanged()
        })

        memoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = memoListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                memosViewModel.deleteAllMemo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            fab -> {
                startActivity(Intent(this, MemoDetatilActivity::class.java))
            }
        }
    }

    private val memoItemClickListener = object : MemoItemClick {
        override fun onClick(id: Int) {
            val intent = Intent(this@MemoListActivity, MemoDetatilActivity::class.java).apply {
                putExtra("id", id)
            }
            startActivity(intent)
        }
    }
}
