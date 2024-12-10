package com.cs407.dummybudget

import android.content.ClipData
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class QuestionnaireActivity : AppCompatActivity() {

    // Map to store initial positions and layout parameters of draggable blocks
    private val initialPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val initialLayoutParams = mutableMapOf<View, ViewGroup.LayoutParams>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.questionnaire_screen)

        // Initialize draggable blocks
        val draggableBlocks = arrayOf(
            findViewById<View>(R.id.red_box),
            findViewById<View>(R.id.green_box),
            findViewById<View>(R.id.purple_box),
            findViewById<View>(R.id.violet_box),
            findViewById<View>(R.id.beige_box),
            findViewById<View>(R.id.blue_box),
            findViewById<View>(R.id.pink_box)
        )

        // Initialize white boxes
        val whiteBoxes = arrayOf(
            findViewById<FrameLayout>(R.id.rectangle_25),
            findViewById<FrameLayout>(R.id.rectangle_26),
            findViewById<FrameLayout>(R.id.rectangle_27),
            findViewById<FrameLayout>(R.id.rectangle_28),
            findViewById<FrameLayout>(R.id.rectangle_29),
            findViewById<FrameLayout>(R.id.rectangle_36),
            findViewById<FrameLayout>(R.id.rectangle_43)
        )

        // Store initial positions and layout parameters of draggable blocks
        for (block in draggableBlocks) {
            initialPositions[block] = Pair(block.x, block.y)
            initialLayoutParams[block] = block.layoutParams
            setDragListeners(block)
        }

        // Set drop listeners on all white boxes
        for (box in whiteBoxes) {
            setDropListeners(box)
        }
    }

    // Set drag listeners for draggable blocks
    private fun setDragListeners(view: View) {
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "") // Drag data
                val shadowBuilder = View.DragShadowBuilder(v) // Drag feedback
                v.startDragAndDrop(data, shadowBuilder, v, 0)
                true
            } else {
                false
            }
        }
    }

    // Set drop listeners for white boxes
    private fun setDropListeners(view: FrameLayout) {
        view.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Highlight the drop area
                    v.setBackgroundColor(Color.LTGRAY)
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Remove the highlight
                    v.setBackgroundColor(Color.WHITE)
                }
                DragEvent.ACTION_DROP -> {
                    try {
                        val draggedView = event.localState as View

                        // Remove the dragged view from its current parent
                        val parent = draggedView.parent as? ViewGroup
                        parent?.removeView(draggedView)

                        val dropTarget = v as FrameLayout

                        // Check if the target already contains a block
                        if (dropTarget.childCount > 0) {
                            val existingBlock = dropTarget.getChildAt(0)
                            dropTarget.removeView(existingBlock)

                            // Reset the replaced block to its initial position and size
                            resetBlockPosition(existingBlock)
                        }

                        // Add the dragged view to the drop target
                        dropTarget.addView(draggedView)

                        // Update the dragged view's layout parameters to match the white box
                        val params = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        draggedView.layoutParams = params

                        // Reset visibility
                        draggedView.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Reset the background color of the drop target
                    v.setBackgroundColor(Color.WHITE)

                    // Reset visibility if the drop was not successful
                    if (!event.result) {
                        val draggedView = event.localState as? View
                        draggedView?.visibility = View.VISIBLE
                    }
                }
            }
            true
        }
    }

    // Reset a block's position to its initial coordinates and size
    private fun resetBlockPosition(block: View) {
        val initialPosition = initialPositions[block]
        val initialParams = initialLayoutParams[block]

        if (initialPosition != null && initialParams != null) {
            // Reset the block's position
            block.x = initialPosition.first
            block.y = initialPosition.second

            // Reset the layout parameters to the original size
            block.layoutParams = initialParams
        }

        // Ensure the block is re-added to its parent layout
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        if (block.parent != rootView) {
            (block.parent as? ViewGroup)?.removeView(block)
            rootView.addView(block)
        }
    }
}