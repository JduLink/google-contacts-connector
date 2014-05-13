
package org.mule.modules.google.contact.oauth;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.google.contact.adapters.GoogleContactsConnectorOAuth2Adapter;
import org.mule.modules.google.contact.connection.UnableToAcquireConnectionException;
import org.mule.modules.google.contact.connection.UnableToReleaseConnectionException;
import org.mule.modules.google.contact.process.ProcessCallback;
import org.mule.modules.google.contact.process.ProcessInterceptor;
import org.mule.modules.google.contact.processors.AbstractConnectedProcessor;
import org.mule.modules.google.contact.processors.AbstractExpressionEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.4.3", date = "2014-03-26T12:38:00-05:00", comments = "Build 3.4.3.1620.30ea288")
public class ManagedAccessTokenProcessInterceptor<T >
    extends AbstractExpressionEvaluator
    implements ProcessInterceptor<T, GoogleContactsConnectorOAuth2Adapter>
{

    private static Logger logger = LoggerFactory.getLogger(ManagedAccessTokenProcessInterceptor.class);
    private final OAuthManager<GoogleContactsConnectorOAuth2Adapter> oauthManager;
    private final MuleContext muleContext;
    private final ProcessInterceptor<T, GoogleContactsConnectorOAuth2Adapter> next;

    public ManagedAccessTokenProcessInterceptor(ProcessInterceptor<T, GoogleContactsConnectorOAuth2Adapter> next, OAuthManager<GoogleContactsConnectorOAuth2Adapter> oauthManager, MuleContext muleContext) {
        this.next = next;
        this.oauthManager = oauthManager;
        this.muleContext = muleContext;
    }

    public T execute(ProcessCallback<T, GoogleContactsConnectorOAuth2Adapter> processCallback, GoogleContactsConnectorOAuth2Adapter object, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        GoogleContactsConnectorOAuth2Adapter connector = null;
        if (!processCallback.isProtected()) {
            return processCallback.process(oauthManager.getDefaultUnauthorizedConnector());
        }
        if (((AbstractConnectedProcessor) messageProcessor).getAccessTokenId() == null) {
            throw new IllegalArgumentException("The accessTokenId cannot be null");
        }
        String _transformedToken = ((String) evaluateAndTransform(muleContext, event, AbstractConnectedProcessor.class.getDeclaredField("_accessTokenIdType").getGenericType(), null, ((AbstractConnectedProcessor) messageProcessor).getAccessTokenId()));
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(("Attempting to acquire access token using from store for [accessTokenId="+ _transformedToken.toString()));
            }
            connector = oauthManager.acquireAccessToken(_transformedToken);
            if (connector == null) {
                throw new UnableToAcquireConnectionException();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug((("Access token has been acquired for [accessTokenId="+ connector.getAccessTokenId())+"]"));
                }
            }
            return next.execute(processCallback, connector, messageProcessor, event);
        } catch (Exception e) {
            if ((processCallback.getManagedExceptions()!= null)&&(connector!= null)) {
                for (Class exceptionClass: ((List<Class> ) processCallback.getManagedExceptions())) {
                    if (exceptionClass.isInstance(e)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug((((("An exception ( "+ exceptionClass.getName())+") has been thrown. Destroying the access token with [accessTokenId=")+ connector.getAccessTokenId())+"]"));
                        }
                        try {
                            oauthManager.destroyAccessToken(_transformedToken, connector);
                            connector = null;
                        } catch (Exception innerException) {
                            logger.error(innerException.getMessage(), innerException);
                        }
                    }
                }
            }
            throw e;
        } finally {
            try {
                if (connector!= null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug((("Releasing the access token back into the pool [accessTokenId="+ connector.getAccessTokenId())+"]"));
                    }
                    oauthManager.releaseAccessToken(_transformedToken, connector);
                }
            } catch (Exception e) {
                throw new UnableToReleaseConnectionException(e);
            }
        }
    }

    public T execute(ProcessCallback<T, GoogleContactsConnectorOAuth2Adapter> processCallback, GoogleContactsConnectorOAuth2Adapter object, Filter filter, MuleMessage message)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

}