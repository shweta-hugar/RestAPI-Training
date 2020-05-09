package Testing.UST;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

/**
 * Unit test for simple App.
 */
public class AppTest {

	static int identity;
	
	static String accessToken;
	
	String root = System.getProperty("user.dir");

	@BeforeClass
	public void beforeTests() {
		RestAssured.baseURI = "http://petstore.swagger.io";
		RestAssured.basePath= "/v2";
	}
	
	@DataProvider
	public Object[][] dpGetWithParam() {
		Object[][] testDatas = new Object[][] { 
			new Object[] { 1, 1,"tom","jerry","www.google.com","www.ust.com","www.pst.com",11,"testing",12,"resting","available" },
			new Object[] {2, 11,"tomy","jerry","www.google.com","www.ust.com","www.pst.com",13,"testing",15,"resting","available" } };
		return testDatas;
	}
	
	@Test(dataProvider = "dpGetWithParam")
	public void createPetWithData(int petId, int cId, String cName, String name, String p1, String p2, String p3, int tId, String tName, int tId_one,String tNameOne, String status )
	{
		Category c = new Category(cId,cName);
		Tag t = new Tag(tId,tName);
		Tag t1 = new Tag(tId_one,tNameOne);
		String[] ph = new String[] {p1,p2,p3};
		Tag[] ar = new Tag[] {t,t1};
		PET pt = new PET(petId,c,name,ph,ar,status);
		RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).body(pt);
				request.log().all();
				Response res = request.post("/pet");
				res.then().log().all();
				System.out.println(res.getHeader("Content-Type"));
	}

	@Test(priority=1)	
	public void createPet()
	{
		Category c = new Category(1,"tom");
		Tag t = new Tag(11,"testing");
		Tag t1 = new Tag(12,"resting");
		String[] ph = new String[] {"www.google.com","www.ust.com","www.pst.com"};
		Tag[] ar = new Tag[] {t,t1};
		PET pt = new PET(52,c,"jerry",ph,ar,"available");
		RequestSpecification request = RestAssured.given().contentType(ContentType.JSON).body(pt);
				request.log().all();
				Response res = request.post("/pet");
				res.then().log().all();
				System.out.println(res.getHeader("Content-Type"));
	Headers test = 	res.getHeaders();
	
	PET pet = res.getBody().as(PET.class);
	
	Assert.assertTrue(res.getBody().asString().contains("tom"));
	res.then().body(matchesJsonSchemaInClasspath("schematest.json"));
	File f = new File(root+ "\\src\\test\\resources\\testdata\\schematest.json");
	res.then().body(matchesJsonSchema(f));
	res.then().time(lessThan(2000L));
	System.out.println(res.getTime());
	System.out.println(res.getTimeIn(TimeUnit.SECONDS));
	for(Header p : test)
	{
		System.out.println(p.getName() + "  -----   "+ p.getValue());
	}
				
				res.then().body("id", equalTo(52));
				res.then().body("id", Matchers.is(52));
				res.then().body("id",greaterThan(10));
				res.then().body("tags.id", hasItems(11,12));
				res.then().body("id", any(Integer.class));
    			
			   res.then().body("tags.findAll {it.id > 0}.name", hasItem("testing"));
//				res.then().log().body();
//				res.then().log().headers();
				res.then().log().ifError();
		Assert.assertEquals(res.getStatusCode(),200);
		Assert.assertTrue(res.getBody().asString().contains("testing"));
		Assert.assertEquals(res.jsonPath().getInt("id"), 52);
		
	}
	
	

	@Test(priority=2)	
	public void updatePet()
	{
		String petInfo = "{\r\n" + 
				"  \"id\": 14,\r\n" + 
				"  \"category\": {\r\n" + 
				"    \"id\": 11,\r\n" + 
				"    \"name\": \"updatePet\"\r\n" + 
				"  },\r\n" + 
				"  \"name\": \"rat\",\r\n" + 
				"  \"photoUrls\": [\r\n" + 
				"    \"string\"\r\n" + 
				"  ],\r\n" + 
				"  \"tags\": [\r\n" + 
				"    {\r\n" + 
				"      \"id\": 0,\r\n" + 
				"      \"name\": \"pretty\"\r\n" + 
				"    }\r\n" + 
				"  ],\r\n" + 
				"  \"status\": \"available\"\r\n" + 
				"}";
		Response res = RestAssured.given().contentType(ContentType.JSON).body(petInfo).put("/pet");
		Assert.assertEquals(res.getStatusCode(),200);
		Assert.assertTrue(res.getBody().asString().contains("cat"));
		
		identity = res.jsonPath().getInt("id");
		
	}
	
	@Test(priority=3)	
	public void firstTest()
	{
		Response res = RestAssured.given().pathParam("id", identity).get("/pet/{id}");
		res.then().log().all();
		Assert.assertEquals(res.getStatusCode(),200);
		Assert.assertTrue(res.getBody().asString().contains("rat"));
	}
	
	
	
	@Test(priority=4)	
	public void deleteTest()
	{
		Response res = RestAssured.given().delete("/pet/5000");
		res.then().log().ifError();
		res.then().log().ifStatusCodeIsEqualTo(404);
		Assert.assertEquals(res.getStatusCode(),404);
	}
	
	@Test(priority=5)	
	public void queryTest1()
	{
		Response res = RestAssured.given().log().all().baseUri("https://reqres.in/api").queryParam("page", 2).queryParam("id", 5).get("/users");
	
	res.then().log().all();
	}
	
	
	@Test(priority=6)	
	public void queryTest()
	{
		Response res = RestAssured.given().log().all().queryParam("username", "amangarg").queryParam("password", "test1234").get("/user/login");
	     res.then().log().all();
	     Assert.assertEquals(res.getStatusCode(), 200);
	    
	}
	
	@Test(priority=7)	
	public void createUser() {
		User user = new User(20, "testing", "aman", "garg", "aman@123", "aman@test.com", "236363636", 1);
		RequestSpecification request = RestAssured.given().log().all().contentType(ContentType.JSON).body(user);

		Response res = request.post("/user");

		res.then().log().all();

		UserResponse re = res.getBody().as(UserResponse.class);

		Assert.assertEquals(re.getCode(), 200);
		Assert.assertEquals(re.getType(), "unknown");
		Assert.assertEquals(re.getMessage(), "20");
	}

	@Test(priority=8)	
	public void createUser1() {
		String testData = readFile("user");
		
	
		RequestSpecification request = RestAssured.given().log().all().contentType(ContentType.JSON).body(testData);

		Response res = request.post("/user");

		res.then().log().all();

	}

	public String readFile(String fileName) {
		String rootPath = System.getProperty("user.dir");
		String completePath = rootPath + "//src//test//resources//testdata//" + fileName + ".json";
		try {
			return new String(Files.readAllBytes(Paths.get(completePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	 @Test(priority=9)	
	public void basicAuthenticaion() {
		RequestSpecification request = RestAssured.given().baseUri("http://restapi.demoqa.com/authentication").auth()
				.basic("ToolsQA", "TestPassword");

		request.log().all();
		Response res = request.get("/CheckForAuthentication");
		res.prettyPrint();
	}

	@Test(priority=10)	
	public void oAuthOneTest() {
		Response res = RestAssured.given().log().all().baseUri("https://api.twitter.com/1.1/statuses").auth()
				.oauth("tpm97q7gPXm4g3EA0oUmtmBCq", "UyFugasDCBN9HfaT0uDe9Sbtvs5Vdh6LfznQxAqFhwgUTQACaT",
						"945645824456953856-ZIjXUDRrzQ4dGSKkT294tvqcIZy1GKl",
						"RY0CXsAwTWDmsuPJGV6QOJJ1XwjkSYuTL8pYUNVYk8b4v")
				.queryParam("status", "This tweet was created using rest assured 1761656768").post("/update.json");
		res.prettyPrint();
		Assert.assertEquals(res.getStatusCode(), 200);
		
	}

	//Change your client id and client secret in basic method for paypal account
		//use this url to sign up on paypal 
		//https://developer.paypal.com/classic-home/
		//sign up and provide your info and don't link card  and hit the url again on providing details.
		//create an app and take ur client id and secret
	@Test(priority=11)	
	public void getAccessTokenViaPreemptiveAuthentication() {
		RestAssured.baseURI = "https://api.sandbox.paypal.com";
		RestAssured.basePath = "v1";
		Response res = RestAssured.given().log().all().param("grant_type", "client_credentials").auth().preemptive()
				.basic("AV80Zx7Ho_dxiwz3XGF5Oe3o-LIY8SgNWDDOhaAktrlfdqShKX4GsSMrS5-BPIWtYTrIK_jup2I7dyC7",
						"EIx3rVK8J7fJmIqy6ap916ZZO4Qo7PwEtT0dOqh7FbvzEgmXcB3JO0ab0x6yQ22LPk3BRIfwjI-drvdQ")
				.post("/oauth2/token");
		res.prettyPrint();
		accessToken = res.jsonPath().getString("access_token");
		Assert.assertEquals(res.getStatusCode(), 200);
		
		
	}

	@Test(priority=12)	
	public void doingPaymentWithOauth2Autentication()
	{
	RestAssured.baseURI = "https://api.sandbox.paypal.com";
	RestAssured.basePath = "v1";
		String filePath = root+ "\\src\\test\\resources\\testdata\\payment.json";
		File file = new File(filePath);
		
		Response res = RestAssured.given().baseUri("https://api.sandbox.paypal.com/v1").contentType(ContentType.JSON).log().all().auth().oauth2(accessToken).body(file).post("/payments/payment");
		
		res.prettyPrint();
		
	}
	
	@Test(priority=13)	
	public void uploadTest()
	{
	RestAssured.baseURI = "http://petstore.swagger.io";
	RestAssured.basePath= "/v2";
	String endPoint = "/pet/"+identity+"/uploadImage";
		File testUploadFile = new File(root + "\\src\\test\\resources\\schematest.json");
		Response res = RestAssured.given().log().all().formParam("additionalMetadata", "testing").multiPart(testUploadFile).post(endPoint);
		Assert.assertEquals(res.getStatusCode(),200);
		res.prettyPrint();
	}
	
	@Test
	public void uploadTest1()
	{
		RestAssured.baseURI = "http://petstore.swagger.io";
		RestAssured.basePath= "/v2";
		String endPoint = "/pet/52/uploadImage";
		File fileName = new File(root+ "\\src\\test\\resources\\testdata\\user.json");
		Response res = RestAssured.given().log().all().formParam("additionalMetadata", "testing the file").multiPart(fileName).post(endPoint);
	    res.prettyPrint();
	}

	// You need to convert your response to bytearray[] from file in case of download.
	// No Other change in download request .. simply hit the endpoint and convert your response to bytearray.
//	String response = r.getBody().asByteArray().toString();
//    Assert.assertTrue(response.contains(""));
	
}
