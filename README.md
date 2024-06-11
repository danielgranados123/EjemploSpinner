Esta aplicación es para realizar la práctica del spinner
Esta aplicacion aun no tiene programdo el Spinner y en claes lo vamos a programar
</br></br>

CREATE TABLE tbDoctores (</br>
    DoctorUUID VARCHAR2(50) PRIMARY KEY,</br>
    nombreDoctor VARCHAR2(50),</br>
    Especialidad VARCHAR2(50),</br>
    Telefono VARCHAR2(20)</br>
);
</br></br>
CREATE TABLE tbPacientes (</br>
    PacienteUUID VARCHAR2(50) PRIMARY KEY,</br>
    DoctorUUID VARCHAR2(50),</br>
    Nombre VARCHAR2(50),</br>
    FechaNacimiento VARCHAR2(50),</br>
    Direccion VARCHAR2(200),</br>
    FOREIGN KEY (DoctorUUID) REFERENCES tbDoctores(DoctorUUID)</br>
);</br>
</br>
select * from tbDoctores;</br>
select * from tbPacientes;</br></br>


//Mostrar el calendario al hacer click en el EditText txtFechaNacimientoPaciente</br>
        txtFechaNacimiento.setOnClickListener {</br>
            val calendario = Calendar.getInstance()</br>
            val anio = calendario.get(Calendar.YEAR)</br>
            val mes = calendario.get(Calendar.MONTH)</br>
            val dia = calendario.get(Calendar.DAY_OF_MONTH)</br>
            val datePickerDialog = DatePickerDialog(</br>
                requireContext(),</br>
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado -></br>
                    val fechaSeleccionada =</br>
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"</br>
                    txtFechaNacimiento.setText(fechaSeleccionada)</br>
                },</br>
                anio, mes, dia</br>
            )</br>
            datePickerDialog.show()</br>
        }</br>

</br>
//Luego de programar el calendario, modifico el XML para que no aparezca el teclado</br>
 android:inputType="none"</br>
 android:focusable="false"</br>
