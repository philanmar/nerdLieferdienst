package com.example.nerdlieferdienst

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
    private lateinit var noodleGarnishItems: Array<String>
    private lateinit var pizzaGarnishItems: Array<String>
    private lateinit var noodleSauceTypes: Array<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pizzaSelectButton = findViewById<Button>(R.id.pizzaButton)
        val noodlesSelectButton = findViewById<Button>(R.id.noodlesButton)
        val shoppingBasketButton = findViewById<Button>(R.id.goToBasketButton)

        pizzaGarnishItems = resources.getStringArray(R.array.garnish)
        noodleGarnishItems = resources.getStringArray(R.array.garnish)
        noodleSauceTypes = resources.getStringArray(R.array.noodleSauces)



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

        // declaring elements for increasing or decreasing the quantity for that order
        val quantityDecreaseButton = pizzaDialogView.findViewById<Button>(R.id.decreaseQuantityButton)
        val quantityIncreaseButton = pizzaDialogView.findViewById<Button>(R.id.increaseQuantityButton)
        val quantityTextView = pizzaDialogView.findViewById<TextView>(R.id.quantityCounter)
        var orderQuantitiy: Int = 1


        //Get Data for Pizzas from String resources
        val pizzaTypes = resources.getStringArray(R.array.pizzaTypes)
        val pizzaIngredients = resources.getStringArray(R.array.pizzaIngredients)



        //Put Pizzatypes in the dropdown list
        val pizzaDropdownAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, pizzaTypes)
        pizzaDropdown.adapter = pizzaDropdownAdapter



        //Populate list with choosable extra ingredients using a custom adapter for enabling usage of checkboxes
        pizzaIngredientDataModel = ArrayList()

        for (ingredient in pizzaIngredients){
            pizzaIngredientDataModel!!.add(PizzaIngredientDataModel(ingredient, false))
        }
        pizzaIngredientAdapter = CustomAdapter(pizzaIngredientDataModel!!, applicationContext)

        pizzaIngredientsListView.adapter = pizzaIngredientAdapter

        pizzaIngredientsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val dataModel: PizzaIngredientDataModel = pizzaIngredientDataModel!![position]
                dataModel.checked = !dataModel.checked
                pizzaIngredientAdapter.notifyDataSetChanged()
            }


        garnishRadioButtonGroup = pizzaDialogView.findViewById(R.id.garnishRadioGroup)

        //generate a RadioButton for every garnish in the list
        for (garnish in pizzaGarnishItems){

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

                createPizzaOrder(
                    pizzaDropdown.selectedItem.toString(),
                    arrayOf("array1", "array2"), garnishRadioButtonGroup.checkedRadioButtonId, orderQuantitiy
                )
            }
        }


        //Decrease quantity for the order on click on the quantityDecreaseButton and change shown quantity
        quantityDecreaseButton.setOnClickListener {
            //first check if quantity is 1 or higher, only allow decreasing if quantity is at least 2
            if(orderQuantitiy > 1){
                orderQuantitiy--
                quantityTextView.setText(orderQuantitiy.toString())
            }
        }

        //Increase quantity for the order on click on the quantityDecreaseButton and change shown quantity
        quantityIncreaseButton.setOnClickListener{
            orderQuantitiy++
            quantityTextView.setText(orderQuantitiy.toString())
        }


        pizzaDialogBuilder.show()

    }

    private fun showNoodleDialog(){


        val noodleDialogView = LayoutInflater.from(this).inflate(R.layout.noodle_alertdialog,null)
        val noodleDialogBuilder = AlertDialog.Builder(this)
        noodleDialogBuilder.setView(noodleDialogView)

        val noodleDropdown = noodleDialogView.findViewById<Spinner>(R.id.noodleSelection)
        val noodleSauceRadioGroup: RadioGroup



        //Get Data for noodles from String resources
        val noodleTypes = resources.getStringArray(R.array.noodleTypes)

        val addToCartButton = noodleDialogView.findViewById<Button>(R.id.addNoodlesToCartButton)


        // declaring elements for increasing or decreasing the quantity for that order
        val quantityDecreaseButton = noodleDialogView.findViewById<Button>(R.id.decreaseQuantityButton)
        val quantityIncreaseButton = noodleDialogView.findViewById<Button>(R.id.increaseQuantityButton)
        val quantityTextView = noodleDialogView.findViewById<TextView>(R.id.quantityCounter)
        var orderQuantitiy: Int = 1


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


        garnishRadioButtonGroup = noodleDialogView.findViewById(R.id.noodleGarnishRadioGroup)

        //generate a RadioButton for every garnish in the list
        for (garnish in noodleGarnishItems){

            val garnishRadioButton = RadioButton(applicationContext)
            garnishRadioButton.setText(garnish)
            garnishRadioButton.textSize = 16F       //set Text size, so it's the same size as every other text

            //add RadioButton to the RadioGroup
            garnishRadioButtonGroup.addView(garnishRadioButton)
        }



        //Decrease quantity for the order on click on the quantityDecreaseButton and change shown quantity
        quantityDecreaseButton.setOnClickListener {
            //first check if quantity is 1 or higher, only allow decreasing if quantity is at least 2
            if(orderQuantitiy > 1){
                orderQuantitiy--
                quantityTextView.setText(orderQuantitiy.toString())
            }
        }

        //Increase quantity for the order on click on the quantityDecreaseButton and change shown quantity
        quantityIncreaseButton.setOnClickListener{
            orderQuantitiy++
            quantityTextView.setText(orderQuantitiy.toString())
        }

        // Checks if anything for completing the order is missing and shows a toast-message to show the user what is missing
        addToCartButton.setOnClickListener {
            if(noodleSauceRadioGroup.checkedRadioButtonId == -1 && garnishRadioButtonGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Bitte eine Soße und eine Beilage auswählen", Toast.LENGTH_SHORT).show()
            }else if(noodleSauceRadioGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Bitte eine Soße auswählen", Toast.LENGTH_SHORT).show()
            }
            else if(garnishRadioButtonGroup.checkedRadioButtonId == -1){
                Toast.makeText(this, "Bitte eine Beilage auswählen", Toast.LENGTH_SHORT).show()
            }
            else{
                    // Send order Data to createNoodleOrder function to add the Data to the shopping cart
                    createNoodleOrder(
                        noodleDropdown.selectedItem.toString(),noodleSauceRadioGroup.checkedRadioButtonId, garnishRadioButtonGroup.checkedRadioButtonId, orderQuantitiy
                    )
            }
        }
        noodleDialogBuilder.show()

    }

    //Saves the pizza order to the custom Data model to pass it further to the creation of the recipe (to save it in some kind of database for the shopping cart)
    private fun createPizzaOrder(pizzaName: String, chosenIngredients: Array<String>, chosenGarnish: Int, desiredQuantity: Int){

        pizzaOrder = PizzaOrderModel("", arrayOf(""),0, 0)
        pizzaOrder.pizza = pizzaName
        pizzaOrder.ingredients = chosenIngredients
        pizzaOrder.garnish = chosenGarnish
        pizzaOrder.quantity = desiredQuantity

        Log.d("Test_Order", "Pizza: " + pizzaName + " Ingredients: "+ chosenIngredients + " Garnish: " + getPizzaGarnishItemString(chosenGarnish) + " Quantity: " + desiredQuantity)
    }

    //Saves the pizza order to the custom Data model to pass it further to the creation of the recipe (to save it in some kind of database for the shopping cart)
    private fun createNoodleOrder(noodleType: String, sauce: Int, chosenGarnish: Int, desiredQuantity: Int){

        noodleOrder = NoodleOrderModel("", 0,0, 0)
        noodleOrder.noodleType = noodleType
        noodleOrder.chosenSauce = sauce
        noodleOrder.garnish = chosenGarnish
        noodleOrder.quantity = desiredQuantity

        Log.d("Test_Order", "NoodleType: " + noodleType + " Sauce: "+ getSauceString(sauce) + " Garnish: " + "Test" + " Quantity: " + desiredQuantity)
    }

/*    private fun getNoodleGarnishItemString(itemID: Int): String {
        return noodleGarnishItems.get(itemID - 1)   // need to substract 1, otherwise itemID and Data from the list is off by 1
    }*/

    private fun getPizzaGarnishItemString(itemID: Int): String {
        return pizzaGarnishItems.get(itemID - 1)   // need to substract 1, otherwise itemID and Data from the list is off by 1
    }

    private fun getSauceString(sauceID: Int): String{
        return noodleSauceTypes.get(sauceID-1)   // need to substract 1, otherwise itemID and Data from the list is off by 1
    }
}
