package br.ufpb.dcx.rodrigor.projetos.disciplina.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.Disciplina;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.PesoDisciplina;
import br.ufpb.dcx.rodrigor.projetos.disciplina.services.DisciplinaService;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import io.javalin.http.Context;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DisciplinaController {
    public DisciplinaController() {
    }

    public void listarDisciplinas(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
        ctx.attribute("disciplinas", disciplinaService.listarDisciplinas());
        ctx.render("/disciplinas/lista_disciplinas.html");
    }

    public void disciplinasEmJson(Context ctx){
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());

        List<Disciplina> disciplinas = disciplinaService.listarDisciplinas();
        ctx.json(disciplinas);
    }

    public void mostrarFormularioCadastro(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());

        ctx.attribute("pesos", PesoDisciplina.values());

        List<Participante> professores = participanteService.listarParticipantesPorCategoria(CategoriaParticipante.PROFESSOR);
        ctx.attribute("professores", professores);

        ctx.render("/disciplinas/formulario_disciplina.html");
    }

    public void mostrarFormularioEdicao(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());

        String id = ctx.pathParam("id");
        Optional<Disciplina> disciplinaOptional = disciplinaService.buscarDisciplinaPorId(id);

        if (disciplinaOptional.isPresent()) {
            ctx.attribute("disciplina", disciplinaOptional.get());
            ctx.attribute("pesos", PesoDisciplina.values());

            ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
            List<Participante> professores = participanteService.listarParticipantesPorCategoria(CategoriaParticipante.PROFESSOR);
            ctx.attribute("professores", professores);

            ctx.render("/disciplinas/formulario_edicao_disciplina.html");
        } else {
            ctx.status(404).result("Disciplina não encontrada");
        }
    }


    public void adicionarDisciplina(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());

        if (!validarParametrosObrigatorios(ctx, "nome", "descricao", "professor", "periodo", "peso")){
            ctx.render("/disciplinas/formulario_disciplina.html");
            return;
        }

        String nome = ctx.formParam("nome");
        String descricao = ctx.formParam("descricao");
        String professor = ctx.formParam("professor");

        // Verifica se o nome ultrapassa o limite de 50 caracteres
        if (nome != null && nome.length() > 50) {
            ctx.attribute("mensagemErro", "O nome da disciplina não pode ter mais de 50 caracteres.");

            ctx.attribute("pesos", PesoDisciplina.values());

            ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
            List<Participante> professores = participanteService.listarParticipantesPorCategoria(CategoriaParticipante.PROFESSOR);
            ctx.attribute("professores", professores);

            ctx.render("/disciplinas/formulario_disciplina.html");
            return;
        }

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(nome);
        disciplina.setDescricao(descricao);
        disciplina.setProfessor(professor);
        disciplina.setPeriodo(Integer.parseInt(Objects.requireNonNull(ctx.formParam("periodo"))));
        disciplina.setPeso(PesoDisciplina.valueOf(ctx.formParam("peso")));

        disciplinaService.adicionarDisciplina(disciplina);
        ctx.redirect("/disciplinas");
    }

    private boolean validarParametrosObrigatorios(Context ctx, String... parametros) {
        for (String param : parametros) {
            String valor = ctx.formParam(param);
            if (valor == null || valor.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void editarDisciplina(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());

        String id = ctx.formParam("id");
        Optional<Disciplina> disciplinaOptional = disciplinaService.buscarDisciplinaPorId(id);

        if (disciplinaOptional.isPresent()) {
            Disciplina disciplina = disciplinaOptional.get();

            disciplina.setNome(ctx.formParam("nome"));
            disciplina.setDescricao(ctx.formParam("descricao"));
            disciplina.setProfessor(ctx.formParam("professor"));
            disciplina.setPeriodo(Integer.parseInt(Objects.requireNonNull(ctx.formParam("periodo"))));
            disciplina.setPeso(PesoDisciplina.valueOf(ctx.formParam("peso")));

            disciplinaService.atualizarDisciplina(disciplina);
            ctx.redirect("/disciplinas");
        } else {
            ctx.status(404).result("Disciplina não encontrada");
        }
    }

    public void removerDisciplina(Context ctx) {
        DisciplinaService disciplinaService = ctx.appData(Keys.DISCIPLINA_SERVICE.key());
        String id = ctx.pathParam("id");
        disciplinaService.removerDisciplina(id);
        ctx.redirect("/disciplinas");
    }
}