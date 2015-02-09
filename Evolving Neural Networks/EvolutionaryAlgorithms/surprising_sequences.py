'''
Contains logic for solving surprising sequences
'''

import itertools                                    # Nice to have
import core.util

from core.individual import BaseIndividual          # To inherit from base individual
from core.phenotype import IntegerPhenotype         # To apply this phenotype to explore strings
from core.problem_handler import ProblemHandler     # To implement the SurprisingSequencesHandler 


class SurprisingSequencesIndividual(BaseIndividual):
    
    # Whether or not to use global surprising sequences. Defaults to true, can be changed from main
    use_global_ss = True
    
    def __init__(self, unique_characters, string_length, phenotype = None):
        '''Constructor. If phenotype is specified, the individual should be cloned'''
        
        # First, call the baseclass constructor
        BaseIndividual.__init__(self)
        
        # Clone the individual..
        if phenotype:
            self.phenotype = phenotype
        # .. or create it randomly
        else:
            # The individual need an iteger phenotypes with as many numbers as the symbols we're representing
            # Do unique_characters-1 because IntergerPhenotype is inclusive
            self.phenotype = IntegerPhenotype(string_length, unique_characters-1, 0)
        
    
    def get_integer_phenotype(self):
        return self.phenotype
    
    def calculate_fitness(self):
        # Map the integers of the phenotype to characters
        sequence = "".join(map(lambda x : chr(97+x), self.phenotype.get_int_string()))
        
        # The calculation itself depends on whether or not we're using global or local surprising sequences
        if SurprisingSequencesIndividual.use_global_ss:
            return SurprisingSequencesIndividual.global_fitness(sequence)
        else:
            return SurprisingSequencesIndividual.local_fitness(sequence)
    
    def is_optimal(self):
        # The same rule applies here as in one_max
        return 1 - self.get_fitness() < 1E-10
    
    def breed(self, other_parent):
        # A surprisingsequence individual can only breed if the other parent can return an integer phenotype
        if 'get_integer_phenotype' in dir(other_parent):
            P1, P2 = self.phenotype.crossover(other_parent.get_integer_phenotype())
            return [ SurprisingSequencesIndividual(0,0,P1) , SurprisingSequencesIndividual(0,0,P2)]
        else:
            raise Exception("A SurprisingSequenceIndividual can only breed with other individuals capable of returning an integer phenotype")
    
    @staticmethod
    def global_fitness(string):
        # Start the count here..
        count = 0
        # For each distance d to consider..
        for d in range(len(string) - 2):
            # Create a dictionary with the patterns to be tested
            occurences = {}
            # Loop trough the string
            for ii in range(len(string)-1-d):
                s = string[ii] + string[ii+d+1]
                if s in occurences:
                    occurences[s] += 1
                else:
                    occurences[s] = 0
        # Count the total number of excess occurences in the string
            for vv in occurences.itervalues():
                count += vv
        return 1.0 / (1.0 + count)
    
    @staticmethod
    def local_fitness(string):
        # Create a dictionary with the patterns to be tested
        occurences = {}
        # Loop trough the string
        for ii in range(len(string)-1):
            # The pattern we're currently considering
            s = string[ii:ii+2]
            # If s is already in occurences, increase the count
            if s in occurences:
                occurences[s] += 1
            # If not, tell occurences that the pattern has appeared
            else:
                occurences[s] = 0
        
        # Loop trough the dict and count the "excess" patterns
        count = 0
        for vv in occurences.itervalues():
            count += vv
        # Return this fraction
        return 1.0/(1+count)
    
    def __str__(self):
        return "SurprisingSequencesIndividual with string " + "".join(map(lambda x : chr(97+x), self.phenotype.get_int_string())) + " and fitness " + str(self.get_fitness())
    
class SurprisingSequencesHandler(ProblemHandler):    
    
    
    @staticmethod
    def get_initial_population(number_of_individuals):
        '''Creates SurprisingSequencesIndividuals and returns them as a list'''
        
        # Get the parameters
        print "What symbol set size should we use? Default is 3"
        symbol_size = core.util.get_int(2, 29) or 3
        
        print "String length to search for?"
        string_length = core.util.get_int(3, 9999) or 5
        
        print "Look for global surprising sequences? Default is no / local SS"
        use_global_ss = core.util.get_yes_no() or False
        
        initial_population = []
        SurprisingSequencesIndividual.use_global_ss = use_global_ss
        for _ in itertools.repeat(None, number_of_individuals):
            initial_population.append(SurprisingSequencesIndividual(symbol_size, string_length))
        return initial_population