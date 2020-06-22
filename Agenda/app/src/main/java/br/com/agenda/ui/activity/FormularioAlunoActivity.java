package br.com.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import br.com.agenda.DAO.AlunoDAO;
import br.com.agenda.R;
import br.com.agenda.model.Aluno;

import static br.com.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

public class FormularioAlunoActivity extends AppCompatActivity {
    private static final String TITULO_APPBAR_NOVO_ALUNO = "Novo Aluno";
    private static final String TITULO_APPBAR_EDITA_ALUNO = "Edita Aluno";
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoEmail;
    private final AlunoDAO dao = new AlunoDAO();
    private Aluno aluno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_aluno);
        inicializacaoDosCampos();
        configuraBotaoSalvar();
        carregaAluno();
    }

    private void carregaAluno() {
        Intent dados = getIntent(); //Tranferencia de informações entre activitys, a classe precisa ser Serializable
        if (dados.hasExtra(CHAVE_ALUNO)) {
            setTitle(TITULO_APPBAR_EDITA_ALUNO);
            aluno = (Aluno) dados.getSerializableExtra(CHAVE_ALUNO);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_ALUNO);
            aluno = new Aluno();
        }
    }

    private void preencheCampos() {
        campoNome.setText(aluno.getNome());
        campoEmail.setText(aluno.getEmail());
        campoTelefone.setText(aluno.getTelefone());
    }

    private void configuraBotaoSalvar() {
        Button botaoSalvar = findViewById(R.id.activity_formulario_aluno_botao_salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizaFormulario();
            }
        });
    }

    private void finalizaFormulario() {
        preencheAluno();
        if (validar(aluno)) {
            if (aluno.temIdValido()) {
                dao.edita(aluno);
            } else {
                dao.salva(aluno);
            }
            finish();
        }
    }

    private void inicializacaoDosCampos() {
        campoNome = findViewById(R.id.activity_formulario_aluno_nome);
        campoTelefone = findViewById(R.id.activity_formulario_aluno_telefone);
        campoEmail = findViewById(R.id.activity_formulario_aluno_email);
    }

    private void preencheAluno() {
        String nome = campoNome.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String email = campoEmail.getText().toString();
        aluno.setNome(nome);
        aluno.setTelefone(telefone);
        aluno.setEmail(email);
    }

    public boolean validar(Aluno aluno) {

        boolean validado = true;
        if (aluno.getNome().length() < 3) {
            //se nome tiver menos de 3 letras
            validado = false;
            campoNome.setError("Nome inválido");//adiciona erro no componente na tela
        }
        if (!aluno.getEmail().contains("@")) {
            //verifica se tem @ no campo do email, e nega a condição no comeco, se não tiver @ não é valido
            validado = false;
            campoEmail.setError("Email inválido");//adciona erro no componente;
        }

        if (aluno.getTelefone().isEmpty() || aluno.getTelefone().length() != 9) {
            //verifica se as senhas sao iguais
            validado = false;
            campoTelefone.setError("Telefone inválido");//add erro no componente
        }
        return validado;
    }


}