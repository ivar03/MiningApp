package com.ivar7284.monerominingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.JsonObject
import com.ivar7284.monerominingapp.model.MarketPriceData
import com.ivar7284.monerominingapp.model.MarketPriceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    val TAG = "my_app"
    lateinit var lineChart: LineChart
    private lateinit var popupView: View
    private lateinit var popupWindow: PopupWindow

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //https://github.com/XMRig-for-Android/xmrig-for-android
        //https://github.com/XMRig-for-Android/xmrig-for-android/blob/main/BUILD.md
        //https://www.opensourceagenda.com/projects/android-xmrig-miner
//--------------------------------------------------------------------------------------------------

        //hash-rate ko bhi realtime karna hai---hoo jayega found a way
        //wallet screen recycler view

//        val commands = arrayOf(
//            "apt update && apt upgrade",
//            "pkg install git build-essential cmake -y",
//            "git clone https://github.com/xmrig/xmrig.git",
//            "apt upgrade",
//            "cd xmrig",
//            "mkdir build",
//            "cd build",
//            "cmake .. -DWITH_HWLOC=OFF",
//            "make -j10"
//        )


        val startBtn : Button = findViewById(R.id.startBtn)
        val setupBtn : Button = findViewById(R.id.setupBtn)
        val payoutBtn : LinearLayout = findViewById(R.id.payoutBtn)
        val profileBtn: LinearLayout = findViewById(R.id.profileBtn)
        val pBar: ProgressBar = findViewById(R.id.pBar)
        val gitUrl = "https://github.com/xmrig/xmrig.git"
        val localPath = File(applicationContext.filesDir, "xmrig_repository")
        localPath.setReadable(true)
        localPath.setWritable(true)
        localPath.setExecutable(true)


//        crashing app not working yet..
//        setupBtn.setOnClickListener {
//
//            val setupBtn: Button = findViewById(R.id.setupBtn)
//            setupBtn.setOnClickListener {
//                gitCloneViewModel.executeClone(gitUrl, localPath, progressListener)
//            }
//        }

        startBtn.setOnClickListener {
            startActivity(Intent(applicationContext, Details::class.java))
        }

        payoutBtn.setOnClickListener {
            startActivity(Intent(applicationContext, walletScreen::class.java ))
            finish()
        }
        profileBtn.setOnClickListener {
            startActivity(Intent(applicationContext, profileActivity::class.java ))

        }
// -------------------------------------------------------------------------------------------------
        //real-time karna ab-hi bacha hai-----done but not able to display the values in a popup when user touch
        //GRAPH-CODE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.blockchain.info/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val blockchainApiService = retrofit.create(BlockchainApiService::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            val marketPriceData = fetchMarketPriceData(blockchainApiService, "1year").values
            displayLineChart(marketPriceData)
        }
        lineChart = findViewById(R.id.chart)

        popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)
        popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        lineChart.setOnChartGestureListener(object : OnChartGestureListener {
            override fun onChartGestureStart(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                Log.d(TAG, "onChartGestureStart: x=${me?.x}, y=${me?.y}")
            }

            override fun onChartGestureEnd(me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?) {
                Log.d(TAG, "onChartGestureEnd: x=${me?.x}, y=${me?.y}")
            }

            override fun onChartLongPressed(me: MotionEvent?) {
                Log.d(TAG, "onChartLongPressed: x=${me?.x}, y=${me?.y}")
            }

            override fun onChartDoubleTapped(me: MotionEvent?) {
                Log.d(TAG, "onChartDoubleTapped: x=${me?.x}, y=${me?.y}")
            }

            override fun onChartSingleTapped(me: MotionEvent?) {
                Log.d(TAG, "onChartSingleTapped: x=${me?.x}, y=${me?.y}")
            }

            override fun onChartFling(me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float) {
                Log.d(TAG, "onChartFling: velocityX=$velocityX, velocityY=$velocityY")
            }

            override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
                Log.d(TAG, "onChartScale: scaleX=$scaleX, scaleY=$scaleY")
            }

            override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                Log.d(TAG, "onChartTranslate: dX=$dX, dY=$dY")
            }
        })

        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    Log.d(TAG, "onValueSelected: x=${e.x}, y=${e.y}")
                    showPopup(e.x, e.y)
                } else {
                    Log.e(TAG, "onValueSelected: Entry is null. Highlight: $h")
                }
            }


            override fun onNothingSelected() {
                Log.d(TAG, "onNothingSelected")
            }
        })

        //--------------------------------------------------------------------------------------------------
    }

    private suspend fun fetchMarketPriceData(apiService: BlockchainApiService, timespan: String): MarketPriceResponse {
        return apiService.getMarketPriceData(timespan)
    }

    private fun showPopup(x: Float, y: Float) {
        val entry = lineChart.getEntryByTouchPoint(x, y)
        Log.d(TAG, "showPopup: x=$x, y=$y, Entry: $entry")
        if (entry != null) {
            val value = entry.y.toInt().toString()
            popupView.findViewById<TextView>(R.id.tvPopup).text = "Value: $value"

            // Show the popup above the point
            popupWindow.showAtLocation(lineChart, Gravity.TOP or Gravity.START, x.toInt(), y.toInt())
        }
    }

    private fun displayLineChart(data: List<MarketPriceData>) {
        lineChart.setBackgroundColor(Color.TRANSPARENT)
        val description = Description()
        description.text = ""
        lineChart.description = description
        lineChart.axisRight.setDrawLabels(false)

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineWidth = 0f
        xAxis.axisLineColor = Color.WHITE
        xAxis.removeAllLimitLines()
        xAxis.textColor = Color.TRANSPARENT
        xAxis.setDrawGridLines(false)
        xAxis.spaceMin = .2f
        xAxis.setDrawAxisLine(false)

        val yAxis = lineChart.axisLeft
        yAxis.axisLineWidth = 0f
        yAxis.axisLineColor = Color.WHITE
        yAxis.removeAllLimitLines()
        yAxis.textColor = Color.TRANSPARENT
        yAxis.setDrawGridLines(false)
        yAxis.setDrawAxisLine(false)
        yAxis.setDrawLabels(false)

        val entries = data.map { Entry(it.x.toFloat(), it.y) }

        // gradient for graphs
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#582ECB"), Color.parseColor("#00000000"))
        )
        val gradientDrawable2 = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(Color.parseColor("#C661E4"), Color.parseColor("#00000000"))
        )

        val dataSet = LineDataSet(entries, "Market Price")
        dataSet.color = Color.rgb(88, 46, 203)
        dataSet.valueTextColor = Color.WHITE
//        dataSet.setCircleColor(Color.rgb(88, 46, 203))
//        dataSet.setCircleRadius(1f)
        dataSet.setDrawCircles(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.2f
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = gradientDrawable
        dataSet.setDrawValues(true)//check for later(popup)
        val lineData = LineData(dataSet)
        val legend = lineChart.legend
        legend.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.setDrawMarkers(false)
        lineChart.setPinchZoom(false)
        lineChart.isDragYEnabled = false
        lineChart.data = lineData
        lineChart.invalidate()
    }


    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
    //LOCAL_CFLAGS += -DWITH_HWLOC=OFF
    //set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DWITH_HWLOC=OFF")

    //10th line: ./xmrig -o (pool) -a randomx -u (wallet) -p (user)

    
}