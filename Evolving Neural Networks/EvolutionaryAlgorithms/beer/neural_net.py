'''
Created on 22. mars 2014

@author: Mads
'''

from __future__ import division
import math
import random

class CTRNN:
    
    # Keep the node indices as lists, to ease iteration
    BIAS = [0]
    SENSORY_INPUT= [1,2,3,4,5]
    HIDDEN_LAYER = [6,7]
    MOTOR_OUTPUT = [8,9]
    
    
    def __init__(self):
        # Create the nodes, 10 are needed in total
        self.nodes = [0 for _ in range(10)]
        # 10 nodes will, in theory, yield 100 possible edges. Most of these will be 0 all the time
        self.edges = [[0 for _ in range(10)] for _ in range(10)]
        # All nodes have an internal gain, tough it will be 1 for some nodes
        self.node_gain = [1 for _ in range(10)]
        # All nodes also have a time constant, though it will be 1 for some nodes
        self.time_constants = [1 for _ in range(10)]
        # The bias node always outputs 1
        for bias_node in CTRNN.BIAS:
            self.nodes[bias_node] = 1
        
        
    def get_motor_output(self, sensory_input, print_status = False):
        '''Given sensory inputs, decide on a motor o. None, one or both of the outputs may fire'''
        # First, set the sensor input
        for node in range(len(sensory_input)):
            self.nodes[CTRNN.SENSORY_INPUT[node]] = sensory_input[node]
        # Next, calculate the input to each of the remaining nodes - the S from the assignment text
        s = [0 for _ in range(10)]
        '''   
        for to_node in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
            s[to_node] = sum(map(lambda from_node : self.o(from_node)*self.edges[from_node][to_node] , CTRNN.BIAS+CTRNN.SENSORY_INPUT+CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT))
        '''
        
       
        #Hidden layer:    
        inputsum = self.o(1)*self.edges[1][6]+self.o(2)*self.edges[2][6]+self.o(3)*self.edges[3][6]+self.o(4)*self.edges[4][6]+self.o(5)*self.edges[5][6]
        hiddensum = self.o(6)*self.edges[6][6] + self.o(7)*self.edges[7][6]+self.o(0)*self.edges[0][6]
        '''
        #For task 5
        modification = self.o(8)*self.edges[8][6]+self.o(9)*self.edges[9][6]
        s[6] = inputsum + hiddensum + modification
        '''
        s[6] = inputsum + hiddensum
        
        inputsum = self.o(1)*self.edges[1][7]+self.o(2)*self.edges[2][7]+self.o(3)*self.edges[3][7]+self.o(4)*self.edges[4][7]+self.o(5)*self.edges[5][7]
        hiddensum = self.o(7)*self.edges[7][7] + self.o(6)*self.edges[6][7]+self.o(0)*self.edges[0][7]+self.o(8)*self.edges[8][7]
        '''
        #For task 5
        modification = self.o(8)*self.edges[8][7]+self.o(9)*self.edges[9][7]
        s[7] = inputsum + hiddensum + modification
        #change
        '''
        s[7] = inputsum + hiddensum
        
        #update internal value of hidden nodes
        self.nodes[6] += (1/self.time_constants[6])*(-self.nodes[6]+s[6])
        self.nodes[7] += (1/self.time_constants[7])*(-self.nodes[7]+s[7])
        
        #motor layer:
        hiddensum = self.o(6)*self.edges[6][8]+self.o(7)*self.edges[7][8]
        motorsum = self.o(8)*self.edges[8][8]+self.o(9)*self.edges[9][8]+self.o(0)*self.edges[0][8]
        s[8] = hiddensum + motorsum
        
        hiddensum = self.o(6)*self.edges[6][9]+self.o(7)*self.edges[7][9]
        motorsum = self.o(9)*self.edges[9][9]+self.o(8)*self.edges[8][9]+self.o(0)*self.edges[0][9]
        s[9] = hiddensum + motorsum
        
        #update internal value of motor nodes
        self.nodes[8] += (1/self.time_constants[8])*(-self.nodes[8]+s[8])
        self.nodes[9] += (1/self.time_constants[9])*(-self.nodes[9]+s[9])
        
        '''    
        # Calculate new values of y ( the delta y delta t from the assignment ) 
        for node in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
            self.nodes[node] += 1/self.time_constants[node]*(-self.nodes[node]+s[node]+self.nodes[0]*self.edges[0][node])
        '''
        # Finally, return the motor outputs
        # TODO: Hvilken av disse skal vi bruke?
