'''
Created on 14. mars 2014

@author: Mads
'''

import util
import strategies
from core.strategies import AdultSelectionStrategies

class ProblemHandler:
    ''''''
    
    population_size = 30
    
    @staticmethod
    def get_initial_population():
        raise NotImplementedError("Creation of a problem-specific individual must be implemented in the problem-specific handler")
    
    @staticmethod
    def do_intermediate_action():
        # This is called between generations to make changes independent of the current running instance 
        # By default, nothing is done, but this can be overridden in the problem-specific handlers
        pass
    
    @staticmethod
    def do_final_action(evolved_population):
        '''This is called at the end of the run, for displaying the solution or similar. Nothing is done by default'''
        pass
        
    
    @staticmethod
    def interactive_customize():
        
        # Set the population size
        print "Set population size (default=" + str(ProblemHandler.population_size) + "): "
        ProblemHandler.population_size = util.get_int(5, 100) or ProblemHandler.population_size
        
        # Set how many elites to keep around
        print "How many elites to keep around? Default=0"
        AdultSelectionStrategies.eliteism_count = util.get_int(0,ProblemHandler.population_size) or 0
        
        
    @staticmethod
    def set_parameters(parameter_dictionary = None):
        
        if parameter_dictionary is None or not isinstance(parameter_dictionary , dict): parameter_dictionary = dict()
        
        # Set the population size
        if 'population size' in parameter_dictionary and isinstance(parameter_dictionary['population size'] , int):
            ProblemHandler.population_size = parameter_dictionary['population size']
            
        # Set the eliteism count
        if 'elites' in parameter_dictionary and isinstance(parameter_dictionary['elites'] , int):
            strategies.AdultSelectionStrategies.eliteism_count = parameter_dictionary['elites']
        
        
