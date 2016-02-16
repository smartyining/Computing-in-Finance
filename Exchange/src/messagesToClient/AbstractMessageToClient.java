package messagesToClient;

import clientMessageFields.ClientId;
import clientMessageFields.ClientMessageId;
import exchange.Client;
import exchangeMessageFields.ExchangeMessageType;

public abstract class AbstractMessageToClient {

	protected ClientId            _clientId;
	protected ClientMessageId     _clientMessageId;
	protected ExchangeMessageType _exchangeMessageType;
	
	public AbstractMessageToClient (
		ClientId            clientId,
		ClientMessageId     clientMessageId,
		ExchangeMessageType exchangeMessageType	
	) {
		_clientId            = clientId;
		_clientMessageId     = clientMessageId;
		_exchangeMessageType = exchangeMessageType;
	}

	public ClientId getClientId() {
		return _clientId;
	}

	public ClientMessageId getClientMessageId() {
		return _clientMessageId;
	}

	public ExchangeMessageType getMessageType() {
		return _exchangeMessageType;
	}
	
	public abstract void getProcessedBy( Client client );
	
}
