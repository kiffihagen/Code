'''
This file contains some utilities which are needed across different parts of the application.
It is not commented as well as the other files..
'''

from random import random as r

def get_int(lower=1, upper = 9999):
    '''Returns a valid integer, or None if the user did not type anything'''
    _input = raw_input('--')
    if _input is '':
        return None
    try:
        value = int(_input)
        if upper < value < lower:
            print "The number must be between " + str(lower) + " and " + str(upper) + " inclusive. Try again."
            return get_int(lower, upper)
        else:
            return value
    except ValueError:
        print "Please type an integer"
        return get_int()
    
def get_float(lower=0, upper=1):
    _input = raw_input('--')
    if _input is '':
        return None
    try:
        value = float(_input)
        if upper < value < lower:
            print "The number must be between " + str(lower) + " and " + str(upper) + " inclusive. Try again."
            return get_float(lower, upper)
        else:
            return value
    except ValueError:
        print "Please type a floating-point number"
        return get_float(lower, upper)

def get_yes_no():
    _input = raw_input('--')
    if _input == 'yes': return True
    elif _input == 'no' or _input == '': return False
    else: 
        print "Type 'yes' or 'no'"
        return get_yes_no()

def roulette_wheel(cummulative_list):
    '''Generate a random number and spin the roulette wheel. Returns the index of the chosen item'''
    try:
        random_number = r()*cummulative_list[-1]
        chosen_index = 0
        while random_number > cummulative_list[chosen_index]:
            chosen_index += 1
        return chosen_index
    except IndexError:
        print "ERROR!"
        print cummulative_list
        
def chunker(sequence, size):
    '''Get a sequence, divide it into chunks of length size and return a list of lists'''
    return [sequence[pos:pos+size] for pos in xrange(0,len(sequence),size)]