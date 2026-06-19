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
public abstract  class Sai_BASE_Heuristic  implements IBranchingHeuristic {
     
    protected  TreeMap<String, Double>  objectiveFunctionMap;
    protected  BagOfAttributes  attributeBag;
          
    public Sai_BASE_Heuristic (  BagOfAttributes  attributeBag, TreeMap<String, Double>  objectiveFunctionMap  ){
                      
        this. objectiveFunctionMap = objectiveFunctionMap;
        this.  attributeBag= attributeBag;
           
    }//end constructor method
                         
               
    public String getBranchingVariable( ) {
        
        TreeSet<String> candidates = new TreeSet<String> ();
                
        if (  this.attributeBag.lowestKnown_Fractional_PrimaryDimension== BILLION){            
            candidates=   selectNeutralBranchingVariable   (   getNeutralBranchingCandidates() );                          
        } else  {           
             
            candidates=  selectPrimaryBranchingVariable   (   getPrimaryBranchingCandidates()  );            
        }
        
        //random tiebreak        
        String[] candidateArray = candidates.toArray(new String[ZERO]);        
        return candidateArray[ PERF_VARIABILITY_RANDOM_GENERATOR.nextInt(candidates.size())];
    }
    
    protected abstract TreeSet<String> selectPrimaryBranchingVariable(TreeMap<String, Double >  candidates  ) ;
    
    
    private TreeMap<String, Double >   getPrimaryBranchingCandidates() {
        TreeMap<String, Double >  primaryBranchingCandidates_WithFrequency = new TreeMap<String, Double >   ();
        
        int lowestKnownPrimaryDim = this.attributeBag.lowestKnown_Fractional_PrimaryDimension;
        TreeSet<String> directDependents = new TreeSet<String>  ();
        
        TreeSet<String> varsThatWillgetUpFixed = new TreeSet<String>  ();
         
        for (Attributes attr:  attributeBag.fractional_Primary_AttributeSet){
            
            for (String var: attr.fractionalPrimaryVariables){
                Double current = primaryBranchingCandidates_WithFrequency.get(var);
                if (null == current) current = DOUBLE_ZERO;
                primaryBranchingCandidates_WithFrequency.put(var, current + ONE);
            }
                        
            if (ONE == lowestKnownPrimaryDim){
                directDependents.addAll( attr.fractionalSecondaryVariables);   
                if (attr.fractionalPrimaryVariables.size()> ONE) varsThatWillgetUpFixed.addAll( attr.fractionalPrimaryVariables);
            }            
        }
        
        //TreeSet<String>   indirectDependants = getIndirectDependants ( varsThatWillgetUpFixed);
        
        //find apex vars       
        TreeSet<String>   apex = new TreeSet<String>     ();
        apex.addAll( primaryBranchingCandidates_WithFrequency.keySet() );
        apex.removeAll(directDependents); 
        //apex.removeAll(indirectDependants); 
               
        if (!apex.isEmpty() && apex.size() < primaryBranchingCandidates_WithFrequency.size()){
            for (String dep : directDependents)  {
                primaryBranchingCandidates_WithFrequency.remove(dep);
            }
        }
        
        return primaryBranchingCandidates_WithFrequency;
    }
    
    /*
    private TreeSet<String> getIndirectDependants ( TreeSet<String> varsThatWillgetUpFixed){
        
        TreeSet<String> indirectDependants = new TreeSet<String> ();

        if ( attributeBag.lowestKnown_Fractional_SecondaryDimension==ONE && ONE == this.attributeBag.lowestKnown_Fractional_PrimaryDimension){ 
            for (Attributes attr:  attributeBag.fractional_Secondary_AttributeSet){
                TreeSet<String> upFixes = new TreeSet<String>  ();
                upFixes.addAll (varsThatWillgetUpFixed );
                TreeSet<String> secondaries = new TreeSet<String>  ();
                secondaries.addAll(attr.fractionalSecondaryVariables ) ;
                    
                upFixes.retainAll(secondaries ) ;
                if ( upFixes.size()>ONE){
                    indirectDependants.addAll(secondaries );
                }else if (upFixes.size()==ONE){
                    TreeSet<String> temp = new TreeSet<String>();
                    temp.addAll(secondaries );
                    temp.removeAll(upFixes );
                    indirectDependants.addAll(temp);
                }
            }
        }
        
        return indirectDependants;
    }
    */
   
    //   
    private   TreeMap<String, Double>    getNeutralBranchingCandidates  (){
        TreeMap<String, Double>   frequencyMap = new TreeMap<String, Double>  ();
        
        for (Attributes attr:  attributeBag.fractional_PositiveNeutral_AttributeSet){
            for ( String fpNVariable : attr.fractionalPositiveNeutralVariables){
                Double currentFreq = frequencyMap.get (fpNVariable);
                if (null == currentFreq)currentFreq= DOUBLE_ZERO;
                frequencyMap.put (fpNVariable,currentFreq + ONE );
            }            
        }
        
        if (attributeBag.lowestKnown_Fractional_NegativeNeutralDimension < 
                attributeBag.lowestKnown_Fractional_PositiveNeutralDimension){
            frequencyMap.clear();            
        }        
        if (attributeBag.lowestKnown_Fractional_NegativeNeutralDimension <= 
                attributeBag.lowestKnown_Fractional_PositiveNeutralDimension){
            for (Attributes attr:  attributeBag.fractional_NegativeNeutral_AttributeSet){
                for  (String nVar : attr.fractionalNegativeNeutralVariables){
                    Double currentFreq = frequencyMap.get (nVar);
                    if (null == currentFreq)currentFreq= DOUBLE_ZERO;
                    frequencyMap.put (nVar,currentFreq + ONE );
                }               
            }
        }
        
        return frequencyMap;
    }
        
    private  TreeSet<String> selectNeutralBranchingVariable(TreeMap<String, Double> candidateFrequencyMap) {
        return  MathUtils.getMaxiMinFrequency(candidateFrequencyMap.keySet(), attributeBag.jwScoreMap_PositiveNeutral ,  attributeBag.jwScoreMap_NegativeNeutral );
    } 
       
    
    
}
