/*
Copyright (c) since 2015, Tel Aviv University and Software Modeling Lab

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of Tel Aviv University and Software Modeling Lab nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Tel Aviv University and Software Modeling Lab 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

package tau.smlab.syntech.spectragameinput.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tau.smlab.syntech.gameinput.model.Define;
import tau.smlab.syntech.gameinput.model.Predicate;
import tau.smlab.syntech.gameinput.model.SpecRegExp;
import tau.smlab.syntech.gameinput.model.SpecRegExp.Quantifier;
import tau.smlab.syntech.gameinput.model.Trigger;
import tau.smlab.syntech.gameinput.model.Variable;
import tau.smlab.syntech.gameinput.spec.CounterReference;
import tau.smlab.syntech.gameinput.spec.DefineReference;
import tau.smlab.syntech.gameinput.spec.MonitorReference;
import tau.smlab.syntech.gameinput.spec.Operator;
import tau.smlab.syntech.gameinput.spec.PredicateInstance;
import tau.smlab.syntech.gameinput.spec.PrimitiveValue;
import tau.smlab.syntech.gameinput.spec.QuantifiedSpec;
import tau.smlab.syntech.gameinput.spec.Spec;
import tau.smlab.syntech.gameinput.spec.SpecExp;
import tau.smlab.syntech.gameinput.spec.SpecHelper;
import tau.smlab.syntech.gameinput.spec.SpecWrapper;
import tau.smlab.syntech.gameinput.spec.VariableReference;
import tau.smlab.syntech.spectra.AnotherValue;
import tau.smlab.syntech.spectra.BinaryRegExp;
import tau.smlab.syntech.spectra.Constant;
import tau.smlab.syntech.spectra.Counter;
import tau.smlab.syntech.spectra.DefineDecl;
import tau.smlab.syntech.spectra.DomainVarDecl;
import tau.smlab.syntech.spectra.Monitor;
import tau.smlab.syntech.spectra.PatternParam;
//import tau.smlab.syntech.spectra.PrimaryRegExp;
import tau.smlab.syntech.spectra.QuantifierExpr;
import tau.smlab.syntech.spectra.Referrable;
import tau.smlab.syntech.spectra.RegExp;
import tau.smlab.syntech.spectra.TemporalAdditiveExpr;
import tau.smlab.syntech.spectra.TemporalAndExpr;
import tau.smlab.syntech.spectra.TemporalBinaryExpr;
import tau.smlab.syntech.spectra.TemporalExpression;
import tau.smlab.syntech.spectra.TemporalIffExpr;
import tau.smlab.syntech.spectra.TemporalImpExpr;
import tau.smlab.syntech.spectra.TemporalMultiplicativeExpr;
import tau.smlab.syntech.spectra.TemporalOrExpr;
import tau.smlab.syntech.spectra.TemporalPrimaryExpr;
import tau.smlab.syntech.spectra.TemporalRelationalExpr;
import tau.smlab.syntech.spectra.TemporalRemainderExpr;
import tau.smlab.syntech.spectra.TemporalUnaryExpr;
import tau.smlab.syntech.spectra.TypeConstant;
import tau.smlab.syntech.spectra.TypedParam;
import tau.smlab.syntech.spectra.VarDecl;
import tau.smlab.syntech.spectra.VarType;
import tau.smlab.syntech.spectragameinput.SpectraTranslationException;
import tau.smlab.syntech.typesystem.TypeSystemUtils;

public class SpectraASTToSpecGenerator {
  /**
   * 
   * @param exp
   * @param entitiesMapper
   * @param tracer
   * @param predicateParamsList
   *          pass null if it's not a predicate
   * @param patternVarsAndParams
   *          pass null if it's not a pattern
   * @return
   * @throws SpectraTranslationException 
   */
  static SpecWrapper getConstraintSpec(TemporalExpression exp, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {

    if (exp instanceof TemporalPrimaryExpr) {
      return getConstraintSpec((TemporalPrimaryExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof QuantifierExpr) {
      return getConstraintSpec((QuantifierExpr)exp, entitiesMapper, tracer, predicateParamsList, 
    	  patternVarsAndParams);
    } else if (exp instanceof TemporalImpExpr) {
      return getConstraintSpec((TemporalImpExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalIffExpr) {
      return getConstraintSpec((TemporalIffExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalOrExpr) {
      return getConstraintSpec((TemporalOrExpr) exp, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);
    } else if (exp instanceof TemporalAndExpr) {
      return getConstraintSpec((TemporalAndExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalRelationalExpr) {
      return getConstraintSpec((TemporalRelationalExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalRemainderExpr) {
      return getConstraintSpec((TemporalRemainderExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalAdditiveExpr) {
      return getConstraintSpec((TemporalAdditiveExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalMultiplicativeExpr) {
      return getConstraintSpec((TemporalMultiplicativeExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalBinaryExpr) {
      return getConstraintSpec((TemporalBinaryExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof TemporalUnaryExpr) {
      return getConstraintSpec((TemporalUnaryExpr) exp, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    } else if (exp instanceof Constant) {
      return getConstraintSpec((Constant) exp, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);
    } else if (exp instanceof tau.smlab.syntech.spectra.Trigger) {
      return getConstraintSpec((tau.smlab.syntech.spectra.Trigger) exp, entitiesMapper, tracer);
    }

    // doesn't match any TemporalExpression subclass
    throw new RuntimeException("Unhaldled class in Specta AST translation: " + exp.getClass().getCanonicalName());
  }
  
  static SpecWrapper getConstraintSpec(QuantifierExpr exp, EntitiesMapper entitiesMapper, Tracer tracer,
          List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
       
      //get the spec of exp's temporal expression
      SpecWrapper spec = getConstraintSpec(exp.getTemporalExpr(), entitiesMapper, tracer, predicateParamsList,
                patternVarsAndParams);
       
      //get the operator of exp (forall or exists)
      Operator op = null;
      if (exp.getOperator().equals("forall")) {
          op = Operator.FORALL;
      } else if(exp.getOperator().contentEquals("exists")) {
          op = Operator.EXISTS;
      } else {
          //unsupported operator
          throw new RuntimeException(exp.getClass().getCanonicalName());
      }
       
      //get exp's domain var from quantifierVars list
//      Variable variable = findDomainVar(exp.getDomainVar().getName());  
      Variable variable = new Variable(exp.getDomainVar().getName(), 
    		  Spectra2GameInputTranslator.getTypeDef(exp.getDomainVar().getDomainType()), true);
 
      //create new QuantifiedSpec for the current quantifier expression
      QuantifiedSpec qSpec = new QuantifiedSpec(op, variable, spec.getSpec());
      return new SpecWrapper(qSpec);
  }


  static SpecWrapper getConstraintSpec(TemporalImpExpr exp, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {

    return new SpecWrapper(new SpecExp(Operator.IMPLIES,
        getConstraintSpec(exp.getLeft(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams),
        getConstraintSpec(exp.getImplication(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
  }

  private static SpecWrapper getConstraintSpec(TemporalIffExpr iffExp, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(iffExp.getElements().get(0), entitiesMapper, tracer, predicateParamsList,
        patternVarsAndParams);
    for (int i = 1; i < iffExp.getElements().size(); i++) {
      spec = new SpecWrapper(new SpecExp(Operator.IFF, spec, getConstraintSpec(iffExp.getElements().get(i),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalOrExpr orExp, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(orExp.getElements().get(0), entitiesMapper, tracer, predicateParamsList,
        patternVarsAndParams);
    for (int i = 1; i < orExp.getElements().size(); i++) {
      Operator op = Operator.OR;
      if ("xor".equals(orExp.getOperator().get(i - 1))) {
        op = Operator.XOR;
      }
      spec = new SpecWrapper(new SpecExp(op, spec, getConstraintSpec(orExp.getElements().get(i), entitiesMapper, tracer,
          predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalAndExpr andExp, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(andExp.getElements().get(0), entitiesMapper, tracer, predicateParamsList,
        patternVarsAndParams);
    for (int i = 1; i < andExp.getElements().size(); i++) {
      spec = new SpecWrapper(new SpecExp(Operator.AND, spec, getConstraintSpec(andExp.getElements().get(i),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalRelationalExpr relExp, EntitiesMapper entitiesMapper,
      Tracer tracer, List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(relExp.getLeft(), entitiesMapper, tracer, predicateParamsList,
        patternVarsAndParams);

    Operator op = null;
    switch (relExp.getOperator()) {
    case "=":
      op = Operator.EQUALS;
      break;
    case "!=":
      op = Operator.EQUALS;
      break;
    case ">":
      op = Operator.LEFT_BIGGER;
      break;
    case ">=":
      op = Operator.LEFT_BIGGER_OR_EQUALS;
      break;
    case "<":
      op = Operator.RIGHT_BIGGER;
      break;
    case "<=":
      op = Operator.RIGHT_BIGGER_OR_EQUALS;
      break;
    default:
      break;
    }
    spec = new SpecWrapper(new SpecExp(op, spec,
        getConstraintSpec(relExp.getRight(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));

    if (relExp.getOperator().equals("!=")) {
      spec = new SpecWrapper(new SpecExp(Operator.NOT, spec));
    }

    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalRemainderExpr tre, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {

    return new SpecWrapper(new SpecExp(Operator.MOD,
        getConstraintSpec(tre.getLeft(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams),
        getConstraintSpec(tre.getRight(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));

  }

  private static SpecWrapper getConstraintSpec(TemporalAdditiveExpr temporalAdditiveExpr, EntitiesMapper entitiesMapper,
      Tracer tracer, List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(temporalAdditiveExpr.getElements().get(0), entitiesMapper, tracer,
        predicateParamsList, patternVarsAndParams);
    for (int i = 1; i < temporalAdditiveExpr.getElements().size(); i++) {
      Operator op = null;
      if ("+".equals(temporalAdditiveExpr.getOperator().get(i - 1))) {
        op = Operator.ADD;
      } else if ("-".equals(temporalAdditiveExpr.getOperator().get(i - 1))) {
        op = Operator.SUBSTRACT;
      }
      spec = new SpecWrapper(new SpecExp(op, spec, getConstraintSpec(temporalAdditiveExpr.getElements().get(i),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalMultiplicativeExpr temporalMultiplicativeExpr,
      EntitiesMapper entitiesMapper, Tracer tracer, List<Variable> predicateParamsList,
      List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(temporalMultiplicativeExpr.getElements().get(0), entitiesMapper, tracer,
        predicateParamsList, patternVarsAndParams);
    for (int i = 1; i < temporalMultiplicativeExpr.getElements().size(); i++) {
      Operator op = null;
      if ("*".equals(temporalMultiplicativeExpr.getOperator().get(i - 1))) {
        op = Operator.MULTIPLY;
      } else if ("/".equals(temporalMultiplicativeExpr.getOperator().get(i - 1))) {
        op = Operator.DIVIDE;
      }
      spec = new SpecWrapper(new SpecExp(op, spec, getConstraintSpec(temporalMultiplicativeExpr.getElements().get(i),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalBinaryExpr temporalBinaryExpr, EntitiesMapper entitiesMapper,
      Tracer tracer, List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = getConstraintSpec(temporalBinaryExpr.getElements().get(0), entitiesMapper, tracer,
        predicateParamsList, patternVarsAndParams);
    for (int i = 1; i < temporalBinaryExpr.getElements().size(); i++) {
      Operator op = null;
      if ("S".equals(temporalBinaryExpr.getOperator().get(i - 1))
          || "SINCE".equals(temporalBinaryExpr.getOperator().get(i - 1))) {
        op = Operator.SINCE;
      } else if ("T".equals(temporalBinaryExpr.getOperator().get(i - 1))
          || "TRIGGERED".equals(temporalBinaryExpr.getOperator().get(i - 1))) {
        op = Operator.TRIGGERED;
      }
      spec = new SpecWrapper(new SpecExp(op, spec, getConstraintSpec(temporalBinaryExpr.getElements().get(i),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalUnaryExpr temporalUnaryExpr, EntitiesMapper entitiesMapper,
      Tracer tracer, List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = null;

    spec = getConstraintSpec(temporalUnaryExpr.getTue(), entitiesMapper, tracer, predicateParamsList,
        patternVarsAndParams);
    Operator op = null;
    if ("Y".equals(temporalUnaryExpr.getKinds()) || "PREV".equals(temporalUnaryExpr.getKinds())) {
      op = Operator.PREV;
    } else if ("H".equals(temporalUnaryExpr.getKinds()) || "HISTORICALLY".equals(temporalUnaryExpr.getKinds())) {
      op = Operator.HISTORICALLY;
    } else if ("O".equals(temporalUnaryExpr.getKinds()) || "ONCE".equals(temporalUnaryExpr.getKinds())) {
      op = Operator.ONCE;
    }
    spec = new SpecWrapper(new SpecExp(op, spec));

    return spec;
  }

  private static SpecWrapper getConstraintSpec(TemporalPrimaryExpr temporalPrimaryExpr, EntitiesMapper entitiesMapper,
      Tracer tracer, List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = null;
    if (temporalPrimaryExpr.getPredPatt() != null) {

      if (temporalPrimaryExpr.getPredPatt() instanceof tau.smlab.syntech.spectra.Predicate) {
        tau.smlab.syntech.spectra.Predicate spectraPredicate = (tau.smlab.syntech.spectra.Predicate) temporalPrimaryExpr
            .getPredPatt();
        Predicate giPredicate = entitiesMapper.getPredicateNameToPredicateMapping().get(spectraPredicate,
            entitiesMapper, tracer);
        List<Spec> parameters = new ArrayList<>();

        for (TemporalExpression temporalExpression : temporalPrimaryExpr.getPredPattParams()) {
          parameters.add(
              getConstraintSpec(temporalExpression, entitiesMapper, tracer, predicateParamsList, 
            		  patternVarsAndParams)
              .getSpec());
        }

        return new SpecWrapper(new PredicateInstance(giPredicate, parameters));

      } else if (temporalPrimaryExpr.getPredPatt() instanceof tau.smlab.syntech.spectra.Pattern) {
        tau.smlab.syntech.spectra.Pattern spectraPattern = (tau.smlab.syntech.spectra.Pattern) temporalPrimaryExpr
            .getPredPatt();
        tau.smlab.syntech.gameinput.model.Pattern giPattern = entitiesMapper.getPatternNameToPatternMapping()
            .get(spectraPattern, entitiesMapper, tracer);
        List<Spec> parameters = new ArrayList<>();

        for (TemporalExpression temporalExpression : temporalPrimaryExpr.getPredPattParams()) {
          parameters.add(
              getConstraintSpec(temporalExpression, entitiesMapper, tracer, predicateParamsList, 
            		  patternVarsAndParams)
              .getSpec());
        }

        return new SpecWrapper(giPattern, parameters);
      }

    }
    if (temporalPrimaryExpr.getPointer() instanceof Monitor) {
      Monitor mon = (Monitor) temporalPrimaryExpr.getPointer();
      return new SpecWrapper(
          new MonitorReference(entitiesMapper.getMonitorNameToMonitorMapping().get(mon, entitiesMapper, tracer)));
    }
    if (temporalPrimaryExpr.getPointer() instanceof Counter) {
      Counter counter = (Counter) temporalPrimaryExpr.getPointer();
      return new SpecWrapper(
          new CounterReference(entitiesMapper.getCounterNameToCounterMapping().get(counter, entitiesMapper, tracer)));
    }
    
    if (temporalPrimaryExpr.getPointer() != null) {
      if (temporalPrimaryExpr.getIndex() != null && temporalPrimaryExpr.getIndex().size() > 0) {
        // array location reference
        spec = getConstraintSpec(temporalPrimaryExpr.getPointer(), entitiesMapper, tracer, predicateParamsList,
            patternVarsAndParams);
        
        // During this phase, referance name of the array is not yet set because it might contain variables
        String arrayName = temporalPrimaryExpr.getPointer().getName();
        Variable variable = entitiesMapper.getVariableNameToVariableMapping().get(arrayName);
        // if arrayName does not point to a global variable, try to find it in parameters of predicate
		if (variable == null && predicateParamsList != null) {
			for (Variable v : predicateParamsList) {
				if (arrayName.equals(v.getName())) {
					variable = v;
					break;
				}
			}
		}
        String refName = variable.getName();
        
        //create an indexes list that contains all the complex (not Integer) indexes of the array
        Map<String, Variable> indexVars = new HashMap<String, Variable>();
        List<Spec> indexSpecs = new ArrayList<Spec>();
        List<Integer> indexDimensions = new ArrayList<>();
        
    	VarType varType = null;
    	if (temporalPrimaryExpr.getPointer() instanceof VarDecl) {
    		VarDecl varDecl = (VarDecl) temporalPrimaryExpr.getPointer();
    		varType = TypeSystemUtils.getVarType(varDecl.getType());
    	}
    	else if (temporalPrimaryExpr.getPointer() instanceof TypedParam) {
    		TypedParam typedParam = (TypedParam)temporalPrimaryExpr.getPointer();
    		varType = TypeSystemUtils.getVarType(typedParam.getType());
    	}
    	indexDimensions = TypeSystemUtils.sizeDefineToInt(varType.getDimensions());
        
        for (TemporalExpression index : temporalPrimaryExpr.getIndex()) {
        	SpecWrapper indexSpec = getConstraintSpec(index, entitiesMapper, tracer, 
        			predicateParamsList, patternVarsAndParams);
        	SpecHelper.getUnderlyingVariables(indexSpec.getSpec(), indexVars);
        	indexSpecs.add(indexSpec.getSpec());
        }
        
        try {
	        if (indexVars.isEmpty()) {
	        	refName = variable.getName();
				for (int i = 0; i < indexSpecs.size(); i++) {
					Integer indexValue = SpecHelper.calculateSpec(indexSpecs.get(i));
					if (indexValue < 0 || indexValue >= indexDimensions.get(i)) {
						throw new SpectraTranslationException(
								String.format("Index %d out of bounds for variable %s", indexValue, variable.getName()));
					}
					refName += String.format("[%d]", indexValue);
				}
	        }
        } catch (Exception e) {
        	throw new SpectraTranslationException(e.getMessage());
        }
        
        spec = new SpecWrapper(new VariableReference(variable, refName, indexVars, indexSpecs, indexDimensions));
      } else {
        spec = getConstraintSpec(temporalPrimaryExpr.getPointer(), entitiesMapper, tracer, predicateParamsList,
            patternVarsAndParams);
      }
    }
    
    if (temporalPrimaryExpr.getTemporalExpression() != null) {

      spec = new SpecWrapper(new SpecExp(Operator.PRIME, getConstraintSpec(temporalPrimaryExpr.getTemporalExpression(),
          entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
    }
    
    if (temporalPrimaryExpr.getTpe() != null) {
      if (temporalPrimaryExpr.getOperator().equals("!")) {
        spec = new SpecWrapper(new SpecExp(Operator.NOT, getConstraintSpec(temporalPrimaryExpr.getTpe(), entitiesMapper,
            tracer, predicateParamsList, patternVarsAndParams)));
      } else if (temporalPrimaryExpr.getOperator().equals("-")) {
        spec = new SpecWrapper(
            new SpecExp(Operator.SUBSTRACT, new SpecWrapper(new PrimitiveValue(0)), getConstraintSpec(
                temporalPrimaryExpr.getTpe(), entitiesMapper, tracer, predicateParamsList, patternVarsAndParams)));
      }
    }

    return spec;
  }

  private static SpecWrapper getConstraintSpec(Referrable pointer, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    SpecWrapper spec = null;

    if (pointer instanceof VarDecl) {
      spec = getConstraintSpec((VarDecl) pointer, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);
    }

    else if (pointer instanceof TypeConstant) {
      spec = getConstraintSpec((TypeConstant) pointer, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);
    }

    else if (pointer instanceof DefineDecl) {
      spec = getConstraintSpec((DefineDecl) pointer, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);

    } else if (pointer instanceof TypedParam) {
      spec = getConstraintSpec((TypedParam) pointer, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);

    } else if (pointer instanceof PatternParam) {
      spec = getConstraintSpec((PatternParam) pointer, entitiesMapper, tracer, predicateParamsList,
          patternVarsAndParams);

    } else if (pointer instanceof Monitor) {
      spec = getConstraintSpec((Monitor) pointer, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);
    } else if (pointer instanceof Counter) {
      spec = getConstraintSpec((Counter) pointer, entitiesMapper, tracer, predicateParamsList, 
    		  patternVarsAndParams);
    }
    //now we check reference to DomainVarDecl from QuantifierExpr
    else if (pointer instanceof DomainVarDecl) {
        spec = getConstraintSpec((DomainVarDecl) pointer, entitiesMapper, tracer, predicateParamsList, 
        		patternVarsAndParams);
    }

    //TODO: add instanceof DefineRegExpDecl
    return spec;
  }

  private static SpecWrapper getConstraintSpec(VarDecl varDecl, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    Variable variable = null;
    if (patternVarsAndParams != null) {
      // it's a pattern. only "inner" variables exist
      for (Variable v : patternVarsAndParams) {
        if (v.getName().equals(varDecl.getName())) {
          variable = v;
          break;
        }
      }
    } else {
      if (!entitiesMapper.getVariableNameToVariableMapping().containsKey(varDecl.getName())) {
        throw new SpectraTranslationException("Couldn't find reference to variable: " + varDecl.getName());
      }
      variable = entitiesMapper.getVariableNameToVariableMapping().get(varDecl.getName());
    }

    return new SpecWrapper(new VariableReference(variable, variable.getName()));
  }
  
  //return the SpecWrapper of the current DomainVarCecl
  private static SpecWrapper getConstraintSpec(DomainVarDecl domainVarDecl, EntitiesMapper entitiesMapper, Tracer tracer,
          List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
       
//      Variable variable = findDomainVar(domainVarDecl.getName());     
      return new SpecWrapper(new VariableReference(new Variable(domainVarDecl.getName(), 
    		  Spectra2GameInputTranslator.getTypeDef(domainVarDecl.getDomainType())), domainVarDecl.getName()));
  }

  private static SpecWrapper getConstraintSpec(Monitor mon, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    Variable variable = null;
    if (!entitiesMapper.getVariableNameToVariableMapping().containsKey(mon.getName())) {
      throw new SpectraTranslationException("Couldn't find reference to monitor: " + mon.getName());
    }
    variable = entitiesMapper.getVariableNameToVariableMapping().get(mon.getName());

    return new SpecWrapper(new VariableReference(variable, variable.getName()));
  }

  private static SpecWrapper getConstraintSpec(Counter counter, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    Variable variable = null;
    if (!entitiesMapper.getVariableNameToVariableMapping().containsKey(counter.getName())) {
      throw new SpectraTranslationException("Couldn't find reference to counter: " + counter.getName());
    }
    variable = entitiesMapper.getVariableNameToVariableMapping().get(counter.getName());

    return new SpecWrapper(new VariableReference(variable, variable.getName()));
  }

  private static SpecWrapper getConstraintSpec(TypeConstant typeConstant, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) {
    return new SpecWrapper(new PrimitiveValue(typeConstant.getName()));
  }

  private static SpecWrapper getConstraintSpec(DefineDecl defineDecl, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) throws SpectraTranslationException {
    Define define = entitiesMapper.getDefineNameToDefineMapping().get(defineDecl, entitiesMapper, tracer);
    return new SpecWrapper(new DefineReference(define));
  }

  private static SpecWrapper getConstraintSpec(TypedParam typedParam, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) {

    if (patternVarsAndParams != null) {
      // this entity is pattern - try to find typedParam in the
      // patternVarsAndParams
      for (Variable v : patternVarsAndParams) {
        if (v.getName().equals(typedParam.getName())) {
          return new SpecWrapper(new VariableReference(v, v.getName()));
        }
      }
    }

    if (predicateParamsList != null) {
      // this entity is predicate - try to find typedParam in the
      // predicateParamsList
      for (Variable v : predicateParamsList) {
        if (v.getName().equals(typedParam.getName())) {
          return new SpecWrapper(new VariableReference(v, v.getName()));
        }
      }
    }
    return null;

  }

  private static SpecWrapper getConstraintSpec(PatternParam patternParam, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) {
    if (patternVarsAndParams != null) {
      // this entity is pattern - try to find patternParam in the
      // patternVarsAndParams
      for (Variable v : patternVarsAndParams) {
        if (v.getName().equals(patternParam.getName())) {
          return new SpecWrapper(new VariableReference(v, v.getName()));
        }
      }
    }

    return null;
  }

  private static SpecWrapper getConstraintSpec(Constant c, EntitiesMapper entitiesMapper, Tracer tracer,
      List<Variable> predicateParamsList, List<Variable> patternVarsAndParams) {
    if (c.getBooleanValue() != null) {
      return new SpecWrapper(new PrimitiveValue(c.getBooleanValue()));
    }
    return new SpecWrapper(new PrimitiveValue(c.getIntegerValue()));
  }

  
  
  
  static SpecWrapper getConstraintSpec(tau.smlab.syntech.spectra.Trigger trig, EntitiesMapper entitiesMapper, 
		  Tracer tracer) throws SpectraTranslationException {
	  
		tau.smlab.syntech.spectra.RegExp init, effect;
		SpecRegExp specRegExpInit, specRegExpEffect;
		List<String> symbols = new ArrayList<>();
		List<Boolean> isBoolean = new ArrayList<>();
		List<Integer> from = new ArrayList<>();
		List<Integer> to = new ArrayList<>();
		
		// create SpecRegExp for the trigger initiator
		if (trig.getInitPointer() != null) {
			init = trig.getInitPointer().getExp();
			specRegExpInit = CreateRegExp(init,symbols,isBoolean,from,to);
		}
		else throw new SpectraTranslationException("trigger has no initial regexp");
		
		// create SpecRegExp for the trigger effect
		if (trig.getEffectPointer() != null) {
			effect = trig.getEffectPointer().getExp();
			specRegExpEffect = CreateRegExp(effect,symbols,isBoolean,from,to);
		}
		else throw new SpectraTranslationException("trigger has no effect regexp");
		
		// create the trigger
		Trigger spectraTrig = new Trigger(specRegExpInit,specRegExpEffect,symbols,
				isBoolean,from,to,tracer.getTrace(trig)); 

		return new SpecWrapper(spectraTrig);
	  
  }

  
  
	/**
	 * translates an xtext regular expression into SpecRegExp. 
	 * Add to the list 'symbols' the variable-names that appear in the regular expression.<br>
	 * 
	 * @param regexp - the regular expression generated by xtext.
	 * @param symbols - a list of all symbols. while exploring the regular expression 
	 * we add to the list 'symbols' the variable-names that appear in the regular expression
	 * @param fromList
	 * @param toList - fromList and toList indicate the range of each variable:
	 * if variable var is stored at index i, var stores vaues from the range fromList[i]..toList[i]
	 * @return A SpecRegExp regular expression
	 */
	
	
	private static SpecRegExp CreateRegExp(RegExp regexp, List<String> symbols, List<Boolean> isBoolean,
			List<Integer> fromList, List<Integer> toList) {
		
		// The empty sequence
		if (regexp.isEmpty()) {
			return( new SpecRegExp() );
		}
		
		// A boolean variable
		if (regexp.getVar() != null) {
			String varname = regexp.getVar().getName();
			
			if ( !symbols.contains(varname) ) { // If varname was not added before, we add it to symbols
				symbols.add( varname ); 
				int index = symbols.indexOf(varname);
				isBoolean.add(index,true);
				fromList.add(index, 0);
				toList.add(index, 1);
				}
			boolean isneg = regexp.isNeg(); // isNeg holds for the case of a !x regexp
		 	return( new SpecRegExp( varname, isneg ) );
		}
		
		
		// A non-boolean variable
		if (regexp.getRangedvar() != null) {
			String varname = regexp.getRangedvar().getVar().getName();
			
			if ( !symbols.contains(varname) ) { // If varname was not added before, we add it to symbols
				symbols.add( varname ); 
				int index = symbols.indexOf(varname);
				isBoolean.add(index,false);
				fromList.add(index, regexp.getRangedvar().getVar().getType().getSubr().getFrom().getValue() );
				toList.add(index, regexp.getRangedvar().getVar().getType().getSubr().getTo().getValue() );
			}
			
			Set<Integer> values = new HashSet<>();
			
			if ( !(regexp.getRangedvar().isMulti()) ) { // There is a single value to add
				values.add(regexp.getRangedvar().getInt());  }
			else {  									// There is a range of values to add 
				int from = regexp.getRangedvar().getFrom();
				int to = regexp.getRangedvar().getTo();
				for (int i = from; i<= to; i++ ) {
					values.add(i);}
			}
			
			AnotherValue another = regexp.getRangedvar().getAnother();
				
			while (another!=null) { // Repeat adding values until there are no more values to add 
				if ( !(another.isMulti()) ) { // There is a single value to add
					values.add(another.getInt());  }
				else {  									// There is a range of values to add 
					int from = another.getFrom();
					int to = another.getTo();
					for (int i = from; i<= to; i++ ) 
						values.add(i);     }
				another=another.getRight();
			}
		 	
			return( new SpecRegExp( varname,values) );
		}
		
		// A boolean value
		if (regexp.getVal() !=null) {
			String val=regexp.getVal();		
			SpecRegExp specRegExp = new SpecRegExp(val);
			return( specRegExp );
		}
		
		// A kleen-star
		if (regexp.isKleened() )  {
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean,fromList,toList );
			Quantifier kind = Quantifier.ZERO_OR_MORE;
			return(new SpecRegExp(left,kind) ); 
		}
		
		// A '?' symbol
		if (regexp.isQuestionMark() )   {
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean,fromList,toList );
			Quantifier kind = Quantifier.ZERO_OR_ONE;
			return(new SpecRegExp(left,kind) ); 
		}
		
		// A '+' symbol
		if (regexp.isPlus() )   {
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean,fromList,toList );
			Quantifier kind = Quantifier.ONE_OR_MORE;
			return(new SpecRegExp(left,kind) ); 
		}
		
		// A complement: '~'
		if (regexp.isComp() )   { 
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean,fromList,toList); 
			Quantifier kind = Quantifier.COMPLEMENT;
			return(new SpecRegExp(left,kind) );
		}
		
		// An exact repetition : {n}
		if (regexp.isHaveExactRepetition() )   { 
				SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean,fromList,toList );
				Quantifier kind = Quantifier.EXACT_REPETITION;
				int exact = regexp.getExactRepetition();
				return(new SpecRegExp(left,kind,exact) ); 
		}
		
		// An at least repetitions: {n,}
		if (regexp.isHaveAtLeast() )   { 
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols ,isBoolean,fromList, toList); 
			Quantifier kind = Quantifier.AT_LEAST;
			int from = regexp.getAtLeast();
			return(new SpecRegExp(left,kind,from) ); 
		}
		
		// A range of repetitions: {n,m}
		if (regexp.isHaveRange() )   { 
			SpecRegExp left = CreateRegExp( regexp.getLeft(),symbols,isBoolean, fromList, toList ); 
			int from = regexp.getFrom();
			int to = regexp.getTo();
			return(new SpecRegExp(left,from,to) ); 
		}
		
		// A binary expression: concatenation, union or intersection
		if (regexp instanceof BinaryRegExp)	{
				BinaryRegExp binRegExp = (BinaryRegExp) regexp;
				if (binRegExp.getRight()!=null) {
					String op = ((BinaryRegExp) regexp).getOp();
					Quantifier kind = null;
					if ( (op==null) ) { kind = Quantifier.CONCAT;}
					else {
						if (op.charAt(0)== '|') kind = Quantifier.UNION;
						if (op.charAt(0) == '&') kind = Quantifier.INTERSECTION; }
					SpecRegExp left = CreateRegExp( binRegExp.getLeft(),symbols 
							,isBoolean,fromList, toList);
					SpecRegExp right = CreateRegExp( binRegExp.getRight(),symbols, 
							isBoolean,fromList, toList );
					return(new SpecRegExp(left,right,kind) );
				}
		}
		
		// If non of the above, we explore the left child.
		return CreateRegExp( regexp.getLeft(),symbols, isBoolean,fromList, toList );
	}
	
  



}
