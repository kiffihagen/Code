'''
Created on 18. feb. 2014

@author: Mads Wilthil

This file is meant to simulate several runs of a problem in order to determine the best parameters for the problems
'''
from __future__ import division
import one_max
import surprising_sequences
from flatland_agent import FlatlandHandler
from beer_agent import BeerHandler
from core.genotypes import BaseGenotype
from core.strategies import AdultSelectionStrategies,ParentSelectionStrategy
import numpy as np
from core.population import Population
from main import plot_history
from core import genotypes
from datetime import datetime
from time import strftime

def initial_testing():
    # The assignment states an EA of size 20 should be able to (usually) solve one-max 20 in ~50 generations. Let's try that
    population_size = 10
    one_max_size = 40
    generations = 100
    stats = []

    for mutation_rate in np.arange(0, 1.1, 0.1):
        for crossover_rate in np.arange(0, 1.1, 0.1):
            BaseGenotype.mutation_rate = mutation_rate
            BaseGenotype.crossover_rate
            initial_individuals = one_max.get_initial_population(one_max_size, population_size)
            initial_population = Population(initial_individuals)
            stats.append([round(mutation_rate,1) , round(crossover_rate,1)] + simulate(generations, initial_population))
    # Print all stats which finished before 50 generation
    for s in stats:
        if s[3] < 50:
            print s
    print "Done initial testing"

def local_surprising_sequences(symbol_size, individuals, max_length):
    
    # Set the parameters first
    BaseGenotype.mutation_rate = 0.8
    BaseGenotype.crossover_rate = 0.5
    AdultSelectionStrategies.strategy = "GM"
    ParentSelectionStrategy.strategy = "FP"
    # The rest of this is obsolete, as handlers are introduced
    pass
        
def global_surprising_sequences(symbol_size, individuals, min_length, max_length):
    # Set the parameters first
    BaseGenotype.mutation_rate = 0.8
    BaseGenotype.crossover_rate = 0.5
    AdultSelectionStrategies.strategy = "GM"
    ParentSelectionStrategy.strategy = "FP" 
    # The rest of this is obsolete, as handlers are introduced
    
def one_max_simulate_and_save(filename):
    filename = str(filename) + ".txt"
    # Using a population size of 245, experiment with mutation rates, crossover rates and strategies for onemax 40
    om_size = 40
    pop_size = 245
    # Create the file in which to store the results
    with open(filename, "w") as log:
        log.write("")
    
    for ParentStrat in ["FP","SS","TS","RS"]:
        ParentSelectionStrategy.strategy = ParentStrat
        for AdultStrat in ["FGR","OP","GM"]:
            AdultSelectionStrategies.strategy = AdultStrat
            for MutRate in np.arange(0.2,0.81,0.1):
                BaseGenotype.mutation_rate = MutRate
                for CrossRate in np.arange(0.2,0.81,0.1):
                    BaseGenotype.crossover_rate = CrossRate
                    # Save a header in the file
                    header = "Parent strategy: " + ParentStrat + " ; Adult Strategy: " + AdultStrat + "  ; Mutation rate: " + str(MutRate) + " ; Crossover rate: " + str(CrossRate)
                    with open(filename, "a") as log:
                        log.write(header + "\n")
                    # Run five tests per settings
                    for ii in range(5):
                        # Create the population
                        pop = Population(one_max.get_initial_population(om_size, pop_size, False))
                        while not pop.get_optimal_individual():
                            pop.evolve()
                        # Save some history to disk. A header line, generation counts, a line with max fitness and a line with average fitness
                        hi = pop.fitness_history
                        fitness_max = map(lambda x : x[0] , hi)
                        fitness_avg = map(lambda x : x[1] , hi)
                        
                        with open(filename, "a") as log:
                            log.write(str(fitness_max) + "\n")
                            log.write(str(fitness_avg) + "\n")
                    
