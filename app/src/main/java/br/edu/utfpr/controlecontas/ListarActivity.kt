package br.edu.utfpr.controlecontas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import br.edu.utfpr.controlecontas.adapter.ElementoListaAdapter
import br.edu.utfpr.controlecontas.database.DatabaseHandler

class ListarActivity : AppCompatActivity() {
    private lateinit var lvRegistro : ListView
    private lateinit var banco : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        lvRegistro = findViewById(R.id.lvRegistros)
        banco = DatabaseHandler(this)

        val registros = banco.listCursor()
        val adapter = ElementoListaAdapter(
            this,
            registros
        )

        lvRegistro.adapter = adapter
    }
}