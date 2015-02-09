from __future__ import division     # 
from random import random as r      # Handy tools for simplifying stuff and things
from random import randint as ri    # 
import itertools                    #

class BaseGenotype:
    '''Base class for all other genotypes.
    
    The BaseGenotype works as an abstract base class for all other genotypes that are to be used in the project.
    All methods defined in this class must be implemented by the other genotypes. This class should never be instanced.
    '''
    
    # Mutation rate: this is the chance that a mutation should actually occur. This defaults to 0.5 but other classes 
    # can (and should!) change this to a more appropriate value
    mutation_rate = 0.5
    
    # Crossover rate: this is the chance that a crossover actually will happen. If this fails, crossover() should
    # just return the genotypes unchanged. This defaults to 0.5, but other classes can (and should!) change this
    # to a more appropriate value
    crossover_rate = 0.5
    
    def mutate(self):
        '''Mutate should manipulate the genome in a small, random matter. Genome specific
        '''
        raise NotImplementedError("All subclasses of BaseGenotype must implement a mutation function")
    
    def crossover(self, other_genome):
        '''Crossover should cross this genome with other_genome, and return the result as a list of two new genomes.
        '''
        raise NotImplementedError("All subclasses of BaseGenotype must implement a crossover function")
    
    
class BinaryGenotype(BaseGenotype):
    '''A binary genotype.
    
    A list of 1s and 0s are used to represent the geonme itself.
    '''
    
    def __init__(self, length, high_probability=0.5, bitstring=None):
        '''Create a binary genome and fill it randomly. If a genome is supplied, use that instead. This is used in breeding, to clone genomes
        '''
        
        # If the supplied bitstring is different from None, the caller wants to clone the genome
        if bitstring:
            self.bitstring = bitstring
        # If not, create a random string
        else:
            # Create the bitstring
            self.bitstring = []
            # Fill it with random 0s and 1s
            for _ in itertools.repeat(None, length):
                if r() < high_probability:
                    self.bitstring.append(1)
                else:
                    self.bitstring.append(0)
        # No matter how the bitstring was supplied, there is a chance for mutation
        if r() < BaseGenotype.mutation_rate:
            self.mutate()

            
        
    def mutate(self):
        '''Mutate the genome in a random matter. 
        
        For a BinaryGenome, a mutation is simply a matter of choosing random bit and flipping it. 
        '''
        
        # Find the index to flip, and flip it. Do it for approx. 10 % of the bitstring
        for _ in itertools.repeat(None, int(max(1, 0.1*len(self.bitstring)))):
            ii = ri(0,len(self.bitstring)-1)
            self.bitstring[ii] = 1 - self.bitstring[ii]
        
    def crossover(self, other_genome, chunk_size = 0):
        '''Perform a crossover with the other genome. Returns a list of both the resulting genome
        '''
        
        # There is a chance that no crossover will actually occur
        if r() < 1-BaseGenotype.crossover_rate:
            # However, the genomes may still be mutated...
            # TODO: Implement this? If so, must implement a way of cloning as well..
            
            return [self, other_genome]
        
        # The actual crossover can only be performed if other_genome has a bitstring
        if 'get_bitstring' in dir(other_genome) and isinstance(other_genome.bitstring, list):
            # TODO: Check length here. May be different?
            # Split down the middle
            split_point = ri(0,len(self.bitstring) // 2)
            # If the chunk size is not 0, round the split point to the nearest chunk
            if chunk_size is not 0:
                split_point = (split_point // chunk_size) * chunk_size
            # Create the bitstrings to use
            b1 = self.bitstring[:split_point]+other_genome.bitstring[split_point:] 
            b2 = other_genome.bitstring[:split_point]+self.bitstring[split_point:]
            if not (b1 or b2):
                _ = 3
            # Create new binary genomes...
            BG1, BG2 = BinaryGenotype(0, bitstring=b1) , BinaryGenotype(0, bitstring=b2)
            # Possibly mutate them
            # TODO: Fix this, as mutation is handled in the constructor
            # .. and return them as a list
            return [ BG1 , BG2 ]
        
    def get_bitstring(self):
        return self.bitstring
            
            
            
            