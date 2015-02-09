'''
This file contains strategies for choosing parents from a population and choosing the new generation
'''
from random import random as r
from random import randint as ri
from random import shuffle
import numpy as np
import util

class AdultSelectionStrategies:
    '''Selects the adults that are allowed to enter the next generation, based on different strategies.
    '''
    
    # The strategy to use. I switch on a string for now, but there are better ways to do this...
    strategy = "FGR"
    
    # The factor of children to produce in over-producing. Defaults to twice as many
    over_production_factor = 2
    
    # The factor of children to produce in generational mixing. Defaults to the same amount.
    generational_mixing_factor = 1
    
    # The number of individuals to copy straight over to the new population, AKA Eliteism
    eliteism_count = 0
    
    @staticmethod
    def get_next_generation(population):
        
        # Get the best eliteism_count parents and add them straight to the new generation
        elites = sorted(population.population , key=lambda x : -x.get_fitness())[:(AdultSelectionStrategies.eliteism_count or 0)]
        #print "Keeping " + str(AdultSelectionStrategies.eliteism_count) + " : " + str(elites)
        
        # Select the proper strategy, and make a call to the ParentSelectionStrategy to get the children. Remember to 
        
        if AdultSelectionStrategies.strategy is "FGR":
            # In full generational replacement, we need as many children as are currently in the population
            children = ParentSelectionStrategy.select_parents(population.population, len(population.population))
            next_generation = AdultSelectionStrategies.full_generational_replacement(population.population, children)
        
        elif AdultSelectionStrategies.strategy is "OP":
            # In over-production, we will need some more children. The exact amount is customizeable
            children = ParentSelectionStrategy.select_parents(population.population, int(AdultSelectionStrategies.over_production_factor*len(population.population)))
            next_generation = AdultSelectionStrategies.over_production(population.population, children)
        
        elif AdultSelectionStrategies.strategy is "GM":
            # Children count in generational mixing is customizeable as well.
            children = ParentSelectionStrategy.select_parents(population.population, int(AdultSelectionStrategies.generational_mixing_factor*len(population.population)))
            next_generation =  AdultSelectionStrategies.generational_mixing(population.population, children)
        
        # Return the elites and the next generation - cut from the far end of the new individuals
        return elites + next_generation[:len(population.population)-len(elites)]
        
    @staticmethod
    def full_generational_replacement(parents, children):
        # Not much to do here. Just return the children list
        return children
    
    @staticmethod
    def over_production(parents, children):
        # Return the best individuals in children
        return sorted(children, key=lambda x : -x.get_fitness())[:len(parents)]
        
    
    @staticmethod
    def generational_mixing(parents, children):
        # Return the best individuals contained in both parents and children
        return sorted(parents+children , key=lambda x : -x.get_fitness())[:len(parents)]
    