#         print sensory_input
#         print {'left' : self.o(8), 'right' : self.o(9) , 'left_node' : self.nodes[8] , 'right_node' : self.nodes[9]}
#         print ""
        return {'left' : self.o(8), 'right' : self.o(9) , 'left_node' : self.nodes[8] , 'right_node' : self.nodes[9]}
            
    
    def set_network_weigths(self, weights):
        # We need 26 weights to update the network. They must be in order BIAS, SENSOR, HIDDEN, MOTOR
        if len(weights) >= 26:
            self.set_bias_weights(weights[:4])
            self.set_sensor_weigts(weights[4:14])
            self.set_hidden_layer_weigths(weights[14:22])
            self.set_motor_layer_weights(weights[22:26])
            '''
            weightsCopy = weights[26:30]
            #Add the new set of weights
            self.edges[8][6] = weigths[27]
            self.edges[9][6] = weigths[28]
            self.edges[8][7] = weigths[29]
            self.edges[9][7] = weigths[30]
            '''
            
    
    def set_network_gains(self, gains):
        # We need 4 gains for each of the hidden neurons and the output neurons. They must be in order HIDDEN, MOTOR
        if len(gains) > 3:
            for gn in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
                self.node_gain[gn] = gains.pop(0)
                
    def set_network_time_constants(self, time_constants):
        # We need 4 time constants for each of the hidden neurons and the o neurons. They must be in order HIDDEN, MOTOR
        if len(time_constants) > 3:
            for tc in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
                self.time_constants[tc] = time_constants.pop(0)
            
    
    ##-----INTERNAL HELPING METHODS-----##        
    def o(self, i):
        '''Get the o of node i''' 
        return 1 / (1+math.exp(-self.node_gain[i]*self.nodes[i]))

    def s(self, i):
        '''Get the input to node ii'''
        for from_node in range(10):
            pass

        
        
    
    ##-----SETTERS FOR THE NODE WEIGHTS (INTERNAL METHODS)-----##    
    def set_bias_weights(self, weigths):
        # There are four connections going from the bias node to the hidden- and o layer
        # Set the weights as follows TODO: Check how many weigths are supplied?
        for from_node in CTRNN.BIAS:
            for to_node in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
                self.edges[from_node][to_node] = weigths.pop(0)
                
    def set_sensor_weigts(self, weigths):
        # There are a total of ten connections going from the sensory input to the hidden layer
        # Set the weigths as follows
        for from_node in CTRNN.SENSORY_INPUT:
            for to_node in CTRNN.HIDDEN_LAYER:
                self.edges[from_node][to_node] = weigths.pop(0)
    
    def set_hidden_layer_weigths(self, weights):
        # There are a total of eight connections going from the hidden layer to the hidden layer and the motor o
        for from_node in CTRNN.HIDDEN_LAYER:
            for to_node in CTRNN.HIDDEN_LAYER+CTRNN.MOTOR_OUTPUT:
                self.edges[from_node][to_node] = weights.pop(0)
                
    def set_motor_layer_weights(self, weigths):
        # There are a total of four connections going from the motor o to the motor o
        for from_node in CTRNN.MOTOR_OUTPUT:
            for to_node in CTRNN.MOTOR_OUTPUT:
                self.edges[from_node][to_node] = weigths.pop(0)
    
    
                
                
    def __str__(self, include_weights = False):
        if not include_weights:
            return "CTRNN with\nNode values: " + str(self.nodes) + "\nand motor output left: " + str(self.o(8)) + "\nand motor output right: " + str(self.o(9))
        res = "CTRNN with\nNode values: " + str(self.nodes) + "\nTime constants: " + str(self.time_constants) + "\nGains: " + str(self.node_gain) + "\nand the following weights\n"
        res+= "BIAS: "
        for ii in CTRNN.BIAS:
            for jj in range(10):
                if self.edges[ii][jj] is not 0:
                    res += "\n\t" + str(ii) + "->" + str(jj) + " = " + str(self.edges[ii][jj])
                    
        res += "\nSENSOR INPUT: "
        for ii in CTRNN.SENSORY_INPUT:
            for jj in range(10):
                if self.edges[ii][jj] is not 0:
                    res += "\n\t" +str(ii) + "->" + str(jj) + " = " + str(self.edges[ii][jj])
                    
        res += "\nHIDDEN LAYER: "                   
        for ii in CTRNN.HIDDEN_LAYER:
            for jj in range(10):
                if self.edges[ii][jj] is not 0:
                    res += "\n\t" + str(ii) + "->" + str(jj) + " = " + str(self.edges[ii][jj])
        
        res += "\nMOTOR OUTPUT: "            
        for ii in CTRNN.MOTOR_OUTPUT:
            for jj in range(10):
                if self.edges[ii][jj] is not 0:
                    res += "\n\t" + str(ii) + "->" + str(jj) + " = " + str(self.edges[ii][jj])
        return res
    
if __name__ == '__main__':
    net = CTRNN()
    
    sensor_weights = ['SKRIV 1->6 , 1->7 , 2->6 , ... , 5->7']
    hidden_weights = ['SKRIV 6->6 , 6->7 , 6->8 , ... , 7->9 ']
    motor_weights = ['SKRIV 8->8 , 8->9 , 9->8 , 9->9']
    bias_weights = ['SKRIV BIAS HER']
    gains = ['SKRIV GAINS HER']
    time_constants = ['SKRIV TIME CONSTANTS HER']
    
    net.set_bias_weights(bias_weights)
    net.set_sensor_weigts(sensor_weights)
    net.set_hidden_layer_weigths(hidden_weights)
    net.set_motor_layer_weights(motor_weights)
    net.set_network_gains(gains)
    net.set_network_time_constants(time_constants)

    print net.__str__(True)
    print "Give sensor input in a single bitstring (11111) and press enter"
    while 1:
        sensor_input_text = raw_input("-->")
        sensor_input = map(lambda x : int(sensor_input_text[x]) , range(5))
        net.get_motor_output(sensor_input)
        print net
