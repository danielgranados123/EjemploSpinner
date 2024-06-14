package bryan.miranda.ejemplospinner

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.dataClassDoctores
import java.util.UUID

class pacientes : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_pacientes, container, false)

        //1-Mandar a llamar a los elementos
        val spDoctores= root.findViewById<Spinner>(R.id.spDoctores)
        val txtFechaNacimientoPaciente = root.findViewById<EditText>(R.id.txtFechaNacimiento)
        val txtNombrePaciente = root.findViewById<EditText>(R.id.txtNombrePaciente)
        val txtDireccion = root.findViewById<EditText>(R.id.txtDireccionPaciente)
        val btnGuardarPaciente = root.findViewById<Button>(R.id.btnGuardarPaciente)

        //Funcion para hacer el select de los nombres de los doctores que voy a mostrar en el Spinner

        fun obtenerDoctores(): List<dataClassDoctores>{
            //Crear un objeto de la clase conexion
            val objConexion= ClaseConexion().cadenaConexion()

            //crepo un Statement que me ejecutara el Select
            val Statement = objConexion?.createStatement()

            val resulset = Statement?.executeQuery("Select * from tbDoctores")!!

            val listadoDoctores= mutableListOf<dataClassDoctores>()

            while (resulset.next()){
                val uuid = resulset.getString("DoctorUUID")
                val nombre = resulset.getString("nombreDoctor")
                val especialidad = resulset.getString("especialidad")
                val telefono = resulset.getString("telefono")
                val unDoctorCompleto = dataClassDoctores(uuid, nombre, especialidad, telefono)

                listadoDoctores.add(unDoctorCompleto)
            }

            return listadoDoctores
        }

        //Programar Spinner para que muestre datos del select

        CoroutineScope(Dispatchers.IO).launch {
            //Obtengo los datos
            val listadoDeDoctores = obtenerDoctores()
            val nombreDoctores = listadoDeDoctores.map { it.nombreDoctor }

            withContext(Dispatchers.Main){
            //2-Crear y modificar el adaptador
            val miAdaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombreDoctores)

            spDoctores.adapter = miAdaptador
        }
        }

        txtFechaNacimientoPaciente.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaNacimientoPaciente.setText(fechaSeleccionada)
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        btnGuardarPaciente.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                val doctor = obtenerDoctores()


                val addPaciente = objConexion?.prepareStatement("insert into tbPacientes (pacienteUUID, doctorUUID, nombre, fechaNacimiento, direccion) values (?, ?, ?, ?, ?")!!

                addPaciente.setString(1, UUID.randomUUID().toString())
                addPaciente.setString(2, doctor[spDoctores.selectedItemPosition].DoctorUUID)
                addPaciente.setString(3, txtNombrePaciente.text.toString())
                addPaciente.setString(4, txtFechaNacimientoPaciente.text.toString())
                addPaciente.setString(5, txtDireccion.text.toString())
                addPaciente.executeUpdate()

                withContext(Dispatchers.Main){
                    txtNombrePaciente.setText("")
                    txtDireccion.setText("")
                    txtFechaNacimientoPaciente.setText("")
                    Toast.makeText(requireContext(), "Paciente agregado con éxito", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return root
    }
}