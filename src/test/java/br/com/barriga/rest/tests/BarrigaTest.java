package br.com.barriga.rest.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.barriga.rest.core.BaseTest;

public class BarrigaTest extends BaseTest {

	private String TOKEN;
	private Integer ID;

	@Before
	public void login() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "demis@beelab.com.br");
		login.put("senha", "123456");

		TOKEN = given().body(login).when().post("/signin").then().statusCode(200).extract().path("token");
	}

	/*
	 * @Test public void naoDeveAcessarAPISemToken() { given() .when()
	 * .get("/contas") .then() .statusCode(401) ; }
	 */

	@Test
	public void deveIncluirContaComSucesso() {

		ID = given().header("Authorization", "JWT " + TOKEN).body("{ \"nome\": \"Conta Teste Automação Inserida\" }")
				.when().post("/contas").then().statusCode(201).extract().path("id");
	}

	@Test
	public void deveAlterarContaComSucesso() {

		ID = given().header("Authorization", "JWT " + TOKEN)
				.body("{ \"nome\": \"Conta Teste Automação INSERIDA PARA EDIÇÃO\" }").when().post("/contas").then()
				.statusCode(201).extract().path("id");

		given().header("Authorization", "JWT " + TOKEN).body("{ \"nome\": \"Conta Teste Automação EDITADA\" }").when()
				.put("/contas/" + ID).then().statusCode(200).body("nome", is("Conta Teste Automação EDITADA"));
	}

	/*
	 * @Test public void deveDeletarContaComSucesso() {
	 * 
	 * ID = given().header("Authorization", "JWT " + TOKEN)
	 * .body("{ \"nome\": \"Conta Teste Automação INSERIDA PARA DELEÇÃO\" }").when()
	 * .post("/contas").then() .statusCode(201).extract().path("id");
	 * 
	 * 
	 * given() .header("Authorization", "JWT " + TOKEN) .when() .delete("/contas/" +
	 * ID) .then() .statusCode(204) ;
	 * 
	 * }
	 */

	@After
	public void limparBase() {
		given().header("Authorization", "JWT " + TOKEN).when().delete("/contas/" + ID).then().statusCode(204);
	}
}
