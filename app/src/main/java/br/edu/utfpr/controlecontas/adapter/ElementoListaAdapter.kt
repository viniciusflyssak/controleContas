package br.edu.utfpr.controlecontas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.edu.utfpr.controlecontas.R
import br.edu.utfpr.controlecontas.entity.Lancamento
import java.text.NumberFormat
import java.util.Locale


class ElementoListaAdapter(private val context: Context, private val cursor: Cursor) : BaseAdapter() {

    companion object{
        private  const val ID = 0
        private  const val TIPO = 1
        private  const val DETALHE = 2
        private  const val VALOR = 3
        private  const val DATALCTO = 4
    }

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        return Lancamento(
            cursor.getInt(ID),
            cursor.getString(TIPO),
            cursor.getString(DETALHE),
            cursor.getDouble(VALOR),
            cursor.getString(DATALCTO))
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)

        return cursor.getInt(0).toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        val tvLancamento = v.findViewById<TextView>(R.id.tvLancamento)

        cursor.moveToPosition(position)

        tvLancamento.text = if (cursor.getString(TIPO) == "Débito"){"D"} else {"C"} + " " + cursor.getString(
            DATALCTO) + " - " + cursor.getString(DETALHE) + " - " + NumberFormat.getCurrencyInstance(
            Locale("pt", "BR")
        ).format(cursor.getDouble(VALOR))

        return v
    }
}