package pt.joaomneto.titancompanion.adventure

import androidx.fragment.app.DialogFragment
import android.view.View
import android.widget.Button
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1

abstract class AdventureFragment : DialogFragment() {

    var baseLayout = -1

    abstract fun refreshScreensFromResume()

    override fun onResume() {
        super.onResume()
        refreshScreensFromResume()
    }

    fun setupIncDecButton(
        rootView: View,
        incButtonId: Int,
        decButtonId: Int,
        property: KMutableProperty0<Int>,
        maxValue: Int
    ) {
        val incButton = rootView.findViewById<Button>(incButtonId)
        val decButton = rootView.findViewById<Button>(decButtonId)

        incButton.setOnClickListener {
            incDec(property, maxValue, true)
            refreshScreensFromResume()
        }

        decButton.setOnClickListener {
            incDec(property, maxValue, false)
            refreshScreensFromResume()
        }
    }

    fun <A : Adventure> setupIncDecButton(
        rootView: View, incButtonId: Int, decButtonId: Int, adv: A, property: KMutableProperty1<A, Int>,
        maxValue: Int, incTrigger: Runnable? = null, decTrigger: Runnable? = null
    ) {

        val incButton = rootView.findViewById<Button>(incButtonId)
        val decButton = rootView.findViewById<Button>(decButtonId)

        incButton.setOnClickListener {
            incDec(adv, property, maxValue, true)
            incTrigger?.run()
            refreshScreensFromResume()
        }

        decButton.setOnClickListener {
            incDec(adv, property, maxValue, false)
            decTrigger?.run()
            refreshScreensFromResume()
        }
    }

    private fun <A : Adventure> incDec(adv: A, property: KMutableProperty1<A, Int>, maxValue: Int, increase: Boolean) {
        if (increase)
            property.set(adv, Math.min(property.get(adv) + 1, maxValue))
        else
            property.set(adv, Math.min(property.get(adv) - 1, 0))
    }

    private fun incDec(property: KMutableProperty0<Int>, maxValue: Int, increase: Boolean) {
        if (increase)
            property.set(Math.min(property.get() + 1, maxValue))
        else
            property.set(Math.min(property.get() - 1, 0))
    }
}
