'''
This file contains logic for running the one-max problem, and sums up what needs to be implemented by other problems which are to use the 
evolutionary algorithm: a problem-specific class inheriting from BaseIndividual, as well as a function to fetch the initial population
'''
from __future__ import division                 # Nice to have
import itertools                                #    -''-
from random import random as r                  #    -''-
from core.util import get_int                         #    -''-
from core.individual import BaseIndividual      # Needed for the problem-specific class
from core.phenotype import IntegerPhenotype     # Needed to use integer phenotypes to represent the bitstring
from core.genotypes import BaseGenotype         # Needed to set suggested values for mutation- and crossover rates in the indiviudals
from core.problem_handler import ProblemHandler # Needed to implement the OneMaxHandler
from sys import maxint
from core.util import get_yes_no

class OneMaxIndividual(BaseIndividual):
    
    def __init__(self, length, phenotype=None):
        '''Constructor for creating an individual. Only the length is needed. If a phenotype is given, it's assumed the caller want to clone the individual'''
        # Call to base class constructor
        BaseIndividual.__init__(self)
        
        # Set up the phenotype
        if phenotype:
            self.phenotype = phenotype
        else:
            # IntegerPhenotype defaults to a binary string, so not much info is needed
            self.phenotype = IntegerPhenotype(length)
    
    def calculate_fitness(self):
        '''The fitness of a one-max individual is related to how much it resembles the target string.'''
        
        # If the bitstring of this individual is different from the target bitstring, this individual is useless
        if len(OneMaxIndividual.target_bitstring) != len(self.get_bitstring()):
            return 0
        
        # Compare the target bitstring with the bitstring of this phenotype. 
        # A "conflict" occurs if a given bit on this individual differs from the same bit in the target bitstring
        conflicts = 0
        for ii in range(len(self.get_bitstring())):
            if self.get_bitstring()[ii] != OneMaxIndividual.target_bitstring[ii]:
                conflicts += 1
                
        # Return a value such that higher score means a more fit individual
        return 1.0 / (1 + conflicts)

    def breed(self, other_parent):
        
        # A one-max individual can only breed with if the other parent can return a integer phenotype
        if 'get_integer_phenotype' in dir(other_parent):
            # New phenotypes can be fetched from the crossover operation in the phenotype
            P1, P2 = self.phenotype.crossover(other_parent.get_integer_phenotype())
            return [ OneMaxIndividual(0,phenotype=P1) , OneMaxIndividual(0,phenotype=P2) ]
            
    def get_bitstring(self):
        # A one-max individual can return a bitstring of 0s and 1s
        return self.phenotype.get_int_string()
     
    def is_optimal(self):
        # An optimal individual has a fitness of 1, motivating the following optimality condition
        return 1 - self.get_fitness() < 1E-10

    def get_integer_phenotype(self):
        '''Simple method for returning the phenotype. Used to breed the individual with other individuals'''
        return self.phenotype
    
    def __str__(self):
        return "One-max individual with bitstring " + str(self.phenotype.genome.bitstring) + "\nand fitness value of " + str(self.get_fitness())
    

    
    @staticmethod
    def get_target_bitstring():
        return OneMaxIndividual.target_bitstring

class OneMaxHandler(ProblemHandler):
    
    @staticmethod
    def get_initial_population():
        '''Create and return a list of individuals to use as an initial population'''
        
        # Get user parameters
        print "Which one-max length would you like? Defaults to 30"
        length = get_int(1,200) or 30
        
        print "Look for a random bitstring? Defaults to 'no'"
        use_random_bitstring = get_int(1,2) or 0
        
        
        # If the user asked for a random bitstring, generate it
        if use_random_bitstring:
            # Clear the old bitstring, just in case
            OneMaxIndividual.target_bitstring = []
            # Generate a random string of the proper length
            for _ in itertools.repeat(None, length):
                if r() < 0.5: OneMaxIndividual.target_bitstring.append(1)
                else: OneMaxIndividual.target_bitstring.append(0)
            
        # If the user has not specified a target bitstring, set it to all 1s
        else:
            OneMaxIndividual.target_bitstring = [1]*length
          
        # Create the initial population
        initial_population = []
        for _ in itertools.repeat(None, ProblemHandler.population_size):
            initial_population.append(OneMaxIndividual(length))
            
        # Before we return, try to enforce the mutation rate and crossover rate to values which are (hopefully) suitable for one-max problem
        # This can be overridden by the user in the main program
        BaseGenotype.mutation_rate = 0.8
        BaseGenotype.crossover_rate = 0.6
        
        # Print the target bitstring
        print "Starting one-max with target bitstring " + str(OneMaxIndividual.get_target_bitstring())
        
        return initial_population
    
        
    
            