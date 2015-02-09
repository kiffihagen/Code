# Do your thing!
from random import random
import math

class ann:
    
    def __init__(self,weights):
        
        self.weights = weights
        
        self.nodes = dict()
        self.myWeights = dict()
        
        self.nodes['left food input'] = 0
        self.nodes['middle food input'] = 0
        self.nodes['right food input'] = 0
        
        self.nodes['left poison input'] = 0
        self.nodes['middle poison input'] = 0
        self.nodes['right poison input'] = 0
        
        self.nodes['left hidden value'] = 0
        self.nodes['middle hidden value'] = 0
        self.nodes['right hidden value'] = 0
        
        self.nodes['left memory value'] = 0
        self.nodes['right memory value'] = 0
        self.nodes['forward bias'] = 1
        
        self.nodes['left output'] = 0
        self.nodes['middle output'] = 0
        self.nodes['right output'] = 0
        
        
        #set up weights
        self.myWeights['left food input to left sum node'] = weights[0]
        self.myWeights['middle food input to middle sum node'] = weights[1]
        self.myWeights['right food input to right sum node'] = weights[2]
        
        self.myWeights['left poison input to left sum node'] = weights[3]
        self.myWeights['middle poison input to middle sum node'] = weights[4]
        self.myWeights['right poison input to right sum node'] = weights[5]
        
        self.myWeights['left memory input to left sum node'] = weights[6]
        self.myWeights['right memory input to right sum node'] = weights[7]
        '''Add read value here'''
        self.myWeights['forward bias to middle sum node'] = weights[8]
        
        
    
    def get_Action(self,food_left,food_middle,food_right,poison_left,poison_middle,poison_right):
        '''Returns the robots action for this timestep and updates the ANN'''
        '''Not sure of the format of the input'''
        
        #Input layer
        self.nodes['left food input'] = food_left
        self.nodes['middle food input'] = food_middle
        self.nodes['right food input'] = food_right
        
        self.nodes['left poison input'] = poison_left
        self.nodes['middle poison input'] = poison_middle
        self.nodes['right poison input'] = poison_right
        
        #Middle layer
        self.nodes['left hidden value'] = self.nodes['left food input']*self.myWeights['left food input to left sum node'] - self.nodes['left poison input']*self.myWeights['left poison input to left sum node']+self.nodes['left memory value']*self.myWeights['left memory input to left sum node']                                 
        self.nodes['middle hidden value'] = self.nodes['middle food input']*self.myWeights['middle food input to middle sum node'] - self.nodes['middle poison input']*self.myWeights['middle poison input to middle sum node']+self.nodes['forward bias']*self.myWeights['forward bias to middle sum node']
        self.nodes['right hidden value'] = self.nodes['right food input']*self.myWeights['right food input to right sum node'] - self.nodes['right poison input']*self.myWeights['right poison input to right sum node']+self.nodes['right memory value']*self.myWeights['right memory input to right sum node']

        '''Selection of action:'''
        self.select_Action()
        
        '''Update memory self.nodes'''
        self.nodes['left memory value'] = self.nodes['left food input']*self.nodes['middle output'] + self.nodes['middle food input']*self.nodes['right output']
        self.nodes['right memory value'] = self.nodes['right food input']*self.nodes['middle output'] + self.nodes['middle food input']*self.nodes['left output']
        
        #print "Hidden layer node inputs\nLeft: " + str(self.nodes['left hidden input']) + "\nRight: " + str(self.nodes['right hidden input']) + "\nMiddle: " + str(self.nodes['middle hidden input'])
        #print "Memory layer:\nLeft: " + str(self.nodes['left memory input']) + "\nRight: " + str(self.nodes['right memory input'])
        
        return self.return_Format()
        
    
            
    def select_Action_Stochastic(self):
        self.nodes['left output'] = 0
        self.nodes['middle output'] = 0
        self.nodes['right output'] = 0
        
        
        
        #making it all positive numbers
        leftInput = math.exp(self.nodes['left hidden value'])
        middleInput = math.exp(self.nodes['middle hidden value'])
        rightInput = math.exp(self.nodes['right hidden value'])
            
        
        #now scaling the values
        left = math.pow(leftInput, 10)
        middle = math.pow(middleInput, 10)
        right = math.pow(rightInput, 10)
        sum = left+middle+right
        
        
        
        selector = random()*sum
        #print "Selector numbers was " + str(selector) + " when sum was " + str(sum)
        #print "Value for left: < " + str(leftValue)
        #print "Value for middle: < " + str(leftValue+middleValue)
        #print "Value for right: < " + str(leftValue+middleValue+rightValue)
        if selector>=left+middle:
            self.nodes['right output'] = 1
        elif selector>=left:
            self.nodes['middle output'] = 1
        else:
            self.nodes['left output'] = 1
        
    def select_Action(self):
        '''No randomization'''
        self.nodes['left output'] = 0
        self.nodes['middle output'] = 0
        self.nodes['right output'] = 0
          
        
        #making it all positive numbers, also scaling values
        leftInput = math.exp(self.nodes['left hidden value'])
        middleInput = math.exp(self.nodes['middle hidden value'])
        rightInput = math.exp(self.nodes['right hidden value'])
        
        #print "Simple action selector:"
        #print "Value for left: "+str(leftInput)
        #print "Value for middle: " + str(middleInput)
        #print "Value for right: " + str(rightInput)
        
        selector = self.get_Highest(leftInput,middleInput,rightInput)

        if selector is 0:
           self.nodes['left output'] = 1
        elif selector is 1:
            self.nodes['middle output'] = 1
        elif selector is 2:
            self.nodes['right output'] = 1
        else:
            '''Pick random'''
            self.set_Random()
            
       
       
        
    def set_Random(self):
        '''Sets ONE random outputnode to 1, equal chances'''
        selector = random()
        if selector<(1/3):
            self.nodes['left output'] = 1
        elif selector<(2/3):
            self.nodes['middle output'] = 1
        else:
            self.nodes['right output'] = 1

    def return_Format(self):
        '''Can be changed to "0,1,2" , "left,middle,right" etc.'''
        if self.nodes['left output'] is 1:
            return 0
        if self.nodes['middle output'] is 1:
            return 1
        if self.nodes['right output'] is 1:
            return 2
        
    def get_Highest(self,left,middle,right):
        '''Returns highest number, or 3 if all is equal'''
        if left > right and left > middle:
            return 0
        elif right>middle:
            return 2
        elif middle>right:
            return 1
        else:
            return 3
        
            
        
        