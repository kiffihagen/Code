'''
The program starts here. This is based on user interaction. To simulate several runs, use simulate.py
'''
from one_max                import OneMaxHandler                                # Needed to start one-max
from surprising_sequences   import SurprisingSequencesHandler                   # Needed to start surprising sequences
from flatland_agent         import FlatlandHandler                              # Needed to start the flatland agent
from beer_agent             import BeerHandler

from core.strategies import AdultSelectionStrategies, ParentSelectionStrategy   # Needed to choose selection strategies
from core.genotypes import BaseGenotype                                         # Needed to customize mutation / crossover rate
from core.population import Population                                          # Needed to create the population and evolve it

import core.util as util                                                        # Used for various tasks - mainly input
import matplotlib.pyplot as plt                                                 # Used to plot the max and average fitness after the run

def choose_problem():
    '''Choose a problem, and return the problem-specific handler. This needs to be updated in the future when new problems are added'''
    
    print "Choose a problem:\n1. One-max problem (default)\n2. Surprising sequences\n3. The flatland agent\n4. The beer agent"
    problem = util.get_int(1,4) or 1
    
    if problem is 1:
        return OneMaxHandler
    
    elif problem is 2:
        return SurprisingSequencesHandler
    
    elif problem is 3:
        return FlatlandHandler
    
    elif problem is 4:
        return BeerHandler
    
    else:
        print "Are you sure you did this right? This should never happen due to \nproblem=util.get_int(1,4) or 1\nabove"

def set_strategies(population_size):
    '''Set the parent- and adult selection strategy. If a strategy has parameters to change, you get to choose those as well!
    
    In the future, this should probably be handled by a StrategyHandler or something similar..'''
    
    print "Choose adult selection strategy:\n1. Full generational replacement (default)\n2. Over-production\n3. Generational mixing"
    adult_strat = util.get_int(1,3) or 1
    
    if adult_strat is 2:
        AdultSelectionStrategies.strategy = "OP"
        print "Set how many children to produce, as a factor of the current population (default=" + str(AdultSelectionStrategies.over_production_factor) + ")"
        AdultSelectionStrategies.over_production_factor = util.get_float(1, 10) or AdultSelectionStrategies.over_production_factor
        
    elif adult_strat is 3:
        AdultSelectionStrategies.strategy = "GM"
        print "Set how many children to produce, as a factor of the current population (default=" + str(AdultSelectionStrategies.generational_mixing_factor) + ")"
        AdultSelectionStrategies.generational_mixing_factor = util.get_float(1, 10) or AdultSelectionStrategies.over_production_factor
        
    print "Choose parent selection strategy:\n1. Fitness proportionate (default)\n2. Sigma scaling\n3. Tournament selection\n4. Rank selection"
    parent_strat = util.get_int(1, 4) or 1
    
    if parent_strat is 2:
        ParentSelectionStrategy.strategy = "SS"
        
    elif parent_strat is 3:
        ParentSelectionStrategy.strategy = "TS"
        print "Set epsilon for tournament selection (default=" + str(ParentSelectionStrategy.tournament_selection_epsilon) + ")"
        ParentSelectionStrategy.tournament_selection_epsilon = util.get_float() or ParentSelectionStrategy.tournament_selection_epsilon
        print "Set K for tournament selection (default=" + str(ParentSelectionStrategy.tournament_selection_K) + ")"
        ParentSelectionStrategy.tournament_selection_K = util.get_int(2,population_size) or ParentSelectionStrategy.tournament_selection_K
        
    elif parent_strat is 4:
        ParentSelectionStrategy.strategy = "RS"
    
def set_parameters():
    '''Set the mutation- and crossover rate for the problem.
    
    In the future, this might be handled by the ProblemHandler...'''
    
    print "Set the mutation rate (default=" + str(BaseGenotype.mutation_rate) + ")"
    BaseGenotype.mutation_rate = util.get_float() or BaseGenotype.mutation_rate
    
    print "Set the crossover rate (default=" + str(BaseGenotype.crossover_rate) + ")"
    BaseGenotype.crossover_rate = util.get_float() or BaseGenotype.crossover_rate
    
    
def plot_history(history):
    '''Plot the history and show a graph'''
    # Create an x-axis based on the age of the population. 
    generation = range(len(history))
    
    # Get the fitness values
    max_f = map(lambda x : x[0] , history)
    avg_f = map(lambda x : x[1] , history)
 
    # Add the max and average fitness as a blue and red line respectively
    plt.plot(generation, max_f, 'b-', label='Max fitness')
    plt.plot(generation, avg_f, 'r-', label='Average fitness')
    
    # Set the axis, add the labels and show the plot
    plt.axis([0, len(generation), min(avg_f)*0.9 ,max(max_f)*1.1])
    plt.legend(loc='lower right')
    plt.show()
    

def main():
    
    # Choose a problem and population size, and get the ProblemHandler for that problem
    problem_handler = choose_problem()
    
    # Since this is an interactive main program, do an interactive customize 
    problem_handler.interactive_customize()
    
    # The program should be all set now. Get the initial population
    population = problem_handler.get_initial_population()
    
    # Time to customize. Change the strategies first
    print "---------------------------\nTime to customize. \nPress enter to accept the default values. \nOtherwise, enter a number as requested"
    set_strategies(len(population))
    
    # Change the parameters next. The popuation size must be given, to specify an upper bound on some parameters (K in tournament selection for now)
    set_parameters()
    
    print "Set max number of generations (default = 200)"
    generations = util.get_int(0, 1000000000) or 200
    
    # Time to start the loop itself. Set a generation counter and take the population from a list of individuals to an actual population
    population = Population(population)
    generation = 0
    while generation < generations:
        generation += 1
        
        # Print an update every 100th generation, to keep track of the progress
        if generation % 10 is 0 or generation is 1:
            print "Starting generation " + str(generation)
        # Evolve the population
        population.evolve()
        #print map(lambda x : str(x) , population.population) # TODO: Remove print
        # Call the handler to update any instance-independent updates
        problem_handler.do_intermediate_action()
        #print population.get_best_individual().get_fitness()
        
        # If an optimal individual is found, break the search
        if population.get_optimal_individual():
            print "Optimal individual found in generation " + str(generation) + "! Ending evolution."
            break
    # No matter how the loop ended, show the best individual found
    print "Done iterating. Best individual = " + str(population.get_best_individual())
    # Do the final action for the handler
    problem_handler.do_final_action(population)
    # Show the fitness history as a graph to the user
    plot_history(population.fitness_history)
    
    
# Run main to start it all
if __name__ == '__main__':    
    main()