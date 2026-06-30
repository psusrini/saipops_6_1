/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.constraints;

import static com.mycompany.saipops_6_1.Constants.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class BagOfAttributes {
    
    public int lowestKnown_Fractional_PrimaryDimension = BILLION;
    public  Set <Attributes> fractional_Primary_AttributeSet = new HashSet <Attributes> ();
    public TreeMap <String, Double> jwScoreMap_Primary = new TreeMap <String, Double>  ();
   
    public int lowestKnown_Fractional_SecondaryDimension = BILLION;
    public  Set <Attributes>  fractional_Secondary_AttributeSet  = new HashSet <Attributes> ();
    public TreeMap <String, Double> jwScoreMap_Secondary = new TreeMap <String, Double>  ();
       
    public int lowestKnown_Fractional_PositiveNeutralDimension = BILLION;
    public int lowestKnown_Fractional_NegativeNeutralDimension = BILLION;
    
    public  Set <Attributes> fractional_PositiveNeutral_AttributeSet  = new HashSet <Attributes> ();
    public  Set <Attributes> fractional_NegativeNeutral_AttributeSet  = new HashSet <Attributes> ();
    
    public TreeMap <String, Double> jwScoreMap_PositiveNeutral = new TreeMap <String, Double>  ();
    public TreeMap <String, Double> jwScoreMap_NegativeNeutral = new TreeMap <String, Double>  ();
    
     
     
    public void add (Attributes attr) {
        
         
        
        if (attr.hasFractionalPositiveNeutralVariables()){
            if (lowestKnown_Fractional_PositiveNeutralDimension > attr.positiveNeutralDimension){
                lowestKnown_Fractional_PositiveNeutralDimension=  attr.positiveNeutralDimension;
                fractional_PositiveNeutral_AttributeSet.clear();                                                 
            }
            if (lowestKnown_Fractional_PositiveNeutralDimension== attr.positiveNeutralDimension){
                fractional_PositiveNeutral_AttributeSet.add(attr );
            }
            
              
            for (String var : attr.fractionalPositiveNeutralVariables){
                Double score = this.jwScoreMap_PositiveNeutral.get (var) ;
                if (null == score) score = DOUBLE_ZERO;
                score +=  Math.pow(TWO, - attr.positiveNeutralDimension)* attr.constraintSize;
                this.jwScoreMap_PositiveNeutral.put (var, score) ;
            }
           
            
        }

        if (attr.hasFractionalNegativeNeutralVariables()){
            if (lowestKnown_Fractional_NegativeNeutralDimension > attr.negativeNeutralDimension){
                lowestKnown_Fractional_NegativeNeutralDimension=  attr.negativeNeutralDimension;
                fractional_NegativeNeutral_AttributeSet.clear();
            }
            if (lowestKnown_Fractional_NegativeNeutralDimension== attr.negativeNeutralDimension){
                fractional_NegativeNeutral_AttributeSet.add(attr );
            }
            
              
            for (String var : attr.fractionalNegativeNeutralVariables){
                Double score = this.jwScoreMap_NegativeNeutral.get (var) ;
                if (null == score) score = DOUBLE_ZERO;
                score +=  Math.pow(TWO, - attr.negativeNeutralDimension)* attr.constraintSize;
                this.jwScoreMap_NegativeNeutral.put (var, score) ;
            }
           
            
        }

        if (attr.hasFractionalPrimaryVariables() ){ 
            if (lowestKnown_Fractional_PrimaryDimension> attr.primaryDimension ){
                lowestKnown_Fractional_PrimaryDimension=attr.primaryDimension ;
                fractional_Primary_AttributeSet.clear();
            }
            if (attr.primaryDimension ==lowestKnown_Fractional_PrimaryDimension ){
                fractional_Primary_AttributeSet.add (attr);
            }
             
            for (String var : attr.fractionalPrimaryVariables){
                Double score = this.jwScoreMap_Primary.get (var) ;
                if (null == score) score = DOUBLE_ZERO;
                
                //score +=  Math.pow(TWO, - attr.primaryDimension)* attr.constraintSize;
                score +=  Math.pow(TWO, - attr.primaryDimension)* (attr.allPrimaryVariables.size() - ONE);  
                
                this.jwScoreMap_Primary.put (var, score) ;
            }
           
        }

        if (attr.hasFractionalSecondaryVariables() ){ 
            if (lowestKnown_Fractional_SecondaryDimension> attr.secondaryDimension ){
                lowestKnown_Fractional_SecondaryDimension=attr.secondaryDimension ;
                fractional_Secondary_AttributeSet.clear();
            }
            if (attr.secondaryDimension ==lowestKnown_Fractional_SecondaryDimension ){
                fractional_Secondary_AttributeSet.add (attr);
            }
            
              
            for (String var : attr.fractionalSecondaryVariables){
                Double score = this.jwScoreMap_Secondary.get (var) ;
                if (null == score) score = DOUBLE_ZERO;
                score +=  Math.pow(TWO, - attr.secondaryDimension)* attr.constraintSize;
                this.jwScoreMap_Secondary.put (var, score) ;
            }
           
            
        }    
        
    }
    
}
