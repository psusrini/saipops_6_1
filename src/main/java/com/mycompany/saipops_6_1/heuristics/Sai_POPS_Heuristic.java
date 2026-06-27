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
    protected TreeSet<String> selectPrimaryBranchingVariable(TreeSet<String  > candidates ) {
        
         
        TreeSet<String>  winners =   MathUtils.getMaxObjMagn(candidates , objectiveFunctionMap );
                       
        winners =  MathUtils.getMaxiMinFrequency(winners,  attributeBag.jwScoreMap_Primary , new TreeMap<String, Double> ()  );

      
        return winners;
    }
    
     
   
 
}
