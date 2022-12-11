package Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EntToEndTest {
	
	Response response;
	RequestSpecification request;
	String baseURI = "http://localhost:3000";
	Map<String,Object> MapObj;
	JsonPath jpath;
	
	@Test
	public void Test() {
		
		//RequestSpecification request;
				
		System.out.println("This is to GET all employees");
		response = GETAllEmployees();
		Assert.assertEquals(200, response.getStatusCode());
				
		System.out.println("This is to CREATE employees");
		response = CreateEmployee("John", 8000);
		Assert.assertEquals(201, response.getStatusCode());
		jpath = response.jsonPath();
		int uid = jpath.get("id");
			 
		
		System.out.println("This is to GET single Employee");
		response = GetSingleEmployee(uid);
		Assert.assertEquals(200, response.getStatusCode());
		
		
		System.out.println("This is to UPDATE employees");
		response = UpdateEmployee(uid, "Smith", 9000);
		Assert.assertEquals(200, response.getStatusCode());
		
		
		System.out.println("This is to Check employee name after update Employee");
		response = GetSingleEmployee(uid);
		jpath = response.jsonPath();		
		//Assert.assertEquals(jpath.get("name"), "Smith");		
		List<String> names = jpath.get("name");
		System.out.println("The name is: " + names.get(0));
		Assert.assertEquals(names.get(0), "Smith");
		
		
		System.out.println("This is to DELETE employees");
		response = DeleteEmployee(uid);
		Assert.assertEquals(200, response.getStatusCode());
		
		System.out.println("This is to Check employees after delete operation");
		response = GetSingleEmployee(uid);
		Assert.assertEquals(200, response.getStatusCode());

	}
	
	public Response GETAllEmployees() {
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		
		response = request.get("employees");
		
		System.out.println(response.getBody().asString());
		
		return response;		
	}
	
	public Response GetSingleEmployee(int uid) {
		
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		
		response = request.param("id", uid).get("employees");
		
		System.out.println(response.getBody().asString());
/*		
		String ResponseBody = response.getBody().asString();
		Assert.assertTrue(ResponseBody.contains(uname));
*/		
		return response;				
	}
	
	public Response CreateEmployee (String name, int salary){
		
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		MapObj = new HashMap<String,Object>();
		
		MapObj.put("name", name);
		MapObj.put("salary", salary);
				
		response = request
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.body(MapObj)
					.post("employees/create");
		
		System.out.println(response.getBody().asString());
		
		return response;		
	}

	public Response UpdateEmployee (int uid, String name, int salary){
		
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		MapObj = new HashMap<String,Object>();
		
		MapObj.put("name", name);
		MapObj.put("salary", salary);
				
		response = request
					.contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.body(MapObj)
					.put("employees/"+uid);
		
		System.out.println(response.getBody().asString());
		
		return response;		
	}
	
	public Response DeleteEmployee (int uid){
		
		RestAssured.baseURI = this.baseURI;
		request = RestAssured.given();
		
		response = request.delete("employees/"+ uid);
		
		System.out.println(response.getBody().asString());
		
		return response;		
	}
}
