package com.example.mytrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//tem de importar android.view.View para o onclicklistener funcionar
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException

//Tem de colocar a View.OncliListener para chamar os elementos na Main
class MainActivity : AppCompatActivity(), View.OnClickListener {

    //fazer o override para o onclicklistener funcionar
    override fun onClick(view: View) {
        //tem de criar a val id, para eu saber distinguir onde estarei aplicando o evento de click
        val id = view.id
        //se o id for o id do botão, faça isso:
        if (id == R.id.botaoCalc) {
            Calcular()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botaoCalc.setOnClickListener(this)

    }

    private fun Calcular() {
        if (isValid()) {

            try {
                // (ditancia * preço) / autonomia
                val dist = editDist.text.toString().toFloat()
                val preco = ediPreco.text.toString().toFloat()
                val auto = editAuto.text.toString().toFloat()
                val resultado = ((dist * preco) / auto)
                textValor.setText("Total R$: $resultado")

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

}