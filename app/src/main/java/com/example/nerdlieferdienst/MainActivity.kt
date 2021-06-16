package com.example.nerdlieferdienst

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity(){

    private var pizzaIngredientDataModel: ArrayList<PizzaIngredientDataModel>? = null
    private lateinit var pizzaIngredientAdapter: CustomAdapter
    private lateinit var garnishRadioButtonGroup: RadioGroup
    private lateinit var pizzaOrder: PizzaOrderModel
    private lateinit var noodleOrder: NoodleOrderModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pizzaSelectButton = findViewById<Button>(R.id.pizzaButton)
        val noodlesSelectButton = findViewById<Button>(R.id.noodlesButton)
        val shoppingBasketButton = findViewById<Button>(R.id.goToBasketButton)


        pizzaSelectButton.setOnClickListener {
            showPizzaDialog()
        }

        noodlesSelectButton.setOnClickListener {
            showNoodleDialog()
        }

        // Set Shoppingcart-Button on disabled as default, when there is nothing in it
        shoppingBasketButton.isEnabled = false
        shoppingBasketButton.isClickable = false

    }


    private fun showPizzaDialog(){

        val pizzaDialogView = LayoutInflater.from(this).inflate(R.layout.pizza_alertdialog,null)
        val pizzaDialogBuilder = AlertDialog.Builder(this)
        pizzaDialogBuilder.setView(pizzaDialogView)

        val pizzaDropdown = pizzaDialogView.findViewById<Spinner>(R.id.pizzaSelection)
        val pizzaIngredientsListView = pizzaDialogView.findViewById<ListView>(R.id.ingredientList)
        val pizzaConstraintLayout = pizzaDialogView.findViewById<ConstraintLayout>(R.id.pizzaOptions)

        val addToCartButton = pizzaDialogView.findViewById<Button>(R.id.addPizzaToCartButton)
        val backButton = pizzaDialogView.findViewById<Button>(R.id.backButton)


        //Get Data for Pizzas from String resources
        val pizzaTypes = resources.getStringArray(R.array.pizzaTypes)
        val pizzaIngredients = resources.getStringArray(R.array.pizzaIngredients)
        val garnishItems = resources.getStringArray(R.array.garnish)



        //Put Pizzatypes in the dropdown list
        val pizzaDropdownAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, pizzaTypes)
        pizzaDropdown.adapter = pizzaDropdownAdapter



        //Populate list with choosable extra ingredients using a custom adapter for enabling usage of checkboxes
        pizzaIngredientDataModel = ArrayList<PizzaIngredientDataModel>()

        for (ingredient in pizzaIngredients){
            pizzaIngredientDataModel!!.add(PizzaIngredientDataModel(ingredient, false))
        }
        pizzaIngredientAdapter = CustomAdapter(pizzaIngredientDataModel!!, applicationContext)

        pizzaIngredientsListView.adapter = pizzaIngredientAdapter

        pizzaIngredientsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val dataModel: PizzaIngredientDataModel = pizzaIngredientDataModel!![position] as PizzaIngredientDataModel
                dataModel.checked = !dataModel.checked
                pizzaIngredientAdapter.notifyDataSetChanged()
            }



        garnishRadioButtonGroup = pizzaDialogView.findViewById(R.id.garnishRadioGroup)

        //generate a RadioButton for every garnish in the list
        for (garnish in garnishItems){

            val garnishRadioButton = RadioButton(applicationContext)
            garnishRadioButton.setText(garnish)
            garnishRadioButton.textSize = 16F

            //add RadioButton to the RadioGroup
            garnishRadioButtonGroup.addView(garnishRadioButton)
        }


        //Listener for pizza selection, changes visibility of options depending on the selection in the dropdown-menu
        pizzaDropdown.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                if(position == 0){
                    pizzaConstraintLayout.visibility = View.GONE
                    addToCartButton.isEnabled = false
                }
                else{
                    pizzaConstraintLayout.visibility = View.VISIBLE
                    addToCartButton.isEnabled = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        addToCartButton.setOnClickListener {
            if(garnishRadioButtonGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Bitte eine Beilage auswählen", Toast.LENGTH_SHORT).show()
            }
            else {

                // Placeholder for function to add pizza to shopping cart
                /*
                createPizzaOrder(
                    pizzaDropdown.selectedItem.toString(),
                    arrayOf("array1", "array2"), garnishRadioButtonGroup.checkedRadioButtonId, 2
                )
                 */
            }
        }

        pizzaDialogBuilder.show()

    }

    private fun showNoodleDialog(){


        val noodleDialogView = LayoutInflater.from(this).inflate(R.layout.noodle_alertdialog,null)
        val noodleDialogBuilder = AlertDialog.Builder(this)
        noodleDialogBuilder.setView(noodleDialogView)

        val noodleDropdown = noodleDialogView.findViewById<Spinner>(R.id.noodleSelection)
        var noodleSauceRadioGroup = noodleDialogView.findViewById<RadioGroup>(R.id.SauceRadioGroup)



        //Get Data for noodles from String resources
        val noodleTypes = resources.getStringArray(R.array.noodleTypes)
        val noodleSauceTypes = resources.getStringArray(R.array.noodleSauces)
        val garnishItems = resources.getStringArray(R.array.garnish)


        val addToCartButton = noodleDialogView.findViewById<Button>(R.id.addNoodlesToCartButton)


        val noodleDropdownAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, noodleTypes)
        noodleDropdown.adapter = noodleDropdownAdapter



        noodleSauceRadioGroup = noodleDialogView.findViewById(R.id.SauceRadioGroup)

        for(sauce in noodleSauceTypes){
            val sauceRadioButton = RadioButton(applicationContext)
            sauceRadioButton.setText(sauce)
            sauceRadioButton.textSize = 16F     //set Text size, so it's the same size as every other text

            //add RadioButton to the RadioGroup
            noodleSauceRadioGroup.addView(sauceRadioButton)
        }


        garnishRadioButtonGroup = noodleDialogView.findViewById(R.id.garnishRadioGroup)

        //generate a RadioButton for every garnish in the list
        for (garnish in garnishItems){

            val garnishRadioButton = RadioButton(applicationContext)
            garnishRadioButton.setText(garnish)
            garnishRadioButton.textSize = 16F       //set Text size, so it's the same size as every other text

            //add RadioButton to the RadioGroup
            garnishRadioButtonGroup.addView(garnishRadioButton)
        }

        noodleDialogBuilder.show()

    }

    //Saves the order to the custom Data model to pass it further to the creation of the recipe (to save it in some kind of database for the shopping cart)
    private fun createPizzaOrder(pizzaName: String, chosenIngredients: Array<String>, chosenGarnish: Int, desiredQuantity: Int){

        pizzaOrder = PizzaOrderModel("", arrayOf(""),0, 0)
        pizzaOrder.pizza = pizzaName
        pizzaOrder.ingredients = chosenIngredients
        pizzaOrder.garnish = chosenGarnish
        pizzaOrder.quantity = desiredQuantity

    }
}
