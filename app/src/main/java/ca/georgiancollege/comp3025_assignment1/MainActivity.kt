/*
File name: MainActivity.kt
Author's name: Samarpreet Singh
StudentID: 200510621
Date: 2024-02-03
App Description: A calculator app design (part 1) and functionality (part 2). The goal with this app is to build a fully functioning calculator in Kotlin.
Version History:
0.1 -> Project Initialized
0.2 -> resultTextView added and formatted
0.3 -> backspace button, divider added
0.4 -> Operator buttons added along with app styling
0.5 -> first row added
0.6 -> Button placement, styling complete
0.7 -> Styles added, decoupled hardcoded values
0.8 -> Landscape implementation started
0.9 -> All landscape buttons added
1.0 -> Landscape layout finalized, Design complete!
 */
package ca.georgiancollege.comp3025_assignment1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.georgiancollege.comp3025_assignment1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding // this main binding is a generated class inside our package check the import above
    private lateinit var calculator: Calculator // lateinit tells kotlin I will initialize it somewhere else but not here!!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calculator = Calculator(binding) // no new keyword in kotlin. This instantiates an object of type calculator
    }
}