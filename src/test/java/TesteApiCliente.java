import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

//executa os metodos de acordo com a ordem passada na anotação @Order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TesteApiCliente {
    // Pega todos os clientes
    String servicoCliente = "http://localhost:8080";

    @Test
    @Order(1)
    @DisplayName("Quando eu pegar a lista de clientes sem cadastrar nenhum, entao nao tem nenhum cliente cadastrado")
    public void pegaClientes(){

        given()
                .contentType(ContentType.JSON)
        .when()
                .get(servicoCliente)
        .then()
                .statusCode(200)
                .body(new IsEqual("{}"));
    }

    @Test
    @Order(2)
    @DisplayName("Quando cadasatrar um cliente. Entao o cliente deve estar presente na resposta")
    public void cadastraCliente(){

        String endpointCliente = "/cliente";

        String clienteParaCadastrar = "        { \n" +
                "            \"nome\": \"Daniela Pochini\",\n" +
                "            \"idade\": 25, \n" +
                "             \"id\": \"01\"\n" +
                "        }";

        //String respostaEsperada = "{\"1\":{\"nome\":\"Daniela Pochini\",\"idade\":25,\"id\":1,\"risco\":0}}";

        // POST

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
        .when()
                .post(servicoCliente+endpointCliente)
        .then()
                .statusCode(201)
                .body("1.nome",  equalTo("Daniela Pochini"))
                .body("1.idade", equalTo(25));
                //.body(new IsEqual(respostaEsperada));
    }

    @Test
    @Order(3)
    @DisplayName("Quando eu atualizar um cliente existente. Entao as novas informacoes deverao ser exibidas com sucesso")
    public void atualizaCliente(){

        String endpointCliente = "/cliente";

        String clienteParaAtualizar = "        { \n" +
                "            \"nome\": \"Daniela Pereira Pochini\",\n" +
                "            \"idade\": 26, \n" +
                "             \"id\": \"01\" \n" +
                "        }";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaAtualizar)
        .when()
                .put(servicoCliente+endpointCliente)
        .then()
                .statusCode(200)
                .body("1.nome", equalTo("Daniela Pereira Pochini"))
                .body("1.idade", equalTo(26));
    }

    @Test
    @Order(4)
    @DisplayName("Quando eu deletar um cliente. Entao devera ser removido com sucesso")
    public void deletaCliente(){
        String endpointCliente = "/cliente";

        String idCliente = "/1";

        String respostaEsperada = "CLIENTE REMOVIDO: { NOME: Daniela Pereira Pochini, IDADE: 26, ID: 1 }";

        given()
                .contentType(ContentType.JSON)
        .when()
                .delete(endpointCliente+idCliente)
        .then()
            .statusCode(200)
            .body(new IsEqual(respostaEsperada));
    }

}
