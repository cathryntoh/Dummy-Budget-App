package com.cs407.dummybudget

import android.content.ClipData
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class QuestionnaireActivity : AppCompatActivity() {

    private val initialPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val initialLayoutParams = mutableMapOf<View, ViewGroup.LayoutParams>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questionnaire_screen)

        val draggableBlocks = arrayOf(
            findViewById<View>(R.id.red_box),
            findViewById<View>(R.id.green_box),
            findViewById<View>(R.id.purple_box),
            findViewById<View>(R.id.violet_box),
            findViewById<View>(R.id.beige_box),
            findViewById<View>(R.id.blue_box),
            findViewById<View>(R.id.pink_box)
        )

        val whiteBoxes = arrayOf(
            findViewById<FrameLayout>(R.id.rectangle_25),
            findViewById<FrameLayout>(R.id.rectangle_26),
            findViewById<FrameLayout>(R.id.rectangle_27),
            findViewById<FrameLayout>(R.id.rectangle_28),
            findViewById<FrameLayout>(R.id.rectangle_29),
            findViewById<FrameLayout>(R.id.rectangle_36),
            findViewById<FrameLayout>(R.id.rectangle_43)
        )

        for (block in draggableBlocks) {
            initialPositions[block] = Pair(block.x, block.y)
            initialLayoutParams[block] = block.layoutParams
            setDragListeners(block)
        }

        for (box in whiteBoxes) {
            setDropListeners(box)
        }

        val submitButton = findViewById<Button>(R.id.button)
        submitButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setDragListeners(view: View) {
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v.startDragAndDrop(data, shadowBuilder, v, 0)
                true
            } else {
                false
            }
        }
    }

    private fun setDropListeners(view: FrameLayout) {
        view.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.setBackgroundColor(Color.LTGRAY)
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.setBackgroundColor(Color.WHITE)
                }
                DragEvent.ACTION_DROP -> {
                    try {
                        val draggedView = event.localState as View
                        val parent = draggedView.parent as? ViewGroup
                        parent?.removeView(draggedView)
                        val dropTarget = v as FrameLayout
                        if (dropTarget.childCount > 0) {
                            val existingBlock = dropTarget.getChildAt(0)
                            dropTarget.removeView(existingBlock)
                            resetBlockPosition(existingBlock)
                        }
                        dropTarget.addView(draggedView)
                        val params = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        draggedView.layoutParams = params
                        draggedView.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.setBackgroundColor(Color.WHITE)
                    if (!event.result) {
                        val draggedView = event.localState as? View
                        draggedView?.visibility = View.VISIBLE
                    }
                }
            }
            true
        }
    }

    private fun resetBlockPosition(block: View) {
        val initialPosition = initialPositions[block]
        val initialParams = initialLayoutParams[block]

        if (initialPosition != null && initialParams != null) {
            block.x = initialPosition.first
            block.y = initialPosition.second
            block.layoutParams = initialParams
        }

        val rootView = findViewById<ViewGroup>(android.R.id.content)
        if (block.parent != rootView) {
            (block.parent as? ViewGroup)?.removeView(block)
            rootView.addView(block)
        }
    }
}