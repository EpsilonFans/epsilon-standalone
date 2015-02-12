package org.eclipse.epsilon.standalone;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;
import org.eclipse.epsilon.etl.execute.context.EtlContext;
import org.eclipse.epsilon.etl.trace.TransformationTrace;
import org.eclipse.epsilon.standalone.util.ExecutionException;

public class EtlStandaloneEngine extends EpsilonStandaloneEngine {

	
	public EtlStandaloneEngine(URI etlSourceURI) {
		super();
		this.sourceURI = etlSourceURI;
		models = new ArrayList<IModel>();
	}

	public EtlStandaloneEngine(String etlSourcePath) {
		super();
		this.sourceURI = URI.create(etlSourcePath);
		models = new ArrayList<IModel>();
	}

	@Override
	public IEolExecutableModule createModule() {
		
		return new EtlModule();
	}
	
	/**
	 * Gets the transformation trace. This method should be called after executing 
	 * and getting its result.
	 *
	 * @throws ExecutorException if no previously execution.
	 */
	
	public TransformationTrace getTransformationTrace() throws ExecutionException {
		
		if( module == null) {
			throw new ExecutionException("Transformation not Executed. No context found.");
		}
		EtlContext context = (EtlContext) module.getContext();
		return context.getTransformationTrace();
	}


}
