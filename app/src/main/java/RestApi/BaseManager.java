package RestApi;

public class BaseManager {
    protected RestApi getRestApiClient(){
        RestApiClient restApi = new RestApiClient(BaseUrl.mainUrl);
        return  restApi.getRestApi();
    }
}