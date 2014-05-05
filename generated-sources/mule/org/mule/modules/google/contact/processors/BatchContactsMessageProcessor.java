
package org.mule.modules.google.contact.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.config.ConfigurationException;
import org.mule.api.devkit.NestedProcessorChain;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.RegistrationException;
import org.mule.common.DefaultResult;
import org.mule.common.FailureType;
import org.mule.common.Result;
import org.mule.common.metadata.ConnectorMetaDataEnabled;
import org.mule.common.metadata.DefaultListMetaDataModel;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultPojoMetaDataModel;
import org.mule.common.metadata.DefaultSimpleMetaDataModel;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.OperationMetaDataEnabled;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.datatype.DataTypeFactory;
import org.mule.modules.google.api.domain.BatchResult;
import org.mule.modules.google.contact.GoogleContactsConnector;
import org.mule.modules.google.contact.oauth.GoogleContactsConnectorOAuthManager;
import org.mule.modules.google.oauth.invalidation.OAuthTokenExpiredException;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * BatchContactsMessageProcessor invokes the {@link org.mule.modules.google.contact.GoogleContactsConnector#batchContacts(java.lang.String, java.util.List)} method in {@link GoogleContactsConnector }. For each argument there is a field in this processor to match it.  Before invoking the actual method the processor will evaluate and transform where possible to the expected argument type.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-16T09:31:06-05:00", comments = "Build master.1915.dd1962d")
public class BatchContactsMessageProcessor
    extends AbstractConnectedProcessor
    implements MessageProcessor, OperationMetaDataEnabled
{

    protected Object batchId;
    protected String _batchIdType;
    protected Object operations;
    protected List<NestedProcessor> _operationsType;

    public BatchContactsMessageProcessor(String operationName) {
        super(operationName);
    }

    /**
     * Obtains the expression manager from the Mule context and initialises the connector. If a target object  has not been set already it will search the Mule registry for a default one.
     * 
     * @throws InitialisationException
     */
    public void initialise()
        throws InitialisationException
    {
        if (operations instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) operations)) {
                if (messageProcessor instanceof Initialisable) {
                    ((Initialisable) messageProcessor).initialise();
                }
            }
        }
    }

    @Override
    public void start()
        throws MuleException
    {
        super.start();
        if (operations instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) operations)) {
                if (messageProcessor instanceof Startable) {
                    ((Startable) messageProcessor).start();
                }
            }
        }
    }

    @Override
    public void stop()
        throws MuleException
    {
        super.stop();
        if (operations instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) operations)) {
                if (messageProcessor instanceof Stoppable) {
                    ((Stoppable) messageProcessor).stop();
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (operations instanceof List) {
            for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) operations)) {
                if (messageProcessor instanceof Disposable) {
                    ((Disposable) messageProcessor).dispose();
                }
            }
        }
    }

    /**
     * Sets batchId
     * 
     * @param value Value to set
     */
    public void setBatchId(Object value) {
        this.batchId = value;
    }

    /**
     * Sets operations
     * 
     * @param value Value to set
     */
    public void setOperations(Object value) {
        this.operations = value;
    }

    /**
     * Invokes the MessageProcessor.
     * 
     * @param event MuleEvent to be processed
     * @throws Exception
     */
    public MuleEvent doProcess(final MuleEvent event)
        throws Exception
    {
        Object moduleObject = null;
        try {
            moduleObject = findOrCreate(GoogleContactsConnectorOAuthManager.class, false, event);
            final String _transformedBatchId = ((String) evaluateAndTransform(getMuleContext(), event, BatchContactsMessageProcessor.class.getDeclaredField("_batchIdType").getGenericType(), null, batchId));
            final List<NestedProcessor> _transformedOperations = new ArrayList<NestedProcessor>();
            if (operations!= null) {
                for (MessageProcessor messageProcessor: ((List<MessageProcessor> ) operations)) {
                    _transformedOperations.add(new NestedProcessorChain(event, getMuleContext(), messageProcessor));
                }
            }
            Object resultPayload;
            final ProcessTemplate<Object, Object> processTemplate = ((ProcessAdapter<Object> ) moduleObject).getProcessTemplate();
            resultPayload = processTemplate.execute(new ProcessCallback<Object,Object>() {


                public List<Class<? extends Exception>> getManagedExceptions() {
                    return Arrays.asList(((Class<? extends Exception> []) new Class[] {OAuthTokenExpiredException.class }));
                }

                public boolean isProtected() {
                    return true;
                }

                public Object process(Object object)
                    throws Exception
                {
                    return ((GoogleContactsConnector) object).batchContacts(_transformedBatchId, _transformedOperations);
                }

            }
            , this, event);
            event.getMessage().setPayload(resultPayload);
            return event;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Result<MetaData> getInputMetaData() {
        return new DefaultResult<MetaData>(new DefaultMetaData(new DefaultListMetaDataModel(getPojoOrSimpleModel(NestedProcessor.class))));
    }

    @Override
    public Result<MetaData> getOutputMetaData(MetaData inputMetadata) {
        return new DefaultResult<MetaData>(new DefaultMetaData(new DefaultListMetaDataModel(getPojoOrSimpleModel(BatchResult.class))));
    }

    private MetaDataModel getPojoOrSimpleModel(Class clazz) {
        DataType dataType = DataTypeFactory.getInstance().getDataType(clazz);
        if (DataType.POJO.equals(dataType)) {
            return new DefaultPojoMetaDataModel(clazz);
        } else {
            return new DefaultSimpleMetaDataModel(dataType);
        }
    }

    public Result<MetaData> getGenericMetaData(MetaDataKey metaDataKey) {
        ConnectorMetaDataEnabled connector;
        try {
            connector = ((ConnectorMetaDataEnabled) findOrCreate(GoogleContactsConnector.class, true, null));
            try {
                Result<MetaData> metadata = connector.getMetaData(metaDataKey);
                if ((Result.Status.FAILURE).equals(metadata.getStatus())) {
                    return metadata;
                }
                if (metadata.get() == null) {
                    return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), "There was an error processing metadata at GoogleContactsConnector at batchContacts retrieving was successful but result is null");
                }
                return metadata;
            } catch (Exception e) {
                return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
            }
        } catch (ClassCastException cast) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), "There was an error getting metadata, there was no connection manager available. Maybe you're trying to use metadata from an Oauth connector");
        } catch (ConfigurationException e) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
        } catch (RegistrationException e) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
        } catch (IllegalAccessException e) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
        } catch (InstantiationException e) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
        } catch (Exception e) {
            return new DefaultResult<MetaData>(null, (Result.Status.FAILURE), e.getMessage(), FailureType.UNSPECIFIED, e);
        }
    }

}