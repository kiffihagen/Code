import random
from random import random as r
import ImageTk #,Image
from PIL import Image
import Tkinter


class Board:
    '''Supporting class for the flatland agent'''
    
    # Declare some constants for now
    empty = 0
    food = 1
    poison = 2
    robot = 3
    
    def __init__(self, map_generation_seed=random.random(), N = [8,8], FPD = [0.5,0.5]):
        
        # Get the state of the random generator to reset it afterwards
        initial_state = random.getstate() 
        # Set the random generator to the supplied seed
        random.seed(map_generation_seed)
        
        # Set the size of the board
        self.size = N
        # Create a board - I use a list representation
        self.board = [[Board.empty for _ in range(self.size[0])] for _ in range(self.size[1])]
        # Fill the board with food and poison
        for ii in range(len(self.board)):
            for jj in range(len(self.board[ii])):
                if r() < FPD[0]: self.board[ii][jj] = Board.food
                elif r() < FPD[1]: self.board[ii][jj] = Board.poison
        
        # Place the agent approximately in the middle of the board, facing up
        self.robot_facing = [-1,0]
        self.robot_pos = [self.size[0]/2,self.size[1]/2]
        self.board[self.size[0]/2][self.size[1]/2] = Board.robot
                
        # The board is created, so return the generator to its previous state
        random.setstate(initial_state)
        
    def move_forward(self):
        '''Move forward and return whatever the robot picked up'''
        
        # Since the agent wraps around, get the true location of the cell in front
        moving_to = self.get_front_cell_indices()
        # Record what the agent is picking up
        picked_up = self.board[moving_to[0]][moving_to[1]]
        # Set the new cell to a robot
        self.board[moving_to[0]][moving_to[1]] = Board.robot
        # Set the old cell to empty
        self.board[self.robot_pos[0]][self.robot_pos[1]] = Board.empty
        # Set the new position - remember to wrap around
        self.robot_pos[0] = moving_to[0]
        self.robot_pos[1] = moving_to[1]
        # Return what we picked up
        return picked_up
        
    def turn_left(self):
        # Just a basic switch for now
        if self.robot_facing == [-1,0]: self.robot_facing = [0,-1]
        elif self.robot_facing == [0,-1]: self.robot_facing = [1,0]
        elif self.robot_facing == [1,0]: self.robot_facing = [0,1]
        elif self.robot_facing == [0,1]: self.robot_facing = [-1,0]
            
    def move_left(self):
        '''Move to the left and return whatever the robot picked up'''
        # Simple enough..
        self.turn_left()
        return self.move_forward()

    def turn_right(self):
        # Lazy mans turn
        for _ in range(3):
            self.turn_left()
    
    def move_right(self):
        # Simple enough..
        self.turn_right()
        return self.move_forward()
    
    def read_sensors(self):
        # TODO: decide a sensor protocol. I use a dict = {'front food' : True/False , 'front poison' : True/False ... } for now
        result = dict()
        # Turn left, and read the front sensors. Store the result in the dict
        self.turn_left()
        result['left food'] = self.read_front_sensors()[0]
        result['left poison'] = self.read_front_sensors()[1]
        # Turn back to the front and read the front sensors
        self.turn_right()
        result['front food'] = self.read_front_sensors()[0]
        result['front poison'] = self.read_front_sensors()[1]
        # Turn right and read from the front sensors
        self.turn_right()
        result['right food'] = self.read_front_sensors()[0]
        result['right poison'] = self.read_front_sensors()[1]
        # Turn back facing forward
        self.turn_left()
        # Return the sensor data
        return result
        
    def read_front_sensors(self):
        '''Read the front sensors and return them as a pair where read_front_sensor[0] is the food and read_front_sensor[1] is the poison'''
        result = [False,False]
        front_cell = self.get_front_cell_indices()
        if self.board[front_cell[0]][front_cell[1]] is Board.food:
            result[0] = True
        if self.board[front_cell[0]][front_cell[1]] is Board.poison:
            result[1] = True
        return result
    
    def get_front_cell_indices(self):
        '''Since the agent wraps around, this help returning the true indices of the cell in front of the agent'''
        return map(lambda x : (self.robot_pos[x] + self.robot_facing[x]) % self.size[x] , range(2))
    
    def count_food_poison(self):
        '''Count the food and poison currently on the board, and returns [food, poison] as a list'''
        res = [0,0]
        for row in self.board:
            for fp in row: 
                if fp is Board.food: res[0] += 1
                elif fp is Board.poison: res[1] += 1
        return res
    
    def __str__(self):
        res = ""
        for row in self.board:
            res += row.__str__()+"\n"
        return res
    
    def show_board_image(self, seconds_to_show = 2):
        # The size of the cells in pixels, assumed to be a square
        img_size = 32
        
        # The resulting image
        result = Image.new("RGBA", (self.size[0]*img_size,self.size[1]*img_size))
        for ii in range(self.size[0]):
            for jj in range(self.size[1]):
                # Get the correct image path depending on the content of the image
                if self.board[ii][jj] is Board.food:
                    img_path = 'food.jpg'
                elif self.board[ii][jj] is Board.poison:
                    img_path = 'poison.jpg'
                elif self.board[ii][jj] is Board.empty:
                    img_path = 'empty.jpg'
                elif self.board[ii][jj] is Board.robot:
                    if self.robot_facing == [-1,0]:
                        img_path = 'robot_up.jpg'
                    elif self.robot_facing == [1,0]:
                        img_path = 'robot_down.jpg'
                    elif self.robot_facing == [0,1]:
                        img_path = 'robot_right.jpg'
                    else:
                        img_path = 'robot_left.jpg'
                # Paste the image into the current result
                result.paste(Image.open("flatland/"+img_path) , (jj*img_size, ii*img_size))
        
        # The image is complete; display it to the user for the desired time

        # Create the root and set it's size to one that fits
        root = Tkinter.Tk()
        root.geometry(str(img_size*self.size[1]+5) + "x" + str(img_size*self.size[0]+5))
        # Create a Tk-compatible image from the PIL Image
        tkpi = ImageTk.PhotoImage(result)
        # Create the label, and place it in the upper left corner. Also, give a proper title
        label_image = Tkinter.Label(root, image=tkpi)
        label_image.place(x=0,y=0)
        root.title('Flatland agent')
        root.after(int(seconds_to_show*1000), root.destroy)
        
        # Run the mainloop
        root.mainloop()
        
        
        

#----TEST CODE----#
if __name__ == '__main__':
    
    bo = Board()
    print bo
    for _ in range(8):
        bo.move_forward()
        bo.show_board_image()
