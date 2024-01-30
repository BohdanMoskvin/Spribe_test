package api.player;

import io.restassured.response.Response;

public interface Request {
  Response sendRequest(RequestDto requestDto, int ExpectedStatusCode);
}
