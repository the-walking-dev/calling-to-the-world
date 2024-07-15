package imclient.telegram;

import java.util.logging.Logger;

import core.Event;
import imclient.IMClient;
import org.drinkless.tdlib.Client;

import static org.drinkless.tdlib.Client.create;
import static org.drinkless.tdlib.Client.ResultHandler;
import static org.drinkless.tdlib.TdApi.Object;
import static org.drinkless.tdlib.TdApi.CheckAuthenticationCode;
import static org.drinkless.tdlib.TdApi.SetTdlibParameters;
import static org.drinkless.tdlib.TdApi.SetAuthenticationPhoneNumber;
import static org.drinkless.tdlib.TdApi.UpdateAuthorizationState;
import static org.drinkless.tdlib.TdApi.AuthorizationState;
import static org.drinkless.tdlib.TdApi.AuthorizationStateWaitCode;
import static org.drinkless.tdlib.TdApi.AuthorizationStateWaitTdlibParameters;
import static org.drinkless.tdlib.TdApi.AuthorizationStateWaitPhoneNumber;
import static org.drinkless.tdlib.TdApi.AuthorizationStateReady;
import static org.drinkless.tdlib.TdApi.Error;
import static org.drinkless.tdlib.TdApi.Ok;

public class TelegramIMClient implements IMClient {
    private static final Logger logger = Logger.getLogger("multitenant-poc-logger");

    private final String  tenantName;
    private final Integer apiId;
    private final String  apiHash;
    private final String  phoneNumber;
    private final Client client;

    private boolean waitingCode = false;

    public TelegramIMClient(String tenantName) {
        this.tenantName  = tenantName;
        this.apiId       = Integer.parseInt(System.getenv(String.format("%s_API_ID", tenantName.toUpperCase())));
        this.apiHash     = System.getenv(String.format("%s_API_HASH", tenantName.toUpperCase()));
        this.phoneNumber = System.getenv(String.format("%s_PHONE_NUMBER", tenantName.toUpperCase()));;
        this.client      = create(new MyResultHandler(), null, null);
    }

    public String tenantName() {
        return tenantName;
    }

    public void sendAuthCode(String code) {
        if (waitingCode) {
            client.send(new CheckAuthenticationCode(code), obj -> logger.info(String.format("logged? %t", obj.getConstructor() == Ok.CONSTRUCTOR)));
        } else {
            logger.info(() -> String.format("%s sending auth code not waiting for it\n", tenantName()));
        }
    }

    private void onAuthorizationStateUpdated(AuthorizationState authorizationState, final ResultHandler resultHandler) {
            waitingCode = authorizationState instanceof AuthorizationStateWaitCode;
            switch (authorizationState.getConstructor()) {
            case AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                var request = new SetTdlibParameters();
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
            case AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                client.send(new SetAuthenticationPhoneNumber(phoneNumber, null), resultHandler);
                logger.info(() -> String.format("%s Waiting for authentication code", tenantName()));
                break;
            case AuthorizationStateReady.CONSTRUCTOR:
                logger.info(() -> String.format("%s authorization ready", tenantName()));
                break;
        }
    }

    private class MyResultHandler implements ResultHandler {
        public void onResult(Object obj) {
            switch (obj.getConstructor()) {
                case UpdateAuthorizationState.CONSTRUCTOR:
                    onAuthorizationStateUpdated(((UpdateAuthorizationState)obj).authorizationState, this);
                    break;
                case Error.CONSTRUCTOR:
                    logger.info(() -> String.format("%s Receive an error: %v", tenantName()));
                    break;
                case Ok.CONSTRUCTOR:
                    var event = new Event() {};
                    publish(event);
                    break;
                default:
                    break;
            }
        }
    }
}
