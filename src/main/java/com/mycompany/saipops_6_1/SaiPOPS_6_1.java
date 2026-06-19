/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.saipops_6_1;

import static com.mycompany.saipops_6_1.Constants.ONE;
import static com.mycompany.saipops_6_1.Parameters.printParameters;
import com.mycompany.saipops_6_1.utils.Solver;
import ilog.cplex.IloCplex;
import static java.lang.System.exit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author sst119
 */
public class SaiPOPS_6_1 {

    private static final Logger logger ;
    private static  IloCplex cplex;
    
    static {
        logger= Logger.getLogger(SaiPOPS_6_1.class.getSimpleName() );
        //logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler(SaiPOPS_6_1.class.getSimpleName()+ ".log");

            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);

            logger.info("Logging initialized.");
            

        } catch (Exception e) {
            System.err.println(e.getMessage()) ;
            exit(ONE);
        }
        
    }

    public static void main(String[] args) throws Exception {
        
        printParameters();
        
        System.out.println("Version: SaiPOPS_6_1.0") ;
        
        Solver solver = new Solver ( ) ;
        
        solver.solve ( );
        logger.info("Test Completed Successfully!");
    }
}
