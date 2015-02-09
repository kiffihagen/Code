'''
Created on 21. mars 2014

@author: Mads
'''
from __future__ import division
import random
import math
import ImageTk #,Image
from PIL import Image
import Tkinter

CAUGHT_TRESHOLD = 0.8

def tracker_test_bit_array(neural_net, object_count = 40, min_horizontal_heigth = 15, max_horizontal_height = 15, object_horizontal_velocity = 0, time_to_show = 0):    
    # TODO: Include agent_array width, board width, and object min- and max width in function definition?
    # TODO: Include wrap-around
    agent_width = 5
    arena_width = 30
    min_object_width, max_object_width = 1, 6
    '''
    Does the same as above, but uses bit arrays to represent the agent_array and object. Simpler, but slower code
    '''
    # The result of this method is a list with tuples, containing the object size, and whether or not it was sucessfully caught
    resulting_list = []
    # To start of, select a starting location for the agent_array. Note that it will wrap around, so no restriction is needed on the upper bound
    '''TODO: Make this deterministic'''
    agent_location = random.randint(0,arena_width)
    
    if object_count is 1:
        object_widths = [random.randint(min_object_width, max_object_width)]
    else:
        object_widths = map(lambda x : (x % 6)+1 , range(object_count))
    
    random.shuffle(object_widths)
    
    # Do once for each object..
    for object_width in object_widths:#range(object_count):
        # Set a random object width and location
        object_location = random.randint(0,arena_width)
        #object_width = random.randint(min_object_width, max_object_width)

        # Create the agent_array and object array
        agent_array = [0 for _ in range(arena_width)]
        for ii in range(agent_location, agent_location+agent_width):
            agent_array[ii%arena_width] = 1
        object_array = [0 for _ in range(arena_width)]
        for ii in range(object_location, object_location+object_width):
            object_array[ii%arena_width] = 1
            
            
        # Count down from a random object height
        initial_height = random.randint(min_horizontal_heigth,max_horizontal_height)
        for time in range(initial_height)[::-1]:
            # Show the setup if show is enabled
            if time_to_show:
                board = []
                # Append empty lines at the top of the board
                for _ in range(initial_height-time):
                    board.append([0 for _ in range(arena_width)])
                # Append the object 
                board.append(object_array)
                # Append the gap between the object and agent
                for _ in range(time):
                    board.append([0 for _ in range(arena_width)])
                board.append(agent_array)
                #if time_to_show:
                 #   print neural_net
                visualize(board , time_to_show)
            # If the time is up, determine if the object is caught or avoided
            if time is 0:
                
                overlapping_pieces = sum(map(lambda x : object_array[(agent_location+x)%arena_width] and agent_array[(agent_location+x)%arena_width] , range(agent_width)))
                # If it is caught append the following to the list
                if overlapping_pieces / object_width >= CAUGHT_TRESHOLD:
                    resulting_list.append((object_width , 1))
                # If it is avoided append the following to the list
                elif overlapping_pieces == 0:
                    resulting_list.append((object_width , 0))
                # If it is a hit, but not a catch, append -1
                else:
                    resulting_list.append((object_width , -1))
            # If the time is not up, move the object and the agent
            else:
                # Move the object. Just create new arrays now, TODO: Improve this..
                object_location += object_horizontal_velocity
                object_array = [0 for _ in range(arena_width)]
                for ii in range(object_location, object_location+object_width):
                    object_array[ii%arena_width] = 1
                # The agent movement is determined by the neural net
                sensor_data = []
                for ii in range(agent_width):
                    if object_array[(agent_location+ii)%arena_width]:   sensor_data.append(1)
                    else:                                               sensor_data.append(0)
                
                # Get the output, and scale it to an integer between 4 and 0
                move_dict = neural_net.get_motor_output(sensor_data)
                if time_to_show:
                    print move_dict
                dr, dl = move_dict['right'] , move_dict['left']
                move = round((dr-dl)*4)
                
                    
                
                #modified
                if dr > 0.2:
                    dr = 4
                elif dr > 0.15:
                    dr = 3
                elif dr > 0.1:
                    dr = 2
                elif dr > 0.05:
                    dr = 1
                else:
                    dr = 0
                
                if dl > 0.2:
                    dl = 4
                elif dl > 0.15:
                    dl = 3
                elif dl > 0.1:
                    dl = 2
                elif dl > 0.05:
                    dl = 1
                else:
                    dl = 0
                move = dr-dl
                
                # Given that the difference between the output is greater than 1
                agent_location += int(move)
                
                agent_array = [0 for _ in range(arena_width)]
                for ii in range(agent_location, agent_location+agent_width):
                    agent_array[ii%arena_width] = 1
                
            
    # Return the resulting list
    return resulting_list

def visualize(board, seconds_to_show = 1):
    # The size of the cells in pixels, assumed to be a square
    img_size = 32
    
    # The resulting image
    result = Image.new("RGBA", (len(board[0])*img_size, len(board)*img_size))
    for row in range(len(board)):
        for col in range(len(board[row])):
            if board[row][col] is 0:
                img_path = 'empty'
            elif board[row][col] is 1 and row < len(board)-1:
                img_path = 'object'
            elif board[row][col] is 1:
                img_path = 'agent'
            img_path = 'beer/' + img_path + '.jpg'
            #img_path += '.jpg'
            result.paste(Image.open(img_path) , (col*img_size, row*img_size))
    
    # The image is complete; display it to the user for the desired time
    # Create the root and set it's size to one that fits
    root = Tkinter.Tk()
    root.geometry(str(img_size*len(board[0])+5) + "x" + str(img_size*len(board)+5))
    # Create a Tk-compatible image from the PIL Image
    tkpi = ImageTk.PhotoImage(result)
    # Create the label, and place it in the upper left corner. Also, give a proper title
    label_image = Tkinter.Label(root, image=tkpi)
    label_image.place(x=0,y=0)
    root.title('Beer agent')
    # Set up self-destruct..
    root.after(int(seconds_to_show*1000), root.destroy)
    # Run the mainloop
    root.mainloop()

if __name__ == '__main__':
    visualize([[0,0,0,0,0,0,0,0],[0,0,0,0,0,1,1,0],  [0,0,0,0,0,0,0,0] ,  [0,0,0,0,0,0,0,0] , [0,0,1,1,1,1,1,0]] ,seconds_to_show = 10)
    
    
    
    pass
        
        
    
    
