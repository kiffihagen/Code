import itertools    # Handy tools
import numpy as np  #    -''-
from strategies import AdultSelectionStrategies # To use the strategies to evolve the population
from core import individual                     # To access the fitness evaluation of the indiviudal

class Population:
        
    def __init__(self, initial_population):
        
        # Set the initial population, as well as an empty history
        self.population = initial_population
        self.fitness_history = []
        
        # Log the first generation in the fitness history
        self.log()
        
        # Find the best initial individual and set it as such
        fitness = map(lambda x : x.get_fitness() , self.population)
        self.best_individual = self.population[fitness.index(max(fitness))]
    
    
    def evolve(self, generations=1):
        '''This is where the magic happens.. Evolves the population by fetching a new population from the strategies, and logging the new population'''
        
        for _ in itertools.repeat(None, generations):
            # Get the next generation from the strategies
            self.population = AdultSelectionStrategies.get_next_generation(self)
            for individual in self.population:
                individual.fitness = individual.calculate_fitness()
            
            # Record the new generation in the fitness history
            self.log()
            
            # Check if any of the individuals are better than the best, and store them if it is
            for individual in self.population:
                if individual.get_fitness() > self.best_individual.get_fitness():
                    self.best_individual = individual
            
            
            
    def log(self):
        '''Logs the current population stats in the population history. The history is stored as triples (max_fitness, avg_fitness, std)'''
        
        # Get the fitness values as a simple list
        if not self.population:
            pass # TODO: Remove after debugging
        fitness_list = map(lambda x: x.get_fitness() , self.population)
        # Add a triplet of values to the fitness history - easy thanks to numpy
        self.fitness_history.append((np.max(fitness_list) , np.average(fitness_list) , np.std(fitness_list)))
    
    def get_optimal_individual(self):
        '''If an optimal individual has been found, it is stored in the field of the best individual. Return None if no optimal individual exists'''
        if self.best_individual.is_optimal():
            return self.best_individual
        else:
            return None
        
    def get_best_individual(self):
        '''Get the best individual the population has seen so far (including past generations)'''
        return self.best_individual
        
