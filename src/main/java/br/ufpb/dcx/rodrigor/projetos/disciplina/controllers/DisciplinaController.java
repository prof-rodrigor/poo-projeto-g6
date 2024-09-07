package br.ufpb.dcx.rodrigor.projetos.disciplina.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.Disciplina;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.PesoDisciplina;
import br.ufpb.dcx.rodrigor.projetos.disciplina.services.DisciplinaService;
import io.javalin.http.Context;

import java.util.Objects;

public class DisciplinaController {
    public DisciplinaController(){}

    public void listarDisciplinas(Context ctx){
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
        ctx.attribute("disciplinas", disciplinaService.listarDisciplinas());
        ctx.render("/disciplinas/lista_disciplinas.html");
    }

    public void mostrarFormularioCadastro(Context ctx){
        ctx.attribute("pesos", PesoDisciplina.values());
        ctx.render("/disciplinas/formulario_disciplina.html");
    }

    public void adicionarDisciplina(Context ctx){
            DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
            String nome = ctx.formParam("nome");
            String descricao = ctx.formParam("descricao");

            // Verifica se o nome ultrapassa o limite de 50 caracteres
            if (nome != null && nome.length() > 50) {
                ctx.attribute("mensagemErro", "O nome da disciplina não pode ter mais de 50 caracteres.");

                ctx.attribute("pesos", PesoDisciplina.values());

                ctx.render("/disciplinas/formulario_disciplina.html");
                return;
            }

            Disciplina disciplina = new Disciplina();
            disciplina.setNome(nome);
            disciplina.setDescricao(descricao);
            int periodo = Integer.parseInt(Objects.requireNonNull(ctx.formParam("periodo")));
            disciplina.setPeriodo(periodo);
            disciplina.setPeso(PesoDisciplina.valueOf(ctx.formParam("peso")));

            disciplinaService.adicionarDisciplina(disciplina);
            ctx.redirect("/disciplinas");
        }


        public void removerDisciplina (Context ctx){
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
        String id = ctx.pathParam("id");
        disciplinaService.removerDisciplina(id);
        ctx.redirect("/disciplinas");
    }
}