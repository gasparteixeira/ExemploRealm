package epm.senacrs.com.br.exemplorealm;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class SalvarActivity extends AppCompatActivity {

    private TextView txtData;
    private EditText txtDescricao, txtLocal, txtNome;
    private long dtTermino;
    private CheckBox ckbIniciada;
    private int Ano, Mes, Dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar);

        InicializaListeners();

        final Calendar cal = Calendar.getInstance();
        Ano = cal.get(Calendar.YEAR);
        Mes = cal.get(Calendar.MONTH);
        Dia = cal.get(Calendar.DAY_OF_MONTH);

        AtualizarData();


    }

    public void onSalvar(View wiew) {

        txtNome = (EditText)findViewById(R.id.taskNome);
        txtDescricao = (EditText)findViewById(R.id.taskDescricao);
        txtLocal = (EditText)findViewById(R.id.taskLocal);
        Calendar cal = Calendar.getInstance();
        cal.set(Ano, Mes, Dia);
        dtTermino = cal.getTime().getTime();
        ckbIniciada = (CheckBox)findViewById(R.id.taskIniciada);

        final Task task = new Task();
        task.nome = txtNome.getText().toString();
        task.descricao = txtDescricao.getText().toString();
        task.local = txtLocal.getText().toString();
        task.termino = dtTermino;
        task.iniciada = ckbIniciada.isChecked();


        ((CoreApplication)getApplication()).realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                bgRealm.copyToRealmOrUpdate(task);
            }
        }, new Realm.Transaction.OnSuccess() {
            public void onSuccess() {
                finish();
            }
        }, new Realm.Transaction.OnError() {
            public void onError(Throwable error) {}
        });

    }

    public void InicializaListeners()
    {
        txtData = (TextView) findViewById(R.id.taskTermino);
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle      savedInstanceState)
        {
            final Calendar calendario = Calendar.getInstance();
            Ano = calendario.get(Calendar.YEAR);
            Mes = calendario.get(Calendar.MONTH);
            Dia = calendario.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, Ano, Mes, Dia);
        }


        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Ano = year;
            Mes = month;
            Dia = day;
            AtualizarData();
            MensagemData();
        }
    }



    private void AtualizarData()
    {
        txtData.setText(new StringBuilder().append(Dia).append("/").append(Mes + 1).append("/").append(Ano).append(" "));
    }

    private void MensagemData()
    {
        Toast.makeText(this, new StringBuilder().append("Data: ").append(txtData.getText()),  Toast.LENGTH_SHORT).show();
    }

    public void MostrarData(View v)
    {
        DialogFragment ClasseData = new  DatePickerFragment();
        ClasseData.show(getFragmentManager(), "datepicker");
    }


}
