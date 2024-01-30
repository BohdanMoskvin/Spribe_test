package api.player;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class AbstractRequest implements Request {
  private final RequestSpecification rs;
  private final String apiBaseUrl;
  private final String endpoint;

  public AbstractRequest(String endpoint) {
    this(ApiBaseUrl.URL, endpoint);
  }

  public AbstractRequest(String apiBaseUrl, String endpoint) {
    this.apiBaseUrl = apiBaseUrl;
    this.endpoint = endpoint;

    rs = new RequestSpecBuilder().setContentType(ContentType.JSON).log(LogDetail.ALL).build();
  }

  public RequestSpecification given() {
    return RestAssured.given().spec(rs).baseUri(apiBaseUrl).basePath(endpoint);
  }
}
