package br.ufpb.dcx.rodrigor.projetos.login;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.bson.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.ufpb.dcx.rodrigor.projetos.App.logger;
import static java.net.HttpURLConnection.HTTP_OK;

public class UsuarioService extends AbstractService {
    private final MongoCollection<Document> usuariosCollection;

    public UsuarioService (MongoDBRepository mongoDBRepository) {
        super(mongoDBRepository);
        MongoDatabase database = mongoDBRepository.getDatabase("usuarios");
        this.usuariosCollection = database.getCollection("usuarios");
    }
    public void cadastrarUsuario (Usuario usuario) throws IllegalAccessException {
        if(buscarUsuarioPorLogin(usuario.getLogin()) != null){
            throw new IllegalAccessException("Um usuário com este login já existe");
        }
        Document doc = usuarioToDocument(usuario);
        usuariosCollection.insertOne(doc);
        usuario.setId(doc.getObjectId("_id").toString());
    }

    public Usuario buscarUsuario(String login, String senha){
        HttpClient httpClient = HttpClient.newHttpClient();
        String url = "http://localhost:8080/v1/autenticar";
        Map<String, String> dados = new HashMap<>();
        dados.put("login", login);
        dados.put("senha", senha);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String loginJson = mapper.writeValueAsString(dados);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(loginJson, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HTTP_OK) {
                Usuario usuario = mapper.readValue(response.body(), Usuario.class);
                return usuario;
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
        return null;
    }

    public void removerUsuario (String id){
        usuariosCollection.deleteOne(new Document("_id", new ObjectId(id)));
    }

    public List<Usuario> listarUsuarios(){
        List<Usuario> usuarios =new ArrayList<>();
        for(Document doc : usuariosCollection.find()){
            usuarios.add(documentToUsuario(doc));
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorLogin (String login){
        Document doc = usuariosCollection.find(new Document("login", login)).first();
        if (doc == null) {
            return null;
        }
        return documentToUsuario(doc);
    }

    public Document usuarioToDocument (Usuario usuario){
        String hashedSenha = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        return new Document("login", usuario.getLogin())
                .append("nome", usuario.getNome())
                .append("senha", hashedSenha);
    }
    public Usuario documentToUsuario (Document doc){
        return new Usuario(
                doc.getObjectId("_id").toString(),
                doc.getString("login"),
                doc.getString("nome"),
                doc.getString("senha")
        );
    }
}
