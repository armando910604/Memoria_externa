package armando.memoria_externa;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MemoriaExt extends ActionBarActivity implements View.OnClickListener{
    private EditText texto;
    private Button guardar, abrir;
    private static final int READ_BLOCK_SIZE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoria_ext);
        texto = (EditText) findViewById(R.id.archivo);
        guardar = (Button)findViewById(R.id.guardar);
        abrir = (Button)findViewById(R.id.abrir);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memoria_ext, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        File externa, direccion, file = null;
        try {
        // comprobamos si se encuentra montada nuestra memoria externa

            if (Environment.getExternalStorageState().equals("mounted")) {
                // Obtenemos la direccion de la memoria externa
                externa = Environment.getExternalStorageDirectory();
                if (v.equals(guardar)) {
                    String text = texto.getText().toString();
                    // Clase que permite grabar texto en un archivo
                    FileOutputStream filestream = null;
                    try {
                        // instanciamos un objeto File para crear un nuevo directorio en la memoria externa
                        direccion = new File(externa.getAbsolutePath() + "/direccionExterna");
                        // se crea el nuevo directorio donde se creara el archivo
                        direccion.mkdirs();
                        // creamos el archivo en el nuevo directorio creado
                        file = new File(direccion, "MiArchivo.txt");
                        filestream = new FileOutputStream(file);
                        // Convierte un stream de caracteres en un stream de bytes
                        OutputStreamWriter write = new OutputStreamWriter(filestream);
                        write.write(text);
                        // Escribe en el buffer la cadena de texto
                        write.flush(); // Envia lo que hay en el buffer al archivo
                        write.close(); // Cierra el archivo de texto
                        Toast.makeText(getBaseContext(), R.string.guardararchivo, Toast.LENGTH_SHORT).show();
                        texto.setText("");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block e.printStackTrace(); }
                    }
                }

                if (v.equals(abrir)) {
                    try {
                        //Obtenemos el direcorio donde se encuentra el archivo que se va a leer
                        direccion = new File(externa.getAbsolutePath() + "/ArchivosExternos");
                        //Creamos un objeto File de nuestro archivo a leer
                        file = new File(direccion, "MiArchivo.txt");
                        //Creamos un objeto de la clase FileInputStream
                        // el cual representa un stream del archivo que vamos a leer
                        FileInputStream filestream = new FileInputStream(file);
                        //Creamos un objeto InputStreamReader que nos permitira
                        // leer el stream del archivo abierto
                        InputStreamReader reader = new InputStreamReader(filestream);
                        char[] Buffer = new char[READ_BLOCK_SIZE];
                        String text = "";
                        // Se lee el archivo de texto mientras no se llegue al
                        // final de él
                        int read;
                        while ((read = reader.read(Buffer)) > 0) {
                            // Se lee por bloques de 100 caracteres
                            // ya que se desconoce el tamaño del texto
                            // Y se va copiando a una cadena de texto
                            String copia = String.copyValueOf(Buffer, 0, read);
                            text += copia;
                            Buffer = new char[READ_BLOCK_SIZE];
                        }
                        // Se muestra el texto leido en la caje de texto
                        texto.setText(text);
                        reader.close();
                        Toast.makeText(getBaseContext(), R.string.cargararchivo, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                    // TODO: handle exception
                    }
                }
                else {

                    Toast.makeText(getBaseContext(), R.string.noguarda, Toast.LENGTH_LONG).show();
                }

            }

        }
        catch(Exception e){
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
