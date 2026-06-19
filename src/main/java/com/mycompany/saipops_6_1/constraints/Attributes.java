/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.constraints;
      
import static com.mycompany.saipops_6_1.Constants.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 */
public class Attributes {
    
    public String constraintName ;
    public int constraintSize ;
    
    public double highest_PrimaryVar_objMagn = ZERO;
    public double second_highest_PrimaryVar_objMagn = ZERO;
    
    public int positiveNeutralDimension = BILLION; 
    public TreeSet<String>  fractionalPositiveNeutralVariables = new TreeSet<String>(); 
    public TreeSet<String>  allPositiveNeutralVariables = new TreeSet<String>(); 
    
    public int negativeNeutralDimension = BILLION; 
    public TreeSet<String>  fractionalNegativeNeutralVariables = new TreeSet<String>(); 
    public TreeSet<String>  allNegativeNeutralVariables = new TreeSet<String>(); 
     
    public int primaryDimension = BILLION; 
    public TreeSet<String   >  fractionalPrimaryVariables    = new TreeSet<String   > ();
    public TreeSet<String   >  allPrimaryVariables    = new TreeSet<String   > ();
   
    public int secondaryDimension = BILLION; 
    public TreeSet<String   >  fractionalSecondaryVariables    = new TreeSet<String   > ();
    public TreeSet<String   >  allSecondaryVariables    = new TreeSet<String   > ();
       
    public boolean hasPrimaryVariables (){
        return allPrimaryVariables.size()> ZERO;
    }
    
    public boolean hasSecondaryVariables (){
        return allSecondaryVariables.size()> ZERO;
    }
   
    public boolean hasFractionalVariables () {
        return  hasFractionalPrimaryVariables () || hasFractionalSecondaryVariables () || hasFractionalNegativeNeutralVariables () || hasFractionalPositiveNeutralVariables() ;
    } 
    
    public boolean hasFractionalPrimaryVariables () {
        return fractionalPrimaryVariables .size()   > ZERO;
    }
    
    public boolean hasFractionalSecondaryVariables () {
        return this.fractionalSecondaryVariables.size()   > ZERO;
    }
     
    public boolean hasFractionalPositiveNeutralVariables () {
        return   fractionalPositiveNeutralVariables.size()> ZERO;
    }
    
    public boolean hasFractionalNegativeNeutralVariables () {
        return   fractionalNegativeNeutralVariables.size()> ZERO;
    }
    
    
}
