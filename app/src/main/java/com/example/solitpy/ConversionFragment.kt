package com.example.solitpy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import java.text.DecimalFormat
import kotlin.math.pow


class ConversionFragment : Fragment() {
    private lateinit var inputSpinner: Spinner
    private lateinit var outputSpinner: Spinner
    private lateinit var inputField: EditText
    private lateinit var inputField2: EditText
    private lateinit var inputField3: EditText
    private lateinit var inputField4: EditText
    private lateinit var inputField5: EditText
    private lateinit var convertButton: AppCompatButton
    private lateinit var outputField: TextView
    private lateinit var yeka: TextView
    private val concentrationTypes = arrayOf(
        "Molarity",
        "Normality",
        "Mass Concentration",
        "Molality",
        "Mole Fraction",
        "Mass Fraction",
        "Mass Percent",
        "ppt",
        "ppm",
        "ppb",
//        "Molicity"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_conversion, container, false)

        inputSpinner = view.findViewById(R.id.inputSpinner)
        outputSpinner = view.findViewById(R.id.outputSpinner)
        inputField = view.findViewById(R.id.inputField)
        inputField2 = view.findViewById(R.id.inputField2)
        inputField3 = view.findViewById(R.id.inputField3)
        inputField4 = view.findViewById(R.id.inputField4)
        inputField5 = view.findViewById(R.id.inputField5)
        convertButton = view.findViewById(R.id.convertButton)
        outputField = view.findViewById(R.id.outputField)
        yeka = view.findViewById(R.id.yeka)

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, concentrationTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputSpinner.adapter = adapter
        outputSpinner.adapter = adapter

        // Set initial visibility
        updateInputFieldsVisibility()

        // Set listeners for spinner selections
        inputSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateInputFieldsVisibility()
                outputField.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        outputSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateInputFieldsVisibility()
                outputField.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        convertButton.setOnClickListener { convert() }
        outputField.setOnClickListener {
            copyToClipboard(outputField.text.toString())
        }
        return view
    }