class ParentSelectionStrategy:
    '''Class for selecting parents, and breeding them to produce children. Input to these strategies are always the old generation, as well as the amount of children to produce'''
    # The strategy to use. I switch on a string for now, but there are better ways to do this...
    strategy = "FP"
    
    @staticmethod
    def select_parents(candidate_parents, children_count):
        # Select the proper parent selection strategy
        
        # Fitness porportinate
        if ParentSelectionStrategy.strategy is "FP":
            return ParentSelectionStrategy.fitness_proportionate(candidate_parents, children_count)
        # Sigma-scaling
        elif ParentSelectionStrategy.strategy is "SS":
            return ParentSelectionStrategy.sigma_scaling(candidate_parents, children_count)
        # Tournament selection
        elif ParentSelectionStrategy.strategy is "TS":
            return ParentSelectionStrategy.tournament_selection(candidate_parents, children_count)
        # Rank selection
        elif ParentSelectionStrategy.strategy is "RS":
            return ParentSelectionStrategy.rank_selection(candidate_parents, children_count)
        
        
    
    @staticmethod
    def fitness_proportionate(candidate_parents, children_count):
        '''Normalize the fitness list and select the parents based on the spin of the roulette wheel'''
        # First, get the fitness as a cumulative list
        fitness_list = [candidate_parents[0].get_fitness()]
        for ii in range(1,len(candidate_parents)):
            fitness_list.append(candidate_parents[ii].get_fitness() + fitness_list[-1])
        
        # Create a list to hold the resulting children
        children = []
        
        # Create the desired number of children
        while len(children) < children_count:

            # Get the parent indices from the roulette wheel
            first_parent_index = util.roulette_wheel(fitness_list)
            second_parent_index = util.roulette_wheel(fitness_list)
                
            # Breed the parents and add the child / children to the list
            children += ParentSelectionStrategy.breed(candidate_parents[first_parent_index], candidate_parents[second_parent_index])
            
        # Return the children list. Due to the uncertanity of breed, make sure to scale the list down        
        return children[:children_count]
    
    @staticmethod
    def sigma_scaling(candidate_parents, children_count):
        
        # Get the average fitness value of the parents
        avg_fitness = np.average(map(lambda x : x.get_fitness() , candidate_parents))
        # Get the variance
        var_fitness = np.var(map(lambda x : x.get_fitness() , candidate_parents))
        
        # Create the cumulative fitness list based on the formula in the paper
        fitness_list = max(0, [1+(candidate_parents[0].get_fitness()-avg_fitness)/(2*var_fitness)])
        for ii in range(1,len(candidate_parents)):
            # Add the fitness. I had some issues regarding negative expected number of times to add the genes to the pool, so I went with a minimum value of 0
            fitness_list.append(max(0,1+(candidate_parents[ii].get_fitness()-avg_fitness)/(2*var_fitness)) + fitness_list[ii-1]) 
                
            
        # Create the desired amount of children
        children = []
        while len(children) < children_count:
            # Spin the wheel!
            first_parent_index = util.roulette_wheel(fitness_list)
            second_parent_index = util.roulette_wheel(fitness_list)
            
            # Breed the two parents to create a child
            children += ParentSelectionStrategy.breed(candidate_parents[first_parent_index], candidate_parents[second_parent_index])
        
        # Return the children list. Due to the uncertanity of breed, make sure to scale the list down        
        return children[:children_count]
    
    # Parameters for the torunament selection. Customizeable from main
    tournament_selection_epsilon = 0.50
    tournament_selection_K = 5
    
    @staticmethod
    def tournament_selection(candidate_parents, children_count):
        # Fetch tournament_selection_K and tournament_selection_epsilon to simplify code
        K, epsilon = ParentSelectionStrategy.tournament_selection_K, ParentSelectionStrategy.tournament_selection_epsilon
        # The resulting children
        children = []
        # The parents to use. This will be emptied as children are generated
        parents = []
        
        while len(children) < children_count:
            # Scramble the parent list
            unpicked = candidate_parents[:]
            shuffle(unpicked)
            # When the list is scrambled, divide into K chunks
            groups = util.chunker(unpicked, K)
            for group in groups:
                # With a chance epsilon, the probability is random
                if r() < epsilon:
                    parents.append(group[ri(0,len(group)-1)])
                # Otherwise, add the best parent
                else:
                    parents.append(sorted(group , key=lambda x : x.get_fitness())[-1])
                    
            # Parents now contain one parent from each group. Breed as many children as possible from that list. Remember to remove the parents!
            while len(parents) > 1:
                children += ParentSelectionStrategy.breed(parents[0], parents[1])
                parents = parents[2:]
                
        # This should be done now. Return the first children_count individuals
        return children[:children_count]
    
    @staticmethod
    def rank_selection(candidate_parents, children_count):
        
        # Sort the candidate parents in ascending order by their fitness
        cand_par = sorted(candidate_parents, key=lambda x : x.get_fitness())
        
        # Map the candidate parents to ranks
        ranks = range(len(cand_par),0,-1)
        
        # Make the rank list cumulative, so it can be used by the roulette wheel
        for ii in range(1,len(ranks)):
            ranks[ii] += ranks[ii-1]
        
        # Start creating the children
        children = []
        while len(children) < children_count:
            # Spin the wheel!
            parent_one_index = util.roulette_wheel(ranks)
            parent_two_index = util.roulette_wheel(ranks)
            children += ParentSelectionStrategy.breed(cand_par[parent_one_index], cand_par[parent_two_index])
        # Return the children
        return children[:children_count]
                
    @staticmethod
    def breed(parent_one, parent_two):
        ''' Helper method. 'Breeds the two parents and return the children (one or more) in a list'''
        child = parent_one.breed(parent_two)
        if isinstance(child, list):
            return child
        else:
            return [child]
    
    