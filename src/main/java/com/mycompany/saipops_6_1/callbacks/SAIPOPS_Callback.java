/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.callbacks;
  
import static com.mycompany.saipops_6_1.Constants.*;
import static com.mycompany.saipops_6_1.HeuristicEnum.*; 
import static com.mycompany.saipops_6_1.Parameters.*;
import com.mycompany.saipops_6_1.constraints.*;
import com.mycompany.saipops_6_1.constraints.LowerBoundConstraint;
import com.mycompany.saipops_6_1.heuristics.IBranchingHeuristic;
import com.mycompany.saipops_6_1.heuristics.*;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import static java.lang.System.exit; 
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet; 

/** 
 *
 * @author sst119
 */
public class SAIPOPS_Callback extends IloCplex.BranchCallback{
    
    private        TreeMap<String, IloNumVar>  mapOfAllVariablesInTheModel ;
    private        TreeMap<Integer, HashSet<LowerBoundConstraint>>  mapOfAllConstraintsInTheModel;
    private        TreeMap<String, Double>  objectiveFunctionMap =null; 
   
    public SAIPOPS_Callback (  
            TreeMap<String, Double>  objectiveFunctionMap,
            TreeMap<String, IloNumVar>  mapOfAllVariablesInTheModel ,
            TreeMap<Integer, HashSet<LowerBoundConstraint>> mapOfAllConstraintsInTheModel             
            ){     
        
        this. objectiveFunctionMap = objectiveFunctionMap;
        this.mapOfAllVariablesInTheModel=mapOfAllVariablesInTheModel;
        this.mapOfAllConstraintsInTheModel=mapOfAllConstraintsInTheModel;            
    }
    

    @Override
    protected void main() throws IloException {
        
        if ( getNbranches()> ZERO ){  
            
            String branchingVar = null;
            
            //get fixed and fractional vars
            TreeMap<String, Boolean> fixings = new  TreeMap<String, Boolean>();
            TreeMap<String, Double>  freeVariables = new TreeMap<String, Double>  ();
            TreeSet <String> fractionalVariables = new TreeSet <String> ();
            getFreeAndFixedVars (freeVariables, fixings, fractionalVariables) ;
            
            //get branching recommendation
            try {                
                
                //overrule cplex branching
                if (   HEURISTIC_TO_USE.equals( NATIVE_CPLEX)) {
                    branchingVar = get_CPLEX_Branch_choice ();
                } else {
                    //walk thru all the constraints to collect information
                    BagOfAttributes  attributeBag   = new BagOfAttributes  () ;  

                    for (HashSet<LowerBoundConstraint> lbcSet : mapOfAllConstraintsInTheModel.values()){                     
                        for (LowerBoundConstraint lbc : lbcSet){

                            LowerBoundConstraint lbc_LocalCopy = lbc.getCopy();
                            lbc_LocalCopy= lbc_LocalCopy.applyKnownFixings(fixings, fractionalVariables);

                            if (null != lbc_LocalCopy){

                                Attributes attr = lbc_LocalCopy.getAttributes();  

                                //collect all attributes 
                                attributeBag.add(attr);                             

                            }
                        }
                    }

                    IBranchingHeuristic branchingHeuristic  =   
                             new Sai_POPS_Heuristic(   attributeBag ,   objectiveFunctionMap );

                    branchingVar = branchingHeuristic .getBranchingVariable();
                }
                    
                overruleCplexBranching (branchingVar) ; 
                
            } catch (Exception ex ){
                System.err.println( ex);
                ex.printStackTrace();
                exit(ONE);
            }//end try catch            
        }//end if
    }//end main
    
     
    private String  get_CPLEX_Branch_choice() throws IloException{
        IloNumVar[][] vars = new IloNumVar[TWO][] ;
        double[ ][] bounds = new double[TWO ][];
        IloCplex.BranchDirection[ ][]  dirs = new  IloCplex.BranchDirection[ TWO][];
        
        getBranches (vars, bounds, dirs) ;
        return vars[ZERO][ZERO].getName();
    }

    private  void getFreeAndFixedVars (  
             TreeMap<String, Double>  freeVariables,
              TreeMap<String, Boolean> fixings,
              TreeSet <String> fractionalvariables) throws IloException {
       
        IloNumVar[] allVariables = new  IloNumVar[mapOfAllVariablesInTheModel.size()] ;
        int index =ZERO;
        for  (Map.Entry <String, IloNumVar> entry : mapOfAllVariablesInTheModel.entrySet()) {
            //
            allVariables[index++] = entry.getValue();
        }
        
        double[] varValues = getValues (allVariables) ;
        IloCplex.IntegerFeasibilityStatus [] status =   getFeasibilities(allVariables);
        
        index =-ONE;
        for (IloNumVar var: allVariables){
            index ++;
            
            Double ub = getUB(var) ;
            Double lb = getLB(var) ;
            if (  status[index].equals(IloCplex.IntegerFeasibilityStatus.Infeasible)){
                freeVariables.put  (var.getName(),varValues[index] ) ;    
                fractionalvariables.add( var.getName());
            }else if (HALF < Math.abs (lb-ub) ) {
                freeVariables.put  (var.getName(),varValues[index] ) ;     
                                
            } else {
                
                fixings.put (var.getName(), varValues[index] > HALF) ;
                                
            }            
        }
               
    }
    
    private void  overruleCplexBranching(String branchingVarName ) throws IloException {
        IloNumVar[][] vars = new IloNumVar[TWO][] ;
        double[ ][] bounds = new double[TWO ][];
        IloCplex.BranchDirection[ ][]  dirs = new  IloCplex.BranchDirection[ TWO][];
                        
        getArraysNeededForCplexBranching(branchingVarName, vars , bounds , dirs);

        //create both kids 

        double lpEstimate = getObjValue();
        IloCplex.NodeId zeroChildID =  makeBranch( vars[ZERO][ZERO],  bounds[ZERO][ZERO],
                                              dirs[ZERO][ZERO],  lpEstimate  );
        IloCplex.NodeId oneChildID = makeBranch( vars[ONE][ZERO],  bounds[ONE][ZERO],
                                                 dirs[ONE][ZERO],   lpEstimate );
        
        
        //System.out.println("Zero child "+ zeroChildID);
        //System.out.println("One child "+ oneChildID);
        
    }
    
    private void getArraysNeededForCplexBranching (String branchingVar,IloNumVar[][] vars ,
                                                   double[ ][] bounds ,IloCplex.BranchDirection[ ][]  dirs ){
        
        IloNumVar branchingCplexVar = mapOfAllVariablesInTheModel.get(branchingVar );
               
        //get var with given name, and create up and down branch conditions
        vars[ZERO] = new IloNumVar[ONE];
        vars[ZERO][ZERO]= branchingCplexVar;
        bounds[ZERO]=new double[ONE ];
        bounds[ZERO][ZERO]=ZERO;
        dirs[ZERO]= new IloCplex.BranchDirection[ONE];
        dirs[ZERO][ZERO]=IloCplex.BranchDirection.Down;

        vars[ONE] = new IloNumVar[ONE];
        vars[ONE][ZERO]=branchingCplexVar;
        bounds[ONE]=new double[ONE ];
        bounds[ONE][ZERO]=ONE;
        dirs[ONE]= new IloCplex.BranchDirection[ONE];
        dirs[ONE][ZERO]=IloCplex.BranchDirection.Up;
    }
    
}
     
  
 

