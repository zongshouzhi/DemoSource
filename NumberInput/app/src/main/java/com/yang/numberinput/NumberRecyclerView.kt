package com.yang.numberinput

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Looper
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NumberRecyclerView :RecyclerView{

    var count = 0
       set(value) {
           field = value
           adapter?.notifyDataSetChanged()
       }

    var numberChangeListener:NumberChangeListener? = null

    private var changeFocusablePosition = false

    private var current:Int = 0
    @SuppressLint("UseSparseArrays")
    private val datas = SparseArray<Char>()

    constructor(context:Context,attributeSet: AttributeSet):super(context,attributeSet)
    constructor(context: Context,attributeSet: AttributeSet,defStyle:Int):super(context,attributeSet,defStyle)

    init {
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true)
        adapter =NumberAdapter()
        addItemDecoration(SimpleItemDecoration(0,2,0,2))
    }

    fun setIndexData(index:Int,data:Char){
        if (index >= count){
            throw IndexOutOfBoundsException("count is $count, index is $index")
        }
        datas.put(index,data)
        considerChange()
    }

    fun setData(data: Char){
        datas.put(current,data)
        //只有true时需要改变focus
        considerChange()
    }

    private fun considerChange() {
        changeFocusablePosition = true
        adapter?.notifyDataSetChanged()
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        var result = ""
        var full = true
        for (index in count-1 downTo 0){
            val get = datas.get(index)
            get?.let {
                result+=get.toString()
            }?: kotlin.run {
                full = false
            }
        }
        numberChangeListener?.apply {
            if (full){
                this.onNumberFull(result)
            }else{
                this.onNumberChange(result)
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter !is NumberAdapter){
            throw IllegalArgumentException("NumberRecyclerView only use NumberAdapter!")
        }
        super.setAdapter(adapter)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        //measureSpec 前两位bit代表SpecMode 后30位代表size。所以下边的expandWidthSpec的模式为子view想要多大就有多大
        val expandWidthSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2,MeasureSpec.AT_MOST)
        super.onMeasure(expandWidthSpec, heightSpec)
    }

    inner class NumberAdapter : RecyclerView.Adapter<NumberViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_number_input,parent,false))
        }

        override fun getItemCount(): Int = count


        override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
            holder.setData(position)
        }

    }

    inner class NumberViewHolder(view:View):RecyclerView.ViewHolder(view),View.OnFocusChangeListener{
        init {
            itemView.onFocusChangeListener = this
            (itemView as EditText).showSoftInputOnFocus = false
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hasFocus){
                current = layoutPosition
            }
        }

        fun setData(position: Int){
            if (current+1 == position && changeFocusablePosition){
                changeFocusablePosition = false
                Looper.myQueue().addIdleHandler { itemView.requestFocus()
                false}
            }
            (itemView as EditText).setText(datas[position]?.toString()?:"")
        }
    }

    inner class SimpleItemDecoration(private val top:Int = 0,private val end:Int = 0,
                                     private val bottom:Int = 0,private val start:Int = 0):ItemDecoration(){
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            outRect.set(start,top,end,bottom)
        }
    }

    interface NumberChangeListener{
        fun onNumberChange(result:String)
        fun onNumberFull(result:String)
    }
}