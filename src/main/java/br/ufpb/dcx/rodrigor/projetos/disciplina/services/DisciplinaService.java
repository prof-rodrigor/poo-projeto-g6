package br.ufpb.dcx.rodrigor.projetos.disciplina.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBRepository;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.Disciplina;
import br.ufpb.dcx.rodrigor.projetos.disciplina.model.PesoDisciplina;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class DisciplinaService extends AbstractService {
    private final MongoCollection<Document> collection;
    public DisciplinaService(MongoDBRepository mongoDBRepository) {
        super(mongoDBRepository);
        MongoDatabase database = mongoDBRepository.getDatabase("disciplinas");
        this.collection =  database.getCollection("disciplinas");
    }
    public List<Disciplina> listarDisciplinasPorPeso(PesoDisciplina pesoDisciplina){
        List<Disciplina> disciplinas = new LinkedList<>();
        for (Document doc : collection.find(eq("peso", pesoDisciplina.name()))){
            disciplinas.add(documentToDisciplina(doc));
        }
        return disciplinas;
    }
    public List<Disciplina> listarDisciplinas(){
        List<Disciplina> disciplinas = new LinkedList<>();
        for (Document doc : collection.find()){
            disciplinas.add(documentToDisciplina(doc));
        }
        return disciplinas;
    }

    public Optional<Disciplina> buscarDisciplinaPorId(String id){
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(doc).map(DisciplinaService::documentToDisciplina);
    }

    public void adicionarDisciplina(Disciplina disciplina){
        Document doc = disciplinaToDocument(disciplina);
        collection.insertOne(doc);
        disciplina.setId(doc.getObjectId("_id"));
    }

    public void atualizarDisciplina(Disciplina disciplinaAtualizada){
        Document doc = disciplinaToDocument(disciplinaAtualizada);
        collection.replaceOne(eq("_id", new ObjectId(disciplinaAtualizada.getId().toString())), doc);
    }
    public void removerDisciplina (String id) {
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    public static Disciplina documentToDisciplina(Document doc) {
        Disciplina disciplina = new Disciplina();

        disciplina.setId(doc.getObjectId("_id"));
        disciplina.setNome(doc.getString("nome"));
        disciplina.setDescricao(doc.getString("descricao"));
        disciplina.setProfessor(doc.getString("professor"));
        disciplina.setPeriodo(doc.getInteger("periodo"));
        disciplina.setPeso(PesoDisciplina.valueOf(doc.getString("peso")));
        return disciplina;
    }
    public static Document disciplinaToDocument(Disciplina disciplina){
        Document doc = new Document();
        if (disciplina.getId() != null){
            doc.put("_id", disciplina.getId());
        }
        doc.put("nome", disciplina.getNome());
        doc.put("descricao", disciplina.getDescricao());
        doc.put("professor", disciplina.getProfessor());
        doc.put("periodo", disciplina.getPeriodo());
        doc.put("peso", disciplina.getPeso());
        return doc;
    }
}