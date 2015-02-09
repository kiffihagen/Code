'''
This file contains logic for all the phenotypes in the application. It has a base class for other phenotypes, as well as some usable phenotypes.
To add more phenotypes, either add them here, or add them in a separate file. Either way, be sure to extend BasePhenotype
'''
from genotypes import BinaryGenotype    # Used by the BinaryPhenotype and the StringPhenotype
import math     # Handy bits and pieces
import util     #         -''-

class BasePhenotype:
    '''Base class for all further phenotypes'''
    
    def crossover(self, other_phenotype):
        '''Make this phenotype crossover with the other phenotype and return the result.'''
        raise NotImplementedError
    
    
class IntegerPhenotype(BasePhenotype):
    '''A phenotype for representing integers. It defaults to a binary string, so be sure to change the numbers if you need more than two values'''
    
    def __init__(self, length, upper_bound=1, lower_bound=0, genome=None):
        # If a genome is supplied, that should be used as a genome for this phenotype. 
        # IMPORTANT: The upper and lower bound must still be set if they are different from 0 and 1
        
        # Set the upper and lower (inclusive) bounds on the numbers needed
        self.upper_bound = upper_bound
        self.lower_bound = lower_bound
        
        # If a genome was supplied, check that it is a binary genome and use that
        if genome and 'get_bitstring' in dir(genome):
            self.genome=genome
        
        # If not, create it based on the parameters supplied
        else:
            # Find out how many bits are needed to represent one integer
            bits_needed = int(math.ceil(math.log(upper_bound-lower_bound+1,2)))
            
            # Create a BinaryGenotype to represent all these numbers
            self.genome = BinaryGenotype(length*bits_needed)
            
    def get_int_string(self):
        '''Returns the list of integers this phenotype is supposed to represent'''
        
        # The resulting list of ints
        result = []
        # Find out how many bits are needed per number
        bits_needed = int(math.ceil(math.log(self.upper_bound-self.lower_bound+1,2)))
        # Get the genome, and divide it into chunks
        for bin_num in util.chunker(self.genome.get_bitstring(), bits_needed):
            # Append the integer by joining the bits to a binary string, and convert that back to integers
            result.append(((int("".join(map(lambda x : str(x) , bin_num)),2)) % (self.upper_bound+1)) + self.lower_bound) 
            
        return result
    
    def get_binary_genotype(self):
        return self.genome
    
    def crossover(self, other_phenotype):
        # If the other phenotype can return a binary genotype, the crossover is allowed
        if 'get_binary_genotype' in dir(other_phenotype):
            # Calculate the bits needed, so we can avoid crossing over in the middle of a weight:
            bits_needed = int(math.ceil(math.log(self.upper_bound-self.lower_bound+1,2)))
            G1, G2 = self.genome.crossover(other_phenotype.get_binary_genotype(), chunk_size = bits_needed)
            # Return the new IntegerPhenotypes using the new genomes. Upper and lower bound is determined by this phenotype
            return [
                    IntegerPhenotype(0, self.upper_bound, self.lower_bound, G1),
                    IntegerPhenotype(0, self.upper_bound, self.lower_bound, G2)
                    ]
            
            
        

class BinaryPhenotype(BasePhenotype):
    '''BinaryPhenotype represents a string of bits. Only one argument is needed - the number of bits to represent.'''
    
    def __init__(self, length, genome=None):
        if genome:
            self.genome = genome
        else:
            self.genome = BinaryGenotype(length)
    
    def crossover(self, other_phenotype):
        # If the other phenotype can return a binray genotype, the crossover is allowed
        if 'get_binary_genotype' in dir(other_phenotype):
            G1, G2 = self.genome.crossover(other_phenotype.get_binary_genotype())
            # Return new BinaryPhenotypes using the genomes
            return [ BinaryPhenotype(0,G1) , BinaryPhenotype(0,G2)]

    def get_binary_genotype(self):
        return self.genome
    
    def get_bitstring(self):
        return self.genome.get_bitstring()
    
class StringPhenotype(BasePhenotype):
    '''StringPhenotype represents a string.
    
    This phenotype uses a binary genotype to represent different characters.
    '''
    
    
    def __init__(self, unique_characters, string_length, genome=[]):
        self.unique_characters = unique_characters
        
        if genome:
            self.genome = genome
        else:
            # We need 1 bit for 2 characters 2 bits for 3,4 characters, 3 bits for 5,6,7,8 characters.. 
            bits_needed = int(math.ceil(math.log(unique_characters,2)))
            # Create a random string for starters
            self.genome = BinaryGenotype(bits_needed*string_length)
        
    def crossover(self, other_phenotype):
        # If the other phenotype can return a binray genotype, the crossover is allowed
        if 'get_binary_genotype' in dir(other_phenotype):
            G1, G2 = self.genome.crossover(other_phenotype.get_binary_genotype())
            # Return new StringPhenotypes using the genomes
            return [ StringPhenotype(self.get_unique_characters(),0,G1) , StringPhenotype(self.get_unique_characters(),0,G2)]
    
    def get_binary_genotype(self):
        return self.genome
    
    def get_string(self):
        # Decode the bitstring into characters. If a value overflows, (i.e. decodes to 'g' when max is 'e') the modulo ('b') is in the string instead
        result = ""
        bits_needed = int(math.ceil(math.log(self.unique_characters,2)))
        for char in util.chunker(self.genome.bitstring, bits_needed):
            # Join the chunk to a string of 1 and 0, and convert it to an int in base 10
            num = int("".join(map(lambda x : str(x) , char)),2)
            # Add it to the result - remember modulo 
            result += chr(97 + (num % self.unique_characters))
        return result
        
    
    def get_unique_characters(self):
        return self.unique_characters
