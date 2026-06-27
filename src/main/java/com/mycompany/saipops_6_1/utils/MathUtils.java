/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.utils;
   
import static com.mycompany.saipops_6_1.Constants.*;
import static java.lang.System.exit;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 *
 * @author sst119
 */
public class MathUtils {
    
    public static double getObjMagnSum (TreeSet<String> variables , TreeMap<String, Double>  objectiveFunctionMap) {
        double sum = DOUBLE_ZERO;
        
        for (String var: variables ){
            sum += Math.abs ( objectiveFunctionMap.get( var));
        }
        
        return sum;
    }
    
    public   static    TreeSet<String>  getMaxObjMagn ( Set<String> candidates  ,  TreeMap<String, Double>  objectiveFunctionMap){
        return getMaxObjMagn (   candidates  ,   objectiveFunctionMap, new  TreeMap<String, Double> ());
    }
   
    public static    TreeSet<String>  getMaxObjMagn ( Set<String> candidates  ,  TreeMap<String, Double>  objectiveFunctionMap, TreeMap <String, Double> fractionalvariablesMap){
        TreeSet<String >  winners = new  TreeSet<String>();
        double bestKnownObjMagn = -ONE;
        
        for (String var : candidates ){
            Double objval =   objectiveFunctionMap.get( var);
            
            if (!fractionalvariablesMap.isEmpty()) {
                
                
                
                if (objval > ZERO){
                    objval *= (ONE - fractionalvariablesMap.get (var) ) ; 
                }else {
                    objval *=   fractionalvariablesMap.get (var)   ; 
                }
                                  
            }
            
            double thisObjMagn = Math.abs ( objval);
            if (thisObjMagn > bestKnownObjMagn){
                bestKnownObjMagn = thisObjMagn;
                winners.clear();
            }
            if (Math.abs(bestKnownObjMagn -thisObjMagn) < EPSILON_SMALL){
                winners.add (var );
            }
        }
       
        return winners ;
    }
    
    public static  boolean isEqual (double x1, double x2) {
        return Math.abs(x1 -x2) < EPSILON_SMALL ;
    }
             
    public static   TreeSet<String> getMaxiMinFrequency(Set<String>  candidates,  TreeMap<String, Double>  mapOne,  TreeMap<String, Double>  mapTwo ) {
        TreeSet<String >  winners = new  TreeSet<String>();
        
        double smallestKnownFreq = DOUBLE_ZERO; 
        double largestKnownFreq = DOUBLE_ZERO;
        
        //find var whose smaller freq is as large as possible
        
        
        for (String candidate:  candidates){
            Double pFreq = mapOne.get (candidate );
            if (null == pFreq) pFreq = DOUBLE_ZERO;
            Double nFreq = mapTwo.get (candidate );
            if (null == nFreq) nFreq = DOUBLE_ZERO;
            
            pFreq= Math.abs (pFreq );
            nFreq= Math.abs (nFreq);
            
            double smaller = Math.min (pFreq,  nFreq);
            double larger = Math.max (pFreq,  nFreq);
            
            if ( (smaller >smallestKnownFreq ) || (  isEqual(smaller , smallestKnownFreq) && largestKnownFreq<larger  )) {
                winners.clear();
                smallestKnownFreq =smaller;
                largestKnownFreq= larger;
                winners.add (candidate );
            }  else if (isEqual(smaller , smallestKnownFreq) && isEqual(largestKnownFreq,larger)  ) {
                winners.add (candidate );
            }
              
        }
               
        return winners;
    }   
    
    
}
