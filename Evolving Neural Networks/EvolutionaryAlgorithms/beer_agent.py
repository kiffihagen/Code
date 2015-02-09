'''
Created on 21. mars 2014

@author: Mads Wilthil
'''
from __future__                 import division
from core.individual            import BaseIndividual
from core.problem_handler       import ProblemHandler
from core.phenotype             import IntegerPhenotype

import core.util

from beer.neural_net            import CTRNN  
from beer.tracker_simulation    import tracker_test_bit_array

class BeerIndividual(BaseIndividual):
    
    def __init__(self, regular_weigths=None, bias_weights=None, gains=None, time_constants=None):
        # Call superclass constructor
        BaseIndividual.__init__(self)
        # A BeerIndividual needs to evolve (1) neural network weights, (2) gains for each of the hidden and output neurons and (3)
        # time constants for each of the hidden and output neurons. This is represented by a range of different phenotypes
        
        # If the parameters were all supplied by the caller, it is a clone
        if regular_weigths and bias_weights and gains and time_constants:
            self.regular_weigths = regular_weigths
            self.bias_weights = bias_weights
            self.gains = gains
            self.time_constants = time_constants
        # If not, create them from scratch
        else:
            # We need twenty two non-bias weights, with a range of -5.0 to 5.0. This is achieved by evolving numbers between 0 and 100, dividing by 10 and deducting 5
            self.regular_weigths = IntegerPhenotype(22,100,0)
            # We need four bias weights, with a range of -10.0 to 0.0. This is achieved by evolving numbers between 0 and 100, dividing by 10 and deducting 10
            self.bias_weights = IntegerPhenotype(4, 100, 0)
            # We need four gains for each of the hidden and output neurons, with a range of 1.0 to 5.0. This is achieved by evolving numbers between 10 and 50, and dividing by 10
            self.gains = IntegerPhenotype(4,50, 10)
            # We need four time constants for each of the hidden and output neurons, with a range of 1.0 to 2.0. This is achieved by evolving numbers between 10 and 20, and dividing by 10
            self.time_constants = IntegerPhenotype(4,20,10)

    def calculate_fitness(self):
        #<test_code>
        # To test the individual, we try to evolve as high sum of weights as possible
        #return sum(self.regular_weigths.get_int_string() + self.bias_weights.get_int_string() + self.gains.get_int_string() + self.time_constants.get_int_string())
        
        
        #</test_code>
        # First, create a neural net based on the weights currently evolved in the agent
        self.neural_net = CTRNN()
        
        # Get the weights to feed into the network
        BW = map(lambda x : x/10-10 , self.bias_weights.get_int_string())
        RW = map(lambda x : x/10-5 , self.regular_weigths.get_int_string())
        # Set the network weights
        self.neural_net.set_network_weigths(BW+RW)
        
        # Next, get the gains and time constants for the hidden and output neurons and set them
        self.neural_net.set_network_gains(map(lambda x : x/10 , self.gains.get_int_string()))
        self.neural_net.set_network_time_constants(map(lambda x : x/10 , self.time_constants.get_int_string()))
        
        # Based on the wanted behavior, call the according fitness function
        if BeerHandler.BEHAVIOR == "CATCH ALL":
            return self.catch_all()
        elif BeerHandler.BEHAVIOR == "CATCH SMALLER":
            return self.catch_smarter()
        elif BeerHandler.BEHAVIOR == "CATCH NONE":
            return self.catch_none()
    
    def breed(self, other_parent):
        # A BeerIndividual can only breed with other BeerIndividuals
        if isinstance(other_parent , BeerIndividual):
            # Create new phenotypes by crossing over each of the children
            nr1,nr2 = self.regular_weigths.crossover(other_parent.regular_weigths)
            nb1,nb2 = self.bias_weights.crossover(other_parent.bias_weights)
            ng1,ng2 = self.gains.crossover(other_parent.gains)
            nt1,nt2 = self.time_constants.crossover(other_parent.time_constants)
            return [ BeerIndividual(nr1,nb1,ng1,nt1) , BeerIndividual(nr2, nb2, ng2, nt2) ]
        else:
            raise Exception("Can't breed\n" + str(self) + "\nwith\n" + str(other_parent))
    
    ##----DIFFERENT FITNESS EVALUATIONS----##
    def catch_all(self):
        '''Encourages catching all falling objects'''
        list_of_result = tracker_test_bit_array(self.neural_net)
        fitness = 0
        for _ , caught in list_of_result:
            if caught is 1:
                fitness += 1
        return fitness
    
    def catch_shorter(self):
        '''Encourage catching only the falling objects which are shorter than the agent'''
        list_of_result = tracker_test_bit_array(self.neural_net)
        fitness = 0
        for object_size, caught in list_of_result:
            if object_size > 4:
                if not caught:
                    fitness += 1
            else:
                if caught:
                    fitness += 1
                '''   
            if (object_size < 5 and caught) or (object_size > 4 and not caught):
                fitness += 1
            else:
                fitness -= 1
                '''
        return max(fitness,0.001)
    
    def catch_smarter(self):
        list_of_result = tracker_test_bit_array(self.neural_net)
        fitness = 0
        #mmax fitness is 40
        for object_size, caught in list_of_result:
                    #SIZE 1
            if object_size is 1:
                if caught is 1:
                    fitness += 1
                else:
                    fitness -= 0
                    #SIZE 2
            elif object_size is 2:
                if caught is 1:
                    fitness += 1
                else:
                    fitness -= 0
                    #SIZE 3
            elif object_size is 3:
                if caught is 1:
                    fitness += 1
                else:
                    fitness -= 0
                    #SIZE 4
            elif object_size is 4:
                if caught is 1:
                    fitness += 1
                else:
                    fitness -= 0
                    #SIZE 5
            elif object_size is 5:
                if (caught is 1) or (caught is -1):
                    fitness -= 1
                else:
                    fitness += 1
            else:
                if (caught is 1) or (caught is -1):
                    fitness -= 1
                else:
                    fitness += 1

        return max(fitness,0.001)
    
    def catch_smartest(self):
        list_of_result = tracker_test_bit_array(self.neural_net)
        correctly_handled = 0
        for object_size , caught in list_of_result:
            if (object_size < 5 and caught is 1) or (object_size > 4 and caught is 0):
                correctly_handled += 1
        return correctly_handled / len(list_of_result)
    
    def catch_none(self):
        list_of_result = tracker_test_bit_array(self.neural_net)
        fitness = 0
        for object_size, caught in list_of_result:
            if not caught:
                fitness += 1
        return fitness
    
    
    ##----INTERNAL METHODS----##
    # None yet--
    
    def __str__(self):
        res = "BeerIndividual with fitness " + str(self.get_fitness()) + " and the following weights:\n"
        res += "Regular weights: " + str(map(lambda x : x/10-5 , self.regular_weigths.get_int_string())) + "\n"
        res += "Bias weights: " + str(map(lambda x : x/10-10 , self.bias_weights.get_int_string())) + "\n"
        res += "Gains: " + str(map(lambda x : x/10 , self.gains.get_int_string())) + "\n"
        res += "Time constants: " + str(map(lambda x : x/10 , self.time_constants.get_int_string())) + "\n"
        return res
    
    
