/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     hhoyos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.standalone;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.EolOperation;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.standalone.util.ExecutionException;
import org.eclipse.epsilon.standalone.util.ParseException;
import org.eclipse.epsilon.standalone.util.SourceLoadException;

/**
 * The Class EolStandaloneEngine.
 * 
 * @author Horacio Hoyos
 */
public class EolStandaloneEngine extends EpsilonStandaloneEngine {
	
	
	/**
	 * Instantiates a new EOL Standalone engine.
	 *
	 * @param eolSourceURI the EOL source URI
	 */
	public EolStandaloneEngine(URI eolSourceURI) {
		super();
		this.sourceURI = eolSourceURI;
		models = new ArrayList<IModel>();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.riskc.riskc.generator.epsilon.EpsilonStandalone#createModule()
	 */
	@Override
	public EolModule createModule() {
		return new EolModule();
	}
	

	/* (non-Javadoc)
	 * @see net.sourceforge.riskc.riskc.generator.epsilon.EpsilonStandalone#getModels()
	 */
	@Override
	public List<IModel> getModels() {
		return models;
	}
	
	/**
	 * Prepares the EOL engine's environment by parsing the source and loading
	 * the models. This method should be called before invoking {@link #executeOperation(String)}
	 * when invoking specific operations in the EOL source. When using this
	 * method, a call to the {@link close()} method should be made when no
	 * more operations are being executed.
	 *
	 * @throws SourceLoadException If there was an error loading the source file
	 * 		   or during initial source parsing.
	 * @throws ParseException If syntax errors were detected in the source file.
	 *         Error details will be printed in the System.err stream.
	 */
	public void prepare() throws SourceLoadException, ParseException  {
		
		module = createModule();
		try {
			module.parse(sourceURI);
		} catch (Exception e1) {
			throw new SourceLoadException("There was an error loading the source.", e1.getCause());
		}
		
		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			throw new ParseException("Parse errors occured. See stack trace for details.");
		}
		
		for (IModel model : getModels()) {
			module.getContext().getModelRepository().addModel(model);
		}
		
	}
	
	/**
	 * When executing specific operations ({@link executeOperation(String)}) 
	 * this method disposes the models. This method should be called
	 * when no more operations are being executed.
	 */
	public void close() {
		
		module.getContext().getModelRepository().dispose();
	}
	
	
	/**
	 * Execute a specific operation, identified by the operationName parameter.
	 * {@link #prepare()} should be invoked (once) before calling this method 
	 * (and the method can then be called repeatedly). The result of executing
	 * the operation in the loaded model(s) is stored in the {@see #result} field.
	 * 	
	 * @param operationName The name of the operation in the EOL source
	 * @throws ExecutionException If there was an error executing the operation 
	 */
	public void executeOperation(String operationName) throws ExecutionException {
		
		preProcess();
		result = executeOperation(module, operationName);
		postProcess();
	}
	

	/**
	 * Executes the first operation found in the EOL source. To invoke a specific
	 * operation use {@link #executeOperation(IEolExecutableModule, String)}.
	 * The result of the operation will be stored in the result field.
	 *
	 * @throws ParseException If syntax errors were detected in the EOL source file.
	 *         Error details will be printed in the System.err stream.
	 * @throws SourceLoadException If there was an error loading the EOL source.
	 * @throws ExecutionException If there was an error executing the operation.
	 */
	@Override
	public void execute() throws ParseException, SourceLoadException, ExecutionException {
		prepare();
		EolOperation operation = module.getDeclaredOperations().get(0);
		preProcess();
		try {
			result = operation.execute(null, Collections.EMPTY_LIST, module.getContext());
		} catch (EolRuntimeException e) {
			throw new ExecutionException(e.getMessage(),e.getCause());
		}
		postProcess();
		
		module.getContext().getModelRepository().dispose();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.riskc.riskc.generator.epsilon.EpsilonStandalone#postProcess()
	 */
	@Override
	public void postProcess() {
		System.out.println(result);
	}
	
	/**
	 * Invokes the actual execution of an operation in the EOL source.  
	 *
	 * @param module the module
	 * @param operationName the operation name
	 * @return the object
	 * @throws ExecutionException the execution exception
	 */
	private Object executeOperation(IEolExecutableModule module, String operationName) throws ExecutionException {
		EolOperation operation = module.getDeclaredOperations().getOperation(operationName);
		if(operation != null) {
			try {
				return operation.execute(null, Collections.EMPTY_LIST, module.getContext());
			} catch (EolRuntimeException e) {
				e.printStackTrace();
				throw new ExecutionException(e.getMessage());
			}
		}
		return null;
	}

}
