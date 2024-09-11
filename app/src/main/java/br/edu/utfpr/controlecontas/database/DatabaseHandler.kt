package br.edu.utfpr.controlecontas.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.controlecontas.entity.Lancamento

class DatabaseHandler (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT, detalhe TEXT, valor REAL, dataLcto TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object{
        private  const val DATABASE_NAME = "dbfile.sqlite"
        private  const val DATABASE_VERSION = 1
        private  const val TABLE_NAME = "lancamentos"
        private  const val ID = 0
        private  const val TIPO = 1
        private  const val DETALHE = 2
        private  const val VALOR = 3
        private  const val DATALCTO = 4
    }

    fun insert(lancamento: Lancamento){
        val db = this.writableDatabase
        val registro = ContentValues()
        registro.put("tipo", lancamento.tipo)
        registro.put("detalhe", lancamento.detalhe)
        registro.put("valor", lancamento.valor)
        registro.put("dataLcto", lancamento.dataLcto)

        db.insert(TABLE_NAME, null, registro)
    }

    @SuppressLint("Recycle")
    fun list(): MutableList<Lancamento>{
        val db = this.writableDatabase
        val registro = db.query(TABLE_NAME, null, null, null, null, null, null)
        val registros = mutableListOf<Lancamento>()

        while (registro.moveToNext()) {
            val cadastro = Lancamento(
                registro.getInt(ID),
                registro.getString(TIPO),
                registro.getString(DETALHE),
                registro.getDouble(VALOR),
                registro.getString(DATALCTO)
            )

            registros.add(cadastro)
        }
        return registros
    }

    fun listCursor(): Cursor {
        val db = this.writableDatabase

        return db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}