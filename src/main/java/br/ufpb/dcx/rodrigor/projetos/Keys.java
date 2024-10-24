package br.ufpb.dcx.rodrigor.projetos;

import br.ufpb.dcx.rodrigor.projetos.db.MongoDBRepository;
import br.ufpb.dcx.rodrigor.projetos.disciplina.services.DisciplinaService;
import br.ufpb.dcx.rodrigor.projetos.login.UsuarioService;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.services.ProjetoService;
import io.javalin.config.Key;

public enum Keys {
    MONGO_DB(new Key<MongoDBRepository>("mongo-db")),
    USUARIO_SERVICE(new Key<UsuarioService>("usuario-service")),
    FORM_SERVICE(new Key<ParticipanteService>("form-service")),
    PROJETO_SERVICE(new Key<ProjetoService>("projeto-service")),
    PARTICIPANTE_SERVICE(new Key<ParticipanteService>("participante-service")),
    DISCIPLINA_SERVICE(new Key<DisciplinaService>("disciplina-service"));

    private final Key<?> k;

    <T> Keys(Key<T> key) {
        this.k = key;
    }

    public <T> Key<T> key() {
        @SuppressWarnings("unchecked")
        Key<T> typedKey = (Key<T>) this.k;
        return typedKey;
    }
}