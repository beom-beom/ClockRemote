package com.example.clockremote

import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var irManager: ConsumerIrManager? = null

    // 일반적인 LED 벽시계 NEC 코드값
    private val HEX_SET = 0x00FF02FD
    private val HEX_PLUS = 0x00FF9867
    private val HEX_MINUS = 0x00FFE21D
    private val HEX_OK = 0x00FF38C7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 안전하게 IR 매니저 초기화
        irManager = getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager

        if (irManager == null) {
            Toast.makeText(this, "이 기기에는 IR 센서(적외선)가 없습니다.", Toast.LENGTH_LONG).show()
        }

        // 버튼 연결
        findViewById<Button>(R.id.btnSet)?.setOnClickListener { transmit(HEX_SET) }
        findViewById<Button>(R.id.btnOk)?.setOnClickListener { transmit(HEX_OK) }
        findViewById<Button>(R.id.btnPlus)?.setOnClickListener { transmit(HEX_PLUS) }
        findViewById<Button>(R.id.btnMinus)?.setOnClickListener { transmit(HEX_MINUS) }
        findViewById<Button>(R.id.btnSync)?.setOnClickListener { syncTimeMacro() }
    }

    private fun transmit(hex: Int) {
        val manager = irManager
        if (manager == null || !manager.hasIrEmitter()) {
            // Toast는 메인 스레드에서만 안전하게 실행되도록 보장
            runOnUiThread {
                Toast.makeText(this, "IR 발신기를 사용할 수 없는 환경입니다.", Toast.LENGTH_SHORT).show()
            }
            return
        }

        try {
            val pattern = convertHexToPattern(hex.toLong())
            manager.transmit(38000, pattern)
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(this, "발신 중 오류 발생: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun syncTimeMacro() {
        val manager = irManager
        if (manager == null || !manager.hasIrEmitter()) {
            Toast.makeText(this, "IR 발신기를 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        thread {
            try {
                val now = Calendar.getInstance()
                val hour = now.get(Calendar.HOUR_OF_DAY)
                val min = now.get(Calendar.MINUTE)

                runOnUiThread {
                    Toast.makeText(this, "${hour}시 ${min}분으로 맞춤을 시작합니다.", Toast.LENGTH_SHORT).show()
                }

                // 1. 설정 모드 진입
                transmit(HEX_SET)
                Thread.sleep(1500)

                // 2. 시간 반복 입력 (hour만큼 Plus 버튼 클릭)
                repeat(hour) {
                    transmit(HEX_PLUS)
                    Thread.sleep(450)
                }

                // 3. 분 설정으로 이동
                transmit(HEX_SET)
                Thread.sleep(1000)

                // 4. 분 반복 입력 (min만큼 Plus 버튼 클릭)
                repeat(min) {
                    transmit(HEX_PLUS)
                    Thread.sleep(450)
                }

                // 5. 확인 및 종료
                transmit(HEX_OK)
                runOnUiThread {
                    Toast.makeText(this, "시간 설정이 완료되었습니다.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "매크로 실행 중 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun convertHexToPattern(hexCode: Long): IntArray {
        val result = mutableListOf<Int>()
        // NEC Protocol Leader: 9ms pulse + 4.5ms space
        result.add(9000); result.add(4500)
        
        for (i in 31 downTo 0) {
            val bit = (hexCode shr i) and 1
            result.add(560) // Pulse
            result.add(if (bit == 1L) 1690 else 560) // Bit 1 vs 0
        }
        result.add(560) // Stop bit
        return result.toIntArray()
    }
}