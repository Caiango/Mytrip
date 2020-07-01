package com.example.mytrip

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
//tem de importar android.view.View para o onclicklistener funcionar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException
import java.text.DecimalFormat

//Tem de colocar a View.OncliListener para chamar os elementos na Main
class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var switch: Switch
    var resultado: Float = 0f

    //fazer o override para o onclicklistener funcionar
    override fun onClick(view: View) {
        //tem de criar a val id, para eu saber distinguir onde estarei aplicando o evento de click
        val id = view.id
        //se o id for o id do botão, faça isso:
        if (id == R.id.botaoCalc) {
            Calcular()
            hideKeyboard()
        } else if (id == R.id.botaoCalc2) {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch = findViewById(R.id.switch1)

        botaoCalc.setOnClickListener(this)
        botaoCalc2.setOnClickListener(this)

    }

    override fun onResume() {
        getDist()
        super.onResume()
    }

    private fun Calcular() {
        if (isValid()) {

            try {
                // (ditancia * preço) / autonomia
                val dist = editDist.text.toString().toFloat()
                val preco = ediPreco.text.toString().toFloat()
                val auto = editAuto.text.toString().toFloat()
                if (switch.isChecked) {
                    resultado = (((dist * preco) / auto) * 2)

                } else {
                    resultado = ((dist * preco) / auto)
                }

                val df = DecimalFormat("0.00")
                val str = df.format(resultado)
                textValor.setText("Total R$: $str")

            } catch (nfe: NumberFormatException) {
                Toast.makeText(this, getString(R.string.valor_valido), Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, getString(R.string.valor_valido), Toast.LENGTH_LONG).show()
        }

    }

    private fun isValid(): Boolean {
        return editDist.text.toString() != ""
                && ediPreco.text.toString() != ""
                && editAuto.text.toString() != ""
                && editAuto.text.toString() != "0"
    }

    fun getDist() {
        editDist.text = Editable.Factory.getInstance().newEditable(MapsActivity.dist)
    }

    //Três funções para esconder o teclado
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}