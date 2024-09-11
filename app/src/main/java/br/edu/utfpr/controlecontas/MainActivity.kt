package br.edu.utfpr.controlecontas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import br.edu.utfpr.controlecontas.database.DatabaseHandler
import br.edu.utfpr.controlecontas.entity.Lancamento
import java.text.NumberFormat
import java.util.Locale

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {
    private lateinit var spTipo : Spinner
    private lateinit var spDetalhe : Spinner
    private lateinit var etValor: EditText
    private lateinit var etDataLancamento: EditText
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spTipo = findViewById(R.id.spTipo)
        spDetalhe = findViewById(R.id.spDetalhe)
        etValor = findViewById(R.id.etValor)
        etDataLancamento = findViewById(R.id.etDataLancamento)

        databaseHandler = DatabaseHandler(this)

        val tipo = listOf("Crédito", "Débito")
        val detalhe = listOf("Salário", "Extras")

        spTipo.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tipo)
        spDetalhe.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, detalhe)


        spTipo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var detalhe = listOf("Salário", "Extras")
                if (spTipo.selectedItem.toString() == "Débito"){
                    detalhe = listOf("Alimentação", "Transporte", "Saúde", "Moradia")
                }
                spDetalhe.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, detalhe)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }
    }

    fun btLancarOnClick(view: View) {
        if (etDataLancamento.text.isEmpty()){
            etDataLancamento.error = getString(R.string.erro_data)
            etDataLancamento.requestFocus()
            return
        }

        if (etValor.text.isEmpty()){
            etValor.error = getString(R.string.erro_valor)
            etValor.requestFocus()
            return
        }

        if (etValor.text.toString().toDouble() <= 0){
            etValor.error = getString(R.string.erro_valor_zerado)
            etValor.requestFocus()
            return
        }

        val lancamento = Lancamento(0, spTipo.selectedItem.toString(), spDetalhe.selectedItem.toString(), etValor.text.toString().toDouble(), etDataLancamento.text.toString())

        databaseHandler.insert(lancamento)

        etValor.setText("")
        etDataLancamento.setText("")
        spTipo.setSelection(0)
        spDetalhe.setSelection(0)

        Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
    }

    fun btVerLancamentosOnClick(view: View) {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    fun btSaldoOnClick(view: View) {
        val lancamentos = this.databaseHandler.list()
        val tvSaldo = TextView(this)

        var valorCredito = 0.00
        var valorDebito = 0.00

        lancamentos.forEach {
            if (it.tipo == "Débito") {
                valorDebito += it.valor
            } else {
                valorCredito += it.valor
            }
        }
        val valorSaldo = valorCredito - valorDebito

        tvSaldo.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valorSaldo)
        tvSaldo.setPadding(50)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saldo")
        builder.setView(tvSaldo)
        builder.setCancelable(false)
        builder.setNegativeButton("Fechar", null)
        builder.show()

    }
}