'''
This file contains core logic for the individuals of the program. BaseIndividual summarizes the methods that other problem-specific individuals need to implement
'''

import math

class BaseIndividual:
    
    def calculate_fitness(self):
        '''Calculate the fitness. This is problem-specific
        '''
        raise NotImplementedError("All problem-specific individual must implement their own fitness evaluation")
    
    def is_optimal(self):
        ''' Assess whether or not this individual is optimal in regards to the problem it represents.
         If no such criteria can be established, simply return false
        '''
        return False
    
    def breed(self, other_parent):
        '''Breeds this individual with the argument parent. Return either a list of individuals, or a single individual
        '''
        raise NotImplementedError("All problem-specific individuals must implement their own breeding procedure")
        
    
    def __init__(self):
        '''Constructor. Initialize the fitness value to nan, in order to determine if it has been set or not.'''
        self.fitness = float('nan')

    def get_fitness(self):
        '''Get the fitness value of this individual. The fitness value is computed once and stored to save runtime. 
        If self.fitness is NaN, the fitness has not yet been calculated.'''
        if math.isnan(self.fitness):
            self.fitness = self.calculate_fitness()
        return self.fitness
    
    