    private fun updateInputFieldsVisibility() {
        val inputUnit = inputSpinner.selectedItem.toString()
        val outputUnit = outputSpinner.selectedItem.toString()

        // Logic to update visibility of input fields based on selected units
        // (Add your existing logic here)
        when (outputUnit) {
            "Molarity" -> yeka.text = "Molarity (mol/L): "
            "Normality" -> yeka.text = "Normality (mol/L): "
            "Mass Concentration" -> yeka.text = "Mass Concentration (g/cm^3): "
            "Molality" -> yeka.text = "Molality (mol/kg): "
            "Mole Fraction" -> yeka.text = "Mole Fraction (Dimensionless): "
            "Mass Fraction" -> yeka.text = "Mass Fraction (Dimensionless): "
            "Mass Percent" -> yeka.text = "Mass Percent (Dimensionless): "
            "ppt" -> yeka.text = "ppt (Dimensionless): "
            "ppm" -> yeka.text = "ppm (Dimensionless): "
            "ppb" -> yeka.text = "ppb (Dimensionless): "
            "Molicity" -> yeka.text = "Molicity (mol/kg): "
        }


        inputField.filters = arrayOf(DecimalInputFilter())
        inputField2.filters = arrayOf(DecimalInputFilter())
        inputField3.filters = arrayOf(DecimalInputFilter())
        inputField4.filters = arrayOf(DecimalInputFilter())
        inputField5.filters = arrayOf(DecimalInputFilter())

        if (inputUnit == outputUnit) {
            // Hide additional input fields
            inputField.hint = "$outputUnit"
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()


        } else if (inputUnit == "Molarity" && outputUnit == "Normality") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.GONE
            inputField4.visibility = View.GONE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Capacity"
            inputField.text.clear()
            inputField2.text.clear()

        } else if (inputUnit == "Molarity" && outputUnit == "Mass Concentration") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.GONE
            inputField4.visibility = View.GONE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.text.clear()
            inputField2.text.clear()

        } else if (inputUnit == "Molarity" && outputUnit == "Molality") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.GONE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()

        } else if (inputUnit == "Molarity" && outputUnit == "Mole Fraction") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Density of Solution in g/cm^3"
            inputField4.hint = "Molar Mass of Solvent in g/mol"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


        } else if (inputUnit == "Molarity" && outputUnit == "Mass Fraction") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()

        } else if (inputUnit == "Molarity" && outputUnit == "Mass Percent") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()

        } else if (inputUnit == "Molarity" && outputUnit == "ppt") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField.text.clear()
            inputField2.visibility = View.VISIBLE
            inputField2.text.clear()
            inputField3.visibility = View.VISIBLE
            inputField3.text.clear()
            inputField4.visibility = View.INVISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"

        } else if (inputUnit == "Molarity" && outputUnit == "ppm") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField.text.clear()
            inputField2.visibility = View.VISIBLE
            inputField2.text.clear()
            inputField3.visibility = View.VISIBLE
            inputField3.text.clear()
            inputField4.visibility = View.INVISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"

        } else if (inputUnit == "Molarity" && outputUnit == "ppb") {
            inputField5.visibility = View.INVISIBLE

            inputField.visibility = View.VISIBLE

            inputField.text.clear()
            inputField2.visibility = View.VISIBLE
            inputField2.text.clear()
            inputField3.visibility = View.VISIBLE
            inputField3.text.clear()
            inputField4.visibility = View.INVISIBLE
            inputField.hint = "Molarity (mol/L)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"
        } else if (inputUnit == "Molarity" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE

        } else if (inputUnit == "Normality" && outputUnit == "Molarity") {
            inputField5.visibility = View.INVISIBLE


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Mass Concentration") {
            inputField5.visibility = View.INVISIBLE


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Molality") {
            inputField5.visibility = View.INVISIBLE


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Mole Fraction") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.VISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField5.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Mass Fraction") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Mass Percent") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "ppt") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "ppm") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "ppb") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Normality (mol/L)"
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
        } else if (inputUnit == "Normality" && outputUnit == "Molicity") {


            inputField5.visibility = View.INVISIBLE


            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else if (inputUnit == "Mass Concentration" && outputUnit == "Molarity") {


            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField.hint = "Mass Concentration (g/cm^3)"
            inputField2.hint = "Molar Mass of Solute in g/mol"
        } else if (inputUnit == "Mass Concentration" && outputUnit == "Normality") {

            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField.hint = "Mass Concentration (g/cm^3)"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Capacity"

        } else if (inputUnit == "Mass Concentration" && outputUnit == "Molality") {

            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField.hint = "Mass Concentration (g/cm^3)"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solute in g/mol"
        } else if (inputUnit == "Mass Concentration" && outputUnit == "Mole Fraction") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()

            inputField4.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mass Concentration (g/cm^3)"

        } else if (inputUnit == "Mass Concentration" && outputUnit == "Mass Fraction") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Concentration (g/cm^3)"

        } else if (inputUnit == "Mass Concentration" && outputUnit == "Mass Percent") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Concentration (g/cm^3)"
        } else if (inputUnit == "Mass Concentration" && outputUnit == "ppt") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Concentration (g/cm^3)"
        } else if (inputUnit == "Mass Concentration" && outputUnit == "ppm") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Concentration (g/cm^3)"
        } else if (inputUnit == "Mass Concentration" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Concentration (g/cm^3)"


        } else if (inputUnit == "Mass Concentration" && outputUnit == "Molicity") {


            inputField5.visibility = View.INVISIBLE


            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else if (inputUnit == "Molality" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Molality (mol/kg)"

        } else if (inputUnit == "Molality" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "Mass Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "Mass Percent") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "ppt") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "ppm") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Molality (mol/kg)"


        } else if (inputUnit == "Molality" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE


        } else if (inputUnit == "Mole Fraction" && outputUnit == "Molarity") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()

            inputField4.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "Normality") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.VISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField5.text.clear()

            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField5.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"
        } else if (inputUnit == "Mole Fraction" && outputUnit == "Mass Concentration") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()

            inputField4.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "Molality") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"


        } else if (inputUnit == "Mole Fraction" && outputUnit == "Mass Fraction") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"


        } else if (inputUnit == "Mole Fraction" && outputUnit == "Mass Percent") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "ppt") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "ppm") {


            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "ppb") {

            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mole Fraction (Dimensionless!)"

        } else if (inputUnit == "Mole Fraction" && outputUnit == "Molicity") {

            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()

            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Molality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Mass Percent") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "ppt") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "ppm") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Fraction (Dimensionless!)"

        } else if (inputUnit == "Mass Fraction" && outputUnit == "Molicity") {

            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE

        } else if (inputUnit == "Mass Percent" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()

            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Molality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()

            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Mass Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "ppt") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "ppm") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "Mass Percent (Dimensionless!)"
        } else if (inputUnit == "Mass Percent" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else if (inputUnit == "ppt" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Molality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Mass Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Mass Percent") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "ppm") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppt (Dimensionless!)"
        } else if (inputUnit == "ppt" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else if (inputUnit == "ppm" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Molality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField3.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Mass Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Mass Percent") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "ppt") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "ppb") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppm (Dimensionless!)"
        } else if (inputUnit == "ppm" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else if (inputUnit == "ppb" && outputUnit == "Molarity") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Normality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.VISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField4.text.clear()
            inputField2.hint = "Capacity"
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField4.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Mass Concentration") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Density of Solution in g/cm^3"
            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Molality") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()


            inputField2.hint = "Molar Mass of Solute in g/mol"
            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Mole Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.VISIBLE
            inputField3.visibility = View.VISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()
            inputField2.text.clear()
            inputField3.text.clear()
            inputField3.hint = "Molar Mass of Solute in g/mol"
            inputField2.hint = "Molar Mass of Solvent in g/mol"
            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Mass Fraction") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Mass Percent") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "ppt") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "ppm") {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE
            inputField.text.clear()

            inputField.hint = "ppb (Dimensionless!)"
        } else if (inputUnit == "ppb" && outputUnit == "Molicity") {
            inputField5.visibility = View.INVISIBLE
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        } else {
            // Show additional input fields
            inputField.visibility = View.INVISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
        }
    }

    private fun convert() {
        val inputValue = convertPersianToStandard(inputField.text.toString()).toDoubleOrNull()
        val inputValue2 = convertPersianToStandard(inputField2.text.toString()).toDoubleOrNull()
        val inputValue3 = convertPersianToStandard(inputField3.text.toString()).toDoubleOrNull()
        val inputValue4 = convertPersianToStandard(inputField4.text.toString()).toDoubleOrNull()
        val inputValue5 = convertPersianToStandard(inputField5.text.toString()).toDoubleOrNull()

        if (inputField.visibility == View.VISIBLE && inputValue == null) {
            inputField.requestFocus() // تمرکز بر روی inputField
            inputField.error = "Please Enter Correct Value!" // نمایش پیام خطا
            return
        }

        if (inputField2.visibility == View.VISIBLE && inputValue2 == null) {
            inputField2.requestFocus() // تمرکز بر روی inputField2
            inputField2.error = "Please Enter Correct Value!" // نمایش پیام خطا
            return
        }

        if (inputField3.visibility == View.VISIBLE && inputValue3 == null) {
            inputField3.requestFocus() // تمرکز بر روی inputField3
            inputField3.error = "Please Enter Correct Value!" // نمایش پیام خطا
            return
        }

        if (inputField4.visibility == View.VISIBLE && inputValue4 == null) {
            inputField4.requestFocus() // تمرکز بر روی inputField4
            inputField4.error = "Please Enter Correct Value!" // نمایش پیام خطا
            return
        }

        if (inputField5.visibility == View.VISIBLE && inputValue5 == null) {
            inputField5.requestFocus() // تمرکز بر روی inputField5
            inputField5.error = "Please Enter Correct Value!" // نمایش پیام خطا
            return
        }

        if (inputValue == null) {
            outputField.text = "Please enter a valid number."
            return
        }

        val inputUnit = inputSpinner.selectedItem.toString()
        val outputUnit = outputSpinner.selectedItem.toString()

        // Check if input unit is the same as output unit
        if (inputUnit == outputUnit) {
            inputField.visibility = View.VISIBLE
            inputField2.visibility = View.INVISIBLE
            inputField3.visibility = View.INVISIBLE
            inputField4.visibility = View.INVISIBLE
            inputField5.visibility = View.INVISIBLE


            inputField.hint = "$inputUnit"

            outputField.text = "$inputValue" // Return the same value
            return
        }

        val result = when (inputUnit to outputUnit) {
            "Molarity" to "Normality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue * (inputValue2)
            }


            "Molarity" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue * (inputValue2) / 1000

            }

            "Molarity" to "Molality" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * ((1000 * inputValue2 / inputValue) - inputValue3).pow(-1)
            }

            "Molarity" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1 + ((1000 * inputValue3) / (inputValue * inputValue4)) - (inputValue2 / inputValue4)).pow(
                    -1
                )

            }

            "Molarity" to "Mass Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue * inputValue3 / (1000 * inputValue2)
            }

            "Molarity" to "Mass Percent" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                100 * inputValue * inputValue3 / (1000 * inputValue2)
            }

            "Molarity" to "ppt" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue * inputValue3 / (1000 * inputValue2)
            }

            "Molarity" to "ppm" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000 * inputValue * inputValue3 / (1000 * inputValue2)
            }

            "Molarity" to "ppb" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000000 * inputValue * inputValue3 / (1000 * inputValue2)

            }

            "Molarity" to "molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."

            }

            "Normality" to "Molarity" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue / inputValue2

            }

            "Normality" to "Mass Concentration" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue3 * inputValue / (1000 * inputValue2)

            }

            "Normality" to "Molality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * ((1000 * inputValue2 * inputValue4 / inputValue) - inputValue3).pow(-1)
            }

            "Normality" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null || inputValue5 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1 + ((1000 * inputValue2 * inputValue4) / (inputValue * inputValue5)) - (inputValue3 / inputValue5)).pow(
                    -1
                )

            }


            "Normality" to "Mass Fraction" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue * inputValue3 / (1000 * inputValue2 * inputValue4)
            }

            "Normality" to "Mass Percent" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                100 * inputValue * inputValue3 / (1000 * inputValue2 * inputValue4)
            }

            "Normality" to "ppt" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue * inputValue3 / (1000 * inputValue2 * inputValue4)

            }

            "Normality" to "ppm" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }

                1000000 * inputValue * inputValue3 / (1000 * inputValue2 * inputValue4)

            }

            "Normality" to "ppb" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000000 * inputValue * inputValue3 / (1000 * inputValue2 * inputValue4)
            }

            "Normality" to "molicity" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }


            "Mass Concentration" to "Molarity" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }


                1000 * inputValue / inputValue2
            }

            "Mass Concentration" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * inputValue / inputValue3

            }

            "Mass Concentration" to "Molality" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue3 / 1000) * ((inputValue2 / inputValue) - 1)).pow(-1)
            }

            "Mass Concentration" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1 + (inputValue4 / inputValue3) * ((inputValue2 / inputValue) - 1)).pow(-1)
            }

            "Mass Concentration" to "Mass Fraction" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue / inputValue2

            }

            "Mass Concentration" to "Mass Percent" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                100 * inputValue / inputValue2

            }


            "Mass Concentration" to "ppt" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue / inputValue2
            }

            "Mass Concentration" to "ppm" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000 * inputValue / inputValue2
            }

            "Mass Concentration" to "ppb" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000000 * inputValue / inputValue2
            }

            "Mass Concentration" to "molicity" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

            "Molality" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 / (inputValue3 + 1000 * inputValue.pow(-1))
            }

            "Molality" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1000 * inputValue2 * inputValue4) / (inputValue3 + 1000 * inputValue.pow(-1))
            }


            "Molality" to "Mass Concentration" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue2 / (1 + (inputValue3 * inputValue / 1000).pow(-1))
            }

            "Molality" to "Mole Fraction" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "Mass Fraction" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "Mass Percent" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                100 * (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "ppt" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "ppm" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000 * (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "ppb" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000000 * (1 + (inputValue2 * inputValue / 1000).pow(-1)).pow(-1)
            }

            "Molality" to "Molicity" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

            "Mole Fraction" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1000 * inputValue2) / (inputValue3 * ((1 / inputValue) + (inputValue4 / inputValue3) - 1))
            }

            "Mole Fraction" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null || inputValue5 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                (1000 * inputValue2 * inputValue4) / (inputValue5 * ((1 / inputValue) + (inputValue3 / inputValue5) - 1))
            }

            "Mole Fraction" to "Mass Concentration" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue2 * (1 + (inputValue3 / inputValue4) * ((1 / inputValue) - 1)).pow(-1)
            }

            "Mole Fraction" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1 / inputValue) - 1)).pow(-1)
            }

            "Mole Fraction" to "Mass Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }

                ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(-1)


            }

            "Mole Fraction" to "Mass Percent" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return

                }
                100 * ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )

            }

            "Mole Fraction" to "ppt" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )
            }

            "Mole Fraction" to "ppm" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000 * ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )
            }

            "Mole Fraction" to "ppb" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000000000 * ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )
            }

            "Mole Fraction" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

            "Mass Fraction" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue * inputValue2 / inputValue3
            }

            "Mass Fraction" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * inputValue * inputValue4 / inputValue3
            }

            "Mass Fraction" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                inputValue * inputValue2
            }

            "Mass Fraction" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1 / inputValue) - 1)).pow(-1)
            }

            "Mass Fraction" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue3 / (inputValue2 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(-1)
            }


            "Mass Fraction" to "Mass Percent" -> {

                100 * inputValue
            }

            "Mass Fraction" to "ppt" -> {

                1000 * inputValue
            }

            "Mass Fraction" to "ppm" -> {

                1000000 * inputValue
            }

            "Mass Fraction" to "ppb" -> {

                1000000000 * inputValue
            }

            "Mass Fraction" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

            "Mass Percent" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * 0.01 * inputValue * inputValue2 / inputValue3
            }

            "Mass Percent" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * 0.01 * inputValue * inputValue4 / inputValue3
            }

            "Mass Percent" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                0.01 * inputValue * inputValue2
            }

            "Mass Percent" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((100 / inputValue) - 1)).pow(-1)
            }

            "Mass Percent" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue3 / (inputValue2 * 0.01 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )
            }

            "Mass Percent" to "Mass Fraction" -> {

                0.01 * inputValue
            }

            "Mass Percent" to "ppt" -> {

                0.01 * inputValue * 1000
            }

            "Mass Percent" to "ppm" -> {

                0.01 * inputValue * 1000000
            }

            "Mass Percent" to "ppb" -> {

                0.01 * inputValue * 1000000000
            }

            "Mass Percent" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

            "ppt" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * 0.001 * inputValue * inputValue2 / inputValue3
            }

            "ppt" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * 0.001 * inputValue * inputValue4 / inputValue3
            }

            "ppt" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                0.001 * inputValue * inputValue2

            }

            "ppt" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1000 / inputValue) - 1)).pow(-1)
            }

            "ppt" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / (inputValue3 * 0.001 * inputValue)) + 1 - (inputValue2 / inputValue3)).pow(
                    -1
                )

            }

            "ppt" to "Mass Fraction" -> {

                0.001 * inputValue
            }

            "ppt" to "Mass Percent" -> {

                0.001 * inputValue * 100
            }

            "ppt" to "ppm" -> {

                0.001 * inputValue * 1000000
            }

            "ppt" to "ppb" -> {

                0.001 * inputValue * 1000000000
            }

            "ppt" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }


            "ppm" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * 0.000001 * inputValue * inputValue2 / inputValue3
            }

            "ppm" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * 0.000001 * inputValue * inputValue4 / inputValue3
            }

            "ppm" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                0.000001 * inputValue * inputValue2


            }

            "ppm" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1000000 / inputValue) - 1)).pow(-1)
            }

            "ppm" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1000000 / inputValue) - 1)).pow(-1)
                ((inputValue2 / (inputValue3 * 0.000001 * inputValue)) + 1 - (inputValue2 / inputValue3)).pow(
                    -1
                )
            }

            "ppm" to "Mass Fraction" -> {

                0.000001 * inputValue
            }

            "ppm" to "Mass Percent" -> {

                0.000001 * inputValue * 100
            }

            "ppm" to "ppt" -> {

                0.000001 * inputValue * 1000
            }

            "ppm" to "ppb" -> {

                0.000001 * inputValue * 1000000000
            }

            "ppm" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }

//            /TODO


            "ppb" to "Molarity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * 0.000000001 * inputValue * inputValue2 / inputValue3
            }

            "ppb" to "Normality" -> {
                if (inputValue2 == null || inputValue3 == null || inputValue4 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                1000 * inputValue2 * 0.000000001 * inputValue * inputValue4 / inputValue3
            }

            "ppb" to "Mass Concentration" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                0.000000001 * inputValue * inputValue2


            }

            "ppb" to "Molality" -> {
                if (inputValue2 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue2 / 1000) * ((1000000000 / inputValue) - 1)).pow(-1)
            }

            "ppb" to "Mole Fraction" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                ((inputValue3 / (inputValue2 * 0.000000001 * inputValue)) + 1 - (inputValue3 / inputValue2)).pow(
                    -1
                )
            }

            "ppb" to "Mass Fraction" -> {

                0.000000001 * inputValue
            }

            "ppb" to "Mass Percent" -> {

                0.000000001 * inputValue * 100
            }

            "ppb" to "ppt" -> {

                0.000000001 * inputValue * 1000
            }

            "ppb" to "ppm" -> {

                0.000000001 * inputValue * 1000000
            }

            "ppb" to "Molicity" -> {
                if (inputValue2 == null || inputValue3 == null) {
                    outputField.text = "Conversion not supported."
                    return
                }
                outputField.text = "Conversion not supported."
            }


            // Add more conversion formulas as needed...
            else -> null
        }

        // Update the output field with the result
        if (result != null) {
            // یا از نوع BigDecimal نیز می‌توانید استفاده کنید

            // استفاده از DecimalFormat برای جدا کردن هر 3 رقم

            val decimalFormat = DecimalFormat("#.###############################") // فرمت دلخواه
            decimalFormat.isGroupingUsed = false // غیرفعال کردن گروه‌بندی (جداکننده‌های هزارگان)
            val formattedNumber = decimalFormat.format(result)


            outputField.text = "$formattedNumber"


        } else {
            outputField.text = "Conversion not supported."
        }

    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        // نمایش پیام "کپی شد"
        Toast.makeText(requireContext(), "The Result Was Copied", Toast.LENGTH_SHORT).show()
    }

    private fun convertPersianToStandard(persianNumber: String): String {
        val persianDigits = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val standardDigits = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

        var standardNumber = persianNumber
        for (i in persianDigits.indices) {
            standardNumber = standardNumber.replace(persianDigits[i], standardDigits[i])
        }
        return standardNumber
    }

}