class BeerHandler(ProblemHandler):
    '''Hold my beer...'''
    
    # The behavior wanted by the neural net. Either CATCH ALL or CATCH SMALLER
    BEHAVIOR = "CATCH ALL"
    
    # The vertical speed of falling objects
    OBJECT_HORIZONTAL_SPEED = 0
    
    @staticmethod
    def get_initial_population():
        # Just create as many BeerIndividuals as are specified by the ProblemHandler
        return [BeerIndividual() for _ in range(ProblemHandler.population_size)]
    
    @staticmethod
    def interactive_customize():
        
        # Call superclass customizer
        ProblemHandler.interactive_customize()
        
        # Set the horizontal speed of falling objects
        print "Set a horizontal speed of the falling objects. Default is 0"
        BeerHandler.OBJECT_HORIZONTAL_SPEED = core.util.get_int(-1, 1) or 0
        
        # Set the catching strategy
        print "Set a strategy for catching the objects. Defalut is \"Catch shorter\"\n1. \"Catch shorter\"\n2. \"Catch all\"\n3. \"Catch none\""
        inp = core.util.get_int(1, 2) or 1
        if inp is 1:    BeerHandler.BEHAVIOR = "CATCH SMALLER"
        elif inp is 2:  BeerHandler.BEHAVIOR = "CATCH ALL"
        else:           BeerHandler.BEHAVIOR = "CATCH NONE"
        
        
    @staticmethod
    def do_final_action(evolved_population):
        # Get the best individual from the evolved population
        best_ind = evolved_population.get_best_individual()
        print "Enter the time to show, -1 to quit"
        input_ = core.util.get_float(-1, 10)
        while input_ > -1:
            caught = tracker_test_bit_array(best_ind.neural_net, object_count = 1, object_horizontal_velocity=BeerHandler.OBJECT_HORIZONTAL_SPEED, min_horizontal_heigth=15, max_horizontal_height=15, time_to_show=input_)[0]
            print "Dropped object of size " + str(caught[0]) + " and it was " +str({1 : "a catch" , 0 : "a miss" , -1 : "a hit"}[caught[1]])
            print "Enter the time to show, -1 to quit"
            input_ = core.util.get_float(-1, 10)

'''CODE FAIL!
from core.population import Population
class BeerDuoPopulation(Population):
    
    def __init__(self, initial_population_one, initial_population_two):
        self.pop_one = Population(initial_population_one)
        self.pop_two = Population(initial_population_two)
        
    def evolve(self, generations = 1):
        BeerHandler.BEHAVIOR = "CATCH ALL"
        self.pop_one.evolve(generations)
        BeerHandler.BEHAVIOR = "CATCH NONE"
        self.pop_two.evolve(generations)    
    
    def get_optimal_individual(self):
        return [self.pop_one.get_optimal_individual() , self.pop_two.get_optimal_individual()]
    
    def get_best_individual(self):
        return [self.pop_one.get_best_individual() , self.pop_two.get_best_individual()]
'''    
        
# TEST CODE:
if __name__ == '__main__':
    # import
    bi = BeerIndividual()
    print bi
