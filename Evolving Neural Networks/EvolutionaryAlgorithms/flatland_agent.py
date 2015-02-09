from __future__ import division
from core.individual import BaseIndividual
from core.phenotype import IntegerPhenotype
from core.problem_handler import ProblemHandler
from core.util import get_yes_no
from random import Random as r
from flatland.board import Board
from flatland.ann import ann
import numpy as np
import core.util
from core import util

class FlatlandIndividual(BaseIndividual):
    '''Class for the individual'''
    
    
    def __init__(self, phenotype = None):
        # Call baseclass constructor
        BaseIndividual.__init__(self)
        
        # If a phenotype is supplied, use that
        if phenotype: # TODO: Check if it is an IntegerPhenotype?
            self.phenotype = phenotype
        # If not, create one randomly
        else:
            # To evolve the weights, keep a list of integers and divide them by 100
            weights_needed = 9
            weights_precision = 10**2 # Use 10^i for i decimal places
            # Create the phenotype
            self.phenotype = IntegerPhenotype(weights_needed, weights_precision, 0)
        
        
    def calculate_fitness(self):
        
        # The first step in calculating the fitness is to scale down the weights for the NN
        ann_weigths = map(lambda x : x/100 , self.phenotype.get_int_string()) # TODO: Change '100' if the precision on the weights change
        #print "ANN weights: " + str(ann_weigths)
        
        # Create the ann
        self.ann = ann(ann_weigths)
        
        # The fitness scores for each run is kept in a list
        fitness = []
        
        # Do as many runs as are seeds available
        for seed in FlatlandIndividual.seeds:
            
            # Create the board
            board = Board(seed)
            
            # Count the food and poison on the board before we start
            food_init = board.count_food_poison()
            
            # Do 50 moves
            for _ in range(50):
                    
                # Get the sensor data from the agent
                sensor_data = board.read_sensors()
                
                    
                # Ask the ANN what to do
                action = self.ann.get_Action(sensor_data['left food'], sensor_data['front food'], sensor_data['right food'], sensor_data['left poison'], sensor_data['front poison'], sensor_data['right poison'])
                
                if action is 0: result = board.move_left()
                elif action is 1: result = board.move_forward()
                else: result = board.move_right()
                
                # TODO: Remove print
                #if result is Board.food: print "Ate food"
                #elif result is Board.poison: print "Ate poison"
                #else: print "Didn't eat"
                
                ##----<test_code>----##
                
                #board.show_board_image(seconds_to_show=0)
                
                ##---</test_code>----##
                
            # Count the food and poison on the board after finish
            food_end = board.count_food_poison()
            
            # Calculate the fitness: score 1 point for each food item eaten, and -2 for each poison eaten
            fitness.append(food_init[0]-food_end[0] - 10*(food_init[1]-food_end[1])) # TODO: Experiment with this
            
            # Try another way : percentage of food eaten - percentage of poison eaten
            #fitness.append((food_init[0]-food_end[0])/food_init[0]  -  3*(food_init[1]-food_end[1])/food_init[1])
            
        
            
        # Return the average of the 5 runs
        return max(np.average(fitness),0.01)
    
    
    def breed(self, other_parent):
        # Breed if the other parent can return an integer phenotype
        if 'get_integer_phenotype' in dir(other_parent):
            P1, P2 = self.phenotype.crossover(other_parent.get_integer_phenotype())
            return [ FlatlandIndividual(phenotype=P1) , FlatlandIndividual(phenotype=P2)]
        else:
            print self, other_parent
            raise Exception("Flatland agents must breed with other individuals capable of returning an integer phenotype")
    
    def is_optimal(self):
        # We never know if a robot controller is optimal, unless it is tested on every configuration ever. 
        # Obviously, we don't have the time for that, so return false
        return False
    
    def get_integer_phenotype(self):
        return self.phenotype
    
    def __str__(self):
        return "Flatland agent with neural network weights " + str(map(lambda x : x/100 , self.phenotype.get_int_string()))
    
    def visualize(self, seed = 0, seconds_to_show = 1):
        
        # Create the board
        board = Board(seed)
        # Count the food and poison on the board before we start
        food_init = board.count_food_poison()
        # Show the initial board
        board.show_board_image(seconds_to_show=seconds_to_show)
        # Do 50 moves
        for _ in range(50):
            # Get the sensor data from the agent
            sensor_data = board.read_sensors()
            # Ask the ANN what to do, and do it
            action = self.ann.get_Action(sensor_data['left food'], sensor_data['front food'], sensor_data['right food'], sensor_data['left poison'], sensor_data['front poison'], sensor_data['right poison'])
            if action is 0: board.move_left()
            elif action is 1: board.move_forward()
            else: board.move_right()
            # Show the progress, if applicable
            if seconds_to_show > 0:
                board.show_board_image(seconds_to_show=seconds_to_show)                
            
        # Count the food and poison on the board after finish
        food_end = board.count_food_poison()
        # Prints how many food was eaten and how many 
        print "Ate " + str(food_init[0]-food_end[0]) + " food and " + str((food_init[1]-food_end[1])) + " poison"
        # Return the percentage amount of food and poison eaten
        return [
                (food_init[0]-food_end[0])/food_init[0] , 
                (food_init[1]-food_end[1])/food_init[1] 
                ]
            

