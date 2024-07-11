import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;

public class MultitenantClient {
    private final String  tenantName;
    private final Integer apiId;
    private final String  apiHash;
    private final String  phoneNumber;
    private final Client  client;

    public MultitenantClient(String tenantName) {
        this.tenantName  = tenantName;
        this.apiId       = Integer.parseInt(System.getenv(String.format("%s_API_ID", tenantName.toUpperCase())));
        this.apiHash     = System.getenv(String.format("%s_API_HASH", tenantName.toUpperCase()));
        this.phoneNumber = System.getenv(String.format("%s_PHONE_NUMBER", tenantName.toUpperCase()));;
        this.client      = Client.create(new MyResultHandler(), null, null);
    }

    void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState, final Client.ResultHandler resultHandler) {
            switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
                request.databaseDirectory   = String.format("tdlib/%s", tenantName);
                request.useChatInfoDatabase = true;
                request.useMessageDatabase  = true;
                request.apiId               = apiId;
                request.apiHash             = apiHash;
                request.systemLanguageCode  = "es";
                request.deviceModel         = "Linux";
                request.applicationVersion  = "0.1.0-dev";
                client.send(request, resultHandler);
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                client.send(new TdApi.SetAuthenticationPhoneNumber(phoneNumber, null), resultHandler);
                System.out.println("Waiting for authentication code...");
                break;
            case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                break;
        }
    }

    private class MyResultHandler implements Client.ResultHandler {
        public void onResult(TdApi.Object obj) {
            switch (obj.getConstructor()) {
                case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                    onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState)obj).authorizationState, this);
                    break;
                case TdApi.Error.CONSTRUCTOR:
                    System.err.println("Receive an error: " + obj);
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                default:
                    break;
            }
        }
    }
}