def forty_bit():
    # Set the parameters
    population_size = 245
    one_max_size = 40
    
    for mut_rate in [0.6]:
        BaseGenotype.mutation_rate = mut_rate
        for cross_rate in [0.6]:
            BaseGenotype.crossover_rate = cross_rate
            # Do 5 runs each setting
            generations = []
            for ii in range(10):
                population = Population(one_max.get_initial_population(one_max_size, population_size, False))
                generations.append(simulate(200 , population)[1])
            print "Mutation rate of " + str(mut_rate) + " and crossover rate of " + str(cross_rate) + " managed " + str(sum(generations)/len(generations)) + " generations on average"

                
    
    
    

def simulate(problem_handler, generational_cap = 200):
    '''
    Given a problem handler, create a population without changing any parameters from the default, and evolve it.
    Returns a list of [evolved_population , generations_needed]
    '''
    
    # Get the population
    population = Population(problem_handler.get_initial_population())
    
    # Evolve the population for a maximum of generational_cap generations
    for ii in range(generational_cap):
        population.evolve()
        if population.get_optimal_individual():
            return [ population , ii ]
        problem_handler.do_intermediate_action()
        
    return [population , generational_cap]


def find_pop_size():
    '''
    This method is used to find a populations size which consistently solves 40-max in under 100 generations
    I required a minimum score of 9/10  as a measurement for "consistently"
    
    OUTDATED: Must implement use of the new population handlers to have any effect
     '''
    # Use a 0.8 and 0.6 mutation and crossover rates - important part is that they are equal for all population sizes
    BaseGenotype.mutation_rate = 0.8
    BaseGenotype.crossover_rate = 0.6
    ParentSelectionStrategy.strategy = "FP"
    AdultSelectionStrategies.strategy = "GM"
    # Go up to 50..
    for pop_size in range(50,51):
        fails_this_size = 0
        for ii in range(10):
            initial_population = Population(one_max.get_initial_population(20, pop_size, True))
            if simulate(100, initial_population)[1] > 99:
                fails_this_size += 1
        print "Population size " + str(pop_size) + " managed " + str(10-fails_this_size) + "/10 times"

def find_best_mutation_rate(handler):
    
    # Keep track of the best individual, in addition to the generations it took to evolve it and the mutation rate used
    simulated = simulate(handler)
    best_stats = [simulated[0].get_best_individual(), simulated[1] , BaseGenotype.mutation_rate]
    
    for mutation_rate in np.arange(0.1,0.91,0.1):
        BaseGenotype.mutation_rate = mutation_rate
        
        # Each mutation rate gets 5 shots at evolving a good individual
        for _ in range(1):
            
            # Evolve the population
            simulated = simulate(handler)
            # Compare the current results to the best result. Fitness evaluation is the most important part, and if they are equal compare on the generations needed to evolve it
            if simulated[0].get_best_individual().get_fitness() > best_stats[0].get_fitness():
                best_stats = [ simulated[0].get_best_individual(), simulated[1], mutation_rate ]
            elif simulated[0].get_best_individual().get_fitness() == best_stats[0].get_fitness() and simulated[1] < best_stats[1]:
                best_stats = [ simulated[0].get_best_individual(), simulated[1], mutation_rate ]
                
                
                        
            # TODO: Remove the print    
            print "Checked mutation rate of " + str(mutation_rate) + " gaining a fitness of " + str(simulated[0].get_best_individual().get_fitness())
                
    # Return the best mutation rate found for this handler
    return best_stats[2]

def find_best_crossover_rate(handler):        
    
    # Keep track of the best individual, in addition to the generations it took to evolve it and the crossover rate used
    simulated = simulate(handler)
    best_stats = [simulated[0].get_best_individual(), simulated[1] , BaseGenotype.mutation_rate]
    
    for crossover_rate in np.arange(0.1,0.91,0.1):
        BaseGenotype.crossover_rate = crossover_rate
        
        # Each mutation rate gets 1 shots at evolving a good individual
        for _ in range(1):
            
            # Evolve the population
            simulated = simulate(handler)
            # Compare the current results to the best result. Fitness evaluation is the most important part
            if simulated[0].get_best_individual().get_fitness() > best_stats[0].get_fitness():
                best_stats = [ simulated[0].get_best_individual(), simulated[1], crossover_rate ]
            
            # If they are equal compare on the generations needed to evolve it
            elif simulated[0].get_best_individual().get_fitness() == best_stats[0].get_fitness() and simulated[1] < best_stats[1]:
                best_stats = [ simulated[0].get_best_individual(), simulated[1], crossover_rate ]
            
            # TODO: Remove the print    
            print "Checked crossover rate of " + str(crossover_rate) + " gaining a fitness of " + str(simulated[0].get_best_individual().get_fitness())
                
                
    # Return the best mutation rate found for this handler
    return best_stats[2]

def flatland_parameter_testing(filename = None, use_static = True):
    '''Do some testing for the flatland agent. If a filename is supplied, store the results in filename'''
    
    # Append some initial information to the file
    with open(filename, "a") as log:
        log.write("\nFlatland testing commenced at " + str(datetime.now()) + " using ")
        if use_static: log.write("static")
        else: log.write("dynamic")
        log.write(" level generation")
                    
    # Set the handler
    handler = FlatlandHandler
    
    # Since this is static runs, tell the handler so
    handler.set_parameters(use_static = use_static)
    
    # Find the best possible mutation rate
    best_mutation_rate = find_best_mutation_rate(handler)
    with open(filename, "a") as log:
        log.write("\nBest mutation rate found = " + str(best_mutation_rate))
    BaseGenotype.mutation_rate = best_mutation_rate
    
    # Find the best possible crossover rate
    best_crossover_rate = find_best_crossover_rate(handler)
    with open(filename, "a") as log:
        log.write("\nBest crossover rate found = " + str(best_crossover_rate))
    BaseGenotype.crossover_rate = best_crossover_rate
    
    
def beer_extensive_testing(filename = None, object_horizontal_speed = 0, fitness_strategy = "CATCH ALL", runs = 20):
    
    if filename:
        with open(filename , 'a') as log:
            log.write("\nBeer testing commenced at " + str(datetime.now()) + " using fitness strategy " + str(fitness_strategy) + " and object horizontal speed " + str(object_horizontal_speed) + "\n")
    
    BeerHandler.BEHAVIOR = fitness_strategy
    BeerHandler.OBJECT_HORIZONTAL_SPEED = object_horizontal_speed
    BeerHandler.population_size = 100
    
    ParentSelectionStrategy.strategy = "FP"
    AdultSelectionStrategies.strategy = "GM"
    
    BaseGenotype.mutation_rate = 0.2
    BaseGenotype.crossover_rate = 0.8
    
    for _ in range(runs):
        best_individual = simulate(BeerHandler)[0].get_best_individual()
        if filename:
            with open(filename , 'a') as log:
                log.write("Best individual caught " + str(best_individual.get_fitness()) + "/40\n")
            print "Best individual caught " + str(best_individual.get_fitness()) + "/40\n"
    
    
    
    
if __name__ == '__main__':
    # Test the flatland agent using static level generation
    #flatland_parameter_testing("flatland_log.txt")
    # .. and dynamic level generation
    #flatland_parameter_testing("flatland_log.txt", True)
    
    # Do extensive testing on beer agent catching all
    #beer_extensive_testing("beer_log.txt", 0, "CATCH ALL", 20)
    #print "Done with catch all"
    # Then, catching only the larger
    beer_extensive_testing("beer_log.txt" , 0 , "CATCH SMALLER" , 20)
    print "Done with catch smaller"
    # Let's try with horizontal movement on the objects
    #print "Doing with horizontal movement"
    #beer_extensive_testing("beer_log.txt", 1, "CATCH ALL", 20)
    #print "Done with catch all"
    #beer_extensive_testing("beer_log.txt", 1, "CATCH SMALLER", 20)
    #print "Done with catch smaller"