class FlatlandHandler(ProblemHandler):

    use_static = False
    
    fitness_map_count = 5
    

    @staticmethod
    def get_initial_population():
        
        # Generate 5 initial seeds for the map generation
        FlatlandIndividual.seeds = [r() for _ in range(FlatlandHandler.fitness_map_count)]
        
        # Create and return the population        
        population = []
        for _ in range(ProblemHandler.population_size):
            population.append(FlatlandIndividual())
        return population
    
    @staticmethod
    def do_intermediate_action():
        # Generate new seeds for the individuals if we run dynamic runs
        if not FlatlandHandler.use_static:
            FlatlandIndividual.seeds = [r() for _ in range(FlatlandHandler.fitness_map_count)]
            
    @staticmethod
    def do_final_action(evolved_population):
        
        # Get the best individual from the population to display it's behaviour
        individual_to_display = evolved_population.get_best_individual()
        
        # If that is not a FlatlandIndividual, raise exception
        if not isinstance(individual_to_display , FlatlandIndividual):
            raise Exception("FlatlandHandler cannot display a final action for anything except FlatlandIndividuals")
        
        # Keep track of the performance
        performance = []
        
        # Do in a loop.. 
        running = True
        while running:
            # For each seed..
            for seed in FlatlandIndividual.seeds:    
                # Ask the user for the delay in showing the images
                print "How many seconds of delay would you like? 0 = user interaction, -1 = quit"
                seconds_to_show = util.get_int(-1,99) or 0
                if seconds_to_show is -1: 
                    running = False
                    break
                performance.append(individual_to_display.visualize(seed = seed, seconds_to_show = seconds_to_show))
                print "So far, robot has eaten " + str(round(np.average(map(lambda x : x[0] , performance))*100,1)) + " % of food and " + str(round(np.average(map(lambda x : x[1] , performance))*100,1)) + " % of poison."
                print performance
            # If all seeds have been run, do intermediate action in flatland handler
            FlatlandHandler.do_intermediate_action()
                            
    @staticmethod
    def get_parameters():
        pass
    
    @staticmethod
    def set_parameters(parameter_dictionary = None, use_static = False, map_count = 5, display_final_agent = False):
        
        # If a parameter dictionary is not supplied, create an empty on so the code below won't crash..
        if not isinstance(parameter_dictionary, dict): parameter_dictionary = dict()
        
        # Set the parent parameters
        ProblemHandler.set_parameters(parameter_dictionary=parameter_dictionary) 
        
        # Set whether or not to use static map generation
        if 'use_static' in parameter_dictionary:
            if parameter_dictionary['use_static']: FlatlandHandler.use_static = True
            else: FlatlandHandler.use_static = False

            
        # Set the number of maps to use for each fitness assessment
        if 'map_count' in parameter_dictionary:
            if isinstance(parameter_dictionary, (int, long)): FlatlandHandler.fitness_map_count = parameter_dictionary['map_count']
        
    @staticmethod
    def interactive_customize():
        # Call the general interactive customizer
        ProblemHandler.interactive_customize()
        
        # Set whether or not to use static runs
        print "Use static runs? Disabled by default"
        FlatlandHandler.use_static = core.util.get_yes_no() or False
        
        
        
        
        
        
if __name__ == '__main__':
    FlatlandIndividual.seeds = [1]
    agent = FlatlandIndividual()
    #print agent.calculate_fitness()
            
        
    