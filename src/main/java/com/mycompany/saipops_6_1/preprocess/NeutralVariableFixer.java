/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.preprocess;
  
import com.mycompany.saipops_6_1.constraints.LowerBoundConstraint;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author sst119
 * 
 * this should be done at every node 
 * 
 * 
 */
public class NeutralVariableFixer {
    
    public static void fixVariables ( List<LowerBoundConstraint> lbcList ){
        //find neutral variables that only appear in 1 sign
        //if positive only , fix to 1
        //if negative only,  fix to 0
        
        Set<String> positiveVariables  = new HashSet<String> ();
        Set<String> negativeVariables = new HashSet<String> ();
        
        for (LowerBoundConstraint lbc : lbcList){
            lbc.getNeutralvariables( positiveVariables, negativeVariables);
        }
        
        Set<String> positiveOnlyVariables  = new HashSet<String> ();
        Set<String> negativeOnlyVariables = new HashSet<String> ();
        
        positiveOnlyVariables.addAll(positiveVariables);
        positiveOnlyVariables.removeAll( negativeVariables);
        
        negativeOnlyVariables.addAll( negativeVariables);
        negativeOnlyVariables.removeAll(positiveVariables );
        
        TreeMap<String, Boolean> fixings = new TreeMap<String, Boolean> ();
        for (String pVar: positiveOnlyVariables){
            fixings.put (pVar, true );
        }
        for (String nVar : negativeOnlyVariables){
            fixings.put (nVar, false);
        }
        
        if (!fixings.isEmpty()){
            TreeSet<String> emptySet =new TreeSet<String> () ;
            for (LowerBoundConstraint lbc : lbcList){
                lbc.applyKnownFixings(fixings, emptySet );
            }
        }
        
        
    }
    
    
}
