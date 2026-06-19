/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.saipops_6_1.heuristics;
   
 
import static com.mycompany.saipops_6_1.Constants.*;
import com.mycompany.saipops_6_1.HeuristicEnum;   
import com.mycompany.saipops_6_1.Parameters;
import static com.mycompany.saipops_6_1.Parameters.HEURISTIC_TO_USE;
import static com.mycompany.saipops_6_1.Parameters.PERF_VARIABILITY_RANDOM_GENERATOR;   
import com.mycompany.saipops_6_1.constraints.Attributes;
import com.mycompany.saipops_6_1.constraints.BagOfAttributes;
import com.mycompany.saipops_6_1.utils.MathUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
         
/**
 *
 * @author sst119
 */
public    class Sai_POPS_Heuristic  extends Sai_BASE_Heuristic{
              
    public Sai_POPS_Heuristic (  BagOfAttributes  attributeBag, TreeMap<String, Double>  objectiveFunctionMap  ){
                      
        super ( attributeBag ,     objectiveFunctionMap);
           
    }//end constructor method

    @Override
    protected TreeSet<String> selectPrimaryBranchingVariable(TreeMap<String, Double> candidatesFrequencyMap) {
        TreeSet<String> winners =   getSecondaryApex (candidatesFrequencyMap.keySet() );
        
        //
        winners = MathUtils.getMaxObjMagn(winners, objectiveFunctionMap);
        winners = MathUtils.getMaxiMinFrequency(winners,  attributeBag.jwScoreMap_Primary , new TreeMap<String, Double> ()  );
        
        return winners;
    }
 
    TreeSet<String>    getSecondaryApex (Set<String>  candidates ){
        TreeSet<String> apex =  new TreeSet<String> ();
        
        TreeSet<String> dominated =  new TreeSet<String> ();
                
        if (this.attributeBag.lowestKnown_Fractional_SecondaryDimension==ONE){
            for (Attributes attr: this.attributeBag.fractional_Secondary_AttributeSet){
                TreeSet<String> secondaryVars =  new TreeSet<String> ();
                secondaryVars.addAll( attr.fractionalSecondaryVariables);
                secondaryVars.retainAll( candidates);
                if (!secondaryVars.isEmpty()) dominated.addAll(attr.fractionalPrimaryVariables );
            }
        }
        
        apex.addAll(candidates );
        apex.removeAll(dominated );
        
        if ( apex.isEmpty()) {
            apex.addAll(candidates );
        }
        
        return apex;
    }
}
