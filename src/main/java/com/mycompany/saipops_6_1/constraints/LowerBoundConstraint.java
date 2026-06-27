/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.constraints;
          
import static com.mycompany.saipops_6_1.Constants.*;
import static com.mycompany.saipops_6_1.Parameters.USE_OPTIMISTIC_DIMENSIONING;
import com.mycompany.saipops_6_1.SignificanceEnum;
import static com.mycompany.saipops_6_1.SignificanceEnum.*;
import com.mycompany.saipops_6_1.utils.MathUtils;
import static com.mycompany.saipops_6_1.utils.MathUtils.isEqual;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class LowerBoundConstraint {
    
    private final String constraint_Name ;
    private double lowerBound ;
    
    private List <Triplet> coefficientList  =   new   ArrayList <Triplet>  ();    
                
    private double maxLHS = ZERO;
     
    public LowerBoundConstraint (double lowerBound , String name) {
        this.lowerBound = lowerBound;
        constraint_Name = name;
    }
    
    public String toString (){
        
        String result= "\n-----------------------\n LBC " + constraint_Name + ": ";
        for (Triplet triplet : this.coefficientList) {
            result += triplet.constraintCoefficient + " "+ triplet.varName + (triplet.isFractional ?  "(f)": "" ) +   " + ";
        }
        result+= " : "+ this.lowerBound;
        result += " MAX_LHS "+ this.maxLHS;
        return result ;
    }
    
    public void getNeutralvariables (Set<String> neutralVarsWithPlusCoeff , Set<String> neutralVarsWithMinusCoeff){
                
        for (Triplet triplet: coefficientList){
            if (MathUtils.isEqual (triplet.objectiveCoeffcient , DOUBLE_ZERO)){
                if (triplet.constraintCoefficient < ZERO){
                    neutralVarsWithMinusCoeff.add (triplet.varName);
                }else {
                    neutralVarsWithPlusCoeff.add  (triplet.varName);
                }
            }
        }
                 
    }
      
    public int getVariableCount () {
        return coefficientList.size();
    }
    
    public void add (Triplet triplet ) {       
               
        boolean cond1= (triplet.objectiveCoeffcient > ZERO && triplet.constraintCoefficient > ZERO);
        boolean cond2= (triplet.objectiveCoeffcient < ZERO && triplet.constraintCoefficient < ZERO);
        
        if (cond1 || cond2   ) {
            triplet.significance = SignificanceEnum.PRIMARY;
        }  else if (triplet.objectiveCoeffcient == DOUBLE_ZERO){
            triplet.significance = SignificanceEnum.NEUTRAL;
        } else triplet.significance = SignificanceEnum.SECONDARY;
        
        this.coefficientList .add (triplet) ;  
        
        if (triplet.constraintCoefficient > ZERO) this.maxLHS += triplet.constraintCoefficient;
         
    }
    
  
    // copy this constraint into another
    //
    // used by every node in the cplex search tree to get its own copy of every constraint
    //    
    public LowerBoundConstraint getCopy ( ) {
        LowerBoundConstraint twin = new LowerBoundConstraint ( this.lowerBound,this.constraint_Name);
        
        for (Triplet thisTriplet: this.coefficientList){
            twin.coefficientList .add (thisTriplet.getCopy() );
        }
               
        twin.maxLHS = this.maxLHS;
       
        return twin;
    }
    
    public Attributes getAttributes (  ) {   
        
        Attributes attr = new Attributes ();
        
        double remainingPrimarySurplus =   this.maxLHS - this.lowerBound;
        double remainingSecondarySurplus =   this.maxLHS - this.lowerBound;
        
        int numPrimaryVariablesExamined     = ZERO; 
        int numSecondaryVariablesExamined     = ZERO; 
        
        double remainingPositiveNeutralSurplus =   this.maxLHS - this.lowerBound;
        double remainingNegativeNeutralSurplus=   this.maxLHS - this.lowerBound;
        
        int numPositiveNeutralVariablesExamined     = ZERO; 
        int numNegativeNeutralVariablesExamined     = ZERO; 
                 
        attr.constraintName = this.constraint_Name;
        attr.constraintSize = coefficientList.size();
        
        for (int index = ZERO; index < coefficientList.size() ; index ++ ){    
            
            Triplet triplet = this.coefficientList.get(index);    
            double thisCoeff = triplet.constraintCoefficient;
            double thisCoeffMagnitude =  Math.abs (  thisCoeff );
            double thisObjMagn = Math.abs (   triplet.objectiveCoeffcient );
                                                
            if (triplet.significance.equals(PRIMARY)  ){
                
                if (thisObjMagn >= attr.highest_PrimaryVar_objMagn) {
                    attr.second_highest_PrimaryVar_objMagn = attr.highest_PrimaryVar_objMagn;
                    attr.highest_PrimaryVar_objMagn =   thisObjMagn;
                } else {
                    attr.second_highest_PrimaryVar_objMagn = Math.max (attr.second_highest_PrimaryVar_objMagn, thisObjMagn) ;
                }
                
                                
                if (triplet.isFractional  )    {
                    attr.fractionalPrimaryVariables  .add( triplet.varName  );                                        
                }  
                
                attr.allPrimaryVariables.add (triplet.varName );
               
                numPrimaryVariablesExamined ++;
                remainingPrimarySurplus -= thisCoeffMagnitude;
                if (BILLION == attr.primaryDimension){
                    if (isEqual (remainingPrimarySurplus, DOUBLE_ZERO)){
                        attr.primaryDimension=numPrimaryVariablesExamined;
                         
                    }else if (remainingPrimarySurplus  < DOUBLE_ZERO){
                        attr.primaryDimension=numPrimaryVariablesExamined- ONE;
                         
                    }
                }
                             
            } else if (triplet.significance.equals( SECONDARY)) {                
                //secondary var   
                
                                                                                
                if (triplet.isFractional   ){
                    attr.fractionalSecondaryVariables.add (triplet.varName   );
                }
                
                attr.allSecondaryVariables.add(triplet.varName  );
                
                numSecondaryVariablesExamined ++;
                remainingSecondarySurplus -= thisCoeffMagnitude;
                if (BILLION == attr.secondaryDimension){
                    if (isEqual (remainingSecondarySurplus , DOUBLE_ZERO)){
                        attr.secondaryDimension=numSecondaryVariablesExamined;                         
                    }else if (remainingSecondarySurplus  < DOUBLE_ZERO){
                        attr.secondaryDimension=numSecondaryVariablesExamined- ONE;                         
                    } 
                }
                
            }   else /*neutral var*/  if (triplet.constraintCoefficient < ZERO){

                if (triplet.isFractional){
                    attr.fractionalNegativeNeutralVariables.add (triplet.varName  );
                }
                
                attr.allNegativeNeutralVariables.add(triplet.varName );
                
                numNegativeNeutralVariablesExamined ++;
                remainingNegativeNeutralSurplus -= thisCoeffMagnitude;
                if (BILLION == attr.negativeNeutralDimension){
                    if (remainingNegativeNeutralSurplus < ZERO){
                        attr.negativeNeutralDimension = numNegativeNeutralVariablesExamined - ONE;
                    } else if (isEqual (remainingNegativeNeutralSurplus , ZERO)){
                        attr.negativeNeutralDimension = numNegativeNeutralVariablesExamined;
                    }
                }

            } else /*positive neutral var*/  {
                               
                if (triplet.isFractional){
                    attr.fractionalPositiveNeutralVariables.add (triplet.varName  );
                }
                
                attr.allPositiveNeutralVariables.add(triplet.varName );
                
                numPositiveNeutralVariablesExamined ++;
                remainingPositiveNeutralSurplus -= thisCoeffMagnitude;
                if (BILLION == attr.positiveNeutralDimension){
                    if (remainingPositiveNeutralSurplus < ZERO){
                        attr.positiveNeutralDimension = numPositiveNeutralVariablesExamined - ONE;
                    } else if (isEqual (remainingPositiveNeutralSurplus ,ZERO)){
                        attr.positiveNeutralDimension = numPositiveNeutralVariablesExamined;
                    }
                }
                                                      
            }//triplet significance if else
        }//for loop walking through coefficients in the constraint
               
        return   attr  ;          
    }
     
    public void sort () {
       
        //for pessimistic dimensioning, sort in natural order 
         
        Collections.sort(this.coefficientList);
        if ( USE_OPTIMISTIC_DIMENSIONING  ) Collections.reverse(coefficientList);
        
    }
      
    public LowerBoundConstraint applyKnownFixings (TreeMap<String, Boolean> fixings, Set<String> fractionalVariables) {
        this.applyFixings(fixings,fractionalVariables);      
        return this.coefficientList.size () < TWO ?  null: this;
    }
    
    private void applyFixings  (TreeMap<String, Boolean> fixings, Set<String> fractionalVariables ) {
        
        
        
        List <Triplet> updated_coefficientList  =   new   ArrayList <Triplet>  ();   
        
        //walk thru coeff list
        for (Triplet triplet:  coefficientList){
            
            Boolean fixedValue = fixings.get( triplet.varName);
            
            if (fractionalVariables.contains(triplet.varName)){
                triplet.isFractional = true;
                             
            } 
            
            if (null!=fixedValue){
                if (  fixedValue){
                    //1 fixed
                    this.lowerBound -= triplet.constraintCoefficient;
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }else {
                    // 0 fixed
                    if (triplet.constraintCoefficient> ZERO) this.maxLHS -=  triplet.constraintCoefficient;
                }
                
            }else {
                updated_coefficientList.add (triplet );
            }
        }
        
               
        this.coefficientList= updated_coefficientList;
    }
    
}
