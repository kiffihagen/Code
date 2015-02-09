# -*- coding: cp1252 -*-
import random
import Image               # An extra Python module (that you'll have to download)
import imagepro            # A module provided by Keith Downing for this assignment


from epuck_basic import EpuckBasic

class MyEpuck(EpuckBasic):
###### Attributes
  moveTime = 1                  # Determines how long to call 'move' in EpuckBasic
  left_wheel_speed = 0          # Determines how to set left wheel speed in EpuckBasic
  right_wheel_speed = 0         # Determines how to set right wheel speed in EpuckBasic
  foundBox = False              # Used to tell if the Epuck has found the box it is looking for
  converge = False              # Used to tell if the Epuck is convergig towards a box
  push = False                  # Used to tell if the Epuck is at a box, pushing it
  LED = []                      # For easy access to the LEDs on the Epuck
  foundBoxLoc = [0,0,0,0,0]     # The pixels across the camera in which a colored pixel was found 
  preferRED = 1                 # 1 if this Epuck wants to push a red box
  foundBlueBox = False          # True if this Epuck has found the blue box
  foundBlueBoxLoc = [0,0,0,0,0] # Determines where (in its vision) the Epuck sees a blue box
  foundRedBoxLoc = [0,0,0,0,0]  # Determines where (in its vision) the Epuck sees a red box
  foundRedBox = False           # True if this Epuck has found the red box
  confidence_value = 1          # A meassure of how good of a position this Epuck is in. Used in stagnation, and describes how likely it is for the Epuck to keep pushing
  standing_still = 0            # The number of rounds this Epuck has stood still in a row. Used to resolve cliques
  
#################################### LED, CAMERA ETC.
  
  ## Looks around, to determine if this Epuck wants to search
  ## for (and thus push) the red or blue food box. 
  def forceSurvey(self):
    returnvalue = 0
    colors = [0,0]
    self.moveTime = 0.032
    rand = random.random()
    if rand < 0.5:
        self.left_wheel_speed = .15
        self.right_wheel_speed = -.15
    else:
        self.left_wheel_speed = -.15
        self.right_wheel_speed = .15

    self.set_wheel_speeds(self.left_wheel_speed,self.right_wheel_speed)
    
    for i in range(360):
      self.do_timed_action(self.moveTime)
      self.takeGeneralPhoto(1)
      if self.foundBox:
        colors[0] = 1
      self.takeGeneralPhoto(0)
      if self.foundBox:
        colors[1] = 1

    if colors[1] is 0:
      print "No blue food found, I prefer RED"
      returnvalue = 1
    elif colors[1] is 1 and colors[0] is 0:
      print "No red food found, I prefer BLUE"
      returnvalue = 0
    else:
      rand = random.random()
      if rand < 0.5:
        returnvalue = 1
        print "Randomed red food, I prefer RED"
      else:
        returnvalue = 0
        print "Randomed blue food, I prefer BLUE"
          
    return returnvalue

  ## Helper, for turning off all the LEDs
  def reset_LED(self):
      for i in range(0,8):
        self.LED[i].set(0)

  ## Helper, for initiating easy access to the LEDs
  def init_LED(self):
      for i in range(0,8):
        self.LED.append(self.getLED("led"+str(i)))
        self.LED[i].set(0)

  ## Implementation of sum(list)..
  def sumFinds(self,inList):
      a = 0
      for i in inList:
        a = a + i
      return a

  ## Takes a photo, and determines if it can spot any of the boxes. 
  def takeGeneralPhoto(self,preferRED):


      image = self.snapshot()

      # 13,14,18:20 works
      # 37:20 always on, 38:20 works
      # 3,4,47,48 does not work
      # 49 always on
      # 2 and 50 works!
##      print "0,20: " +str(image.getpixel((0,20))[0])
##      print "2,20: " +str(image.getpixel((2,20))[0])
##      print "49,20: " +str(image.getpixel((49,20))[0])
##      print "50,20: " +str(image.getpixel((50,20))[0])


      # If this Epuck prefers the red food source, look for red pixels in the photo
      if preferRED is 1:
        if image.getpixel((2,20))[0] is 255:
          self.foundBoxLoc[0]=1
        else:
          self.foundBoxLoc[0]=0
        if image.getpixel((14,20))[0] is 255:
          self.foundBoxLoc[1]=1
        else:
          self.foundBoxLoc[1]=0
        if image.getpixel((26,20))[0] is 255:
          self.foundBoxLoc[2]=1
        else:
          self.foundBoxLoc[2]=0
        if image.getpixel((38,20))[0] is 255:
          self.foundBoxLoc[3]=1
        else:
          self.foundBoxLoc[3]=0
        if image.getpixel((50,20))[0] is 255:
          self.foundBoxLoc[4]=1
        else:
          self.foundBoxLoc[4]=0
      # Else, look for blue pixels
      else:
        if image.getpixel((2,20))[2] is 255:
          self.foundBoxLoc[0]=1
        else:
          self.foundBoxLoc[0]=0
        if image.getpixel((14,20))[2] is 255:
          self.foundBoxLoc[1]=1
        else:
          self.foundBoxLoc[1]=0
        if image.getpixel((26,20))[2] is 255:
          self.foundBoxLoc[2]=1
        else:
          self.foundBoxLoc[2]=0
        if image.getpixel((38,20))[2] is 255:
          self.foundBoxLoc[3]=1
        else:
          self.foundBoxLoc[3]=0
        if image.getpixel((50,20))[2] is 255:
          self.foundBoxLoc[4]=1
        else:
          self.foundBoxLoc[4]=0
      

##      if self.sumFinds(self.foundBoxLoc) is 0:
      if sum(self.foundBoxLoc) is 0:
        self.foundBox = False
        self.LEDbody.set(0)
      else:
        self.foundBox = True
        self.LEDbody.set(1)
    


#################################### SURVIVE CODE

  ## Basic surviving. If you are not on collision course,
  ## continue exploring/searching
  def survive(self, proxList):
      if self.checkCollision(proxList):
        self.avoidance(proxList)
      else:
        self.moveRandom()

####### NOT USED? SLETTE?
  def weightedRandom(self):
    x = random.betavariate(5,3)
    ##x = random.gauss(0.5,0.2)
    x = min(1,x)
    ##x = max(0,x)
    return x


  ## Sets the wheel speeds to random values, in order to turn and explore at random.
  ## Have a low move time, to avoid running into things as well as hopefully turn often.
  def moveRandom(self):
      self.left_wheel_speed = random.random()
      self.right_wheel_speed = random.random()
      self.moveTime = 0.32

  ## Determines whether or not the robit is headed straight for something
  def checkCollision(self,proxlist):
    return proxlist[0] > 500 or proxlist[7] > 500
##      avoid = False
##      if proxlist[0] > 500:
##          avoid = True
##      elif proxlist[7] > 500:
##          avoid = True
##      return avoid
    
  ## Determines if an obstacle is approaching, and performs an evasive maneuver if it is.
  ## Note that this is only called if the Epuck has not seen a box
  def avoidance(self,proxlist):
    sum1 = proxlist[0] + proxlist[1] + proxlist[2]/2
    sum2 = proxlist[6] + proxlist[7] + proxlist[5]/2
    difference = abs(sum1-sum2)

    ## If something is very close, spin around.
    if difference < 1000:
      self.left_wheel_speed = -1
      self.right_wheel_speed = -1
      self.moveTime = 0.3+self.weightedRandom()

    ## If something is approaching on the right, turn left...
    elif sum1>sum2:
      self.left_wheel_speed =0.7
      self.right_wheel_speed = -0.3
      self.moveTime = 0.4+self.weightedRandom()
    ## ... or vice versa
    elif sum1<sum2:
      self.left_wheel_speed = -0.3
      self.right_wheel_speed = 0.7
      self.moveTime = 0.4+self.weightedRandom()

################################# RETRIEVAL CODE

  ## Basic retrieval. Determines whether or not to approach the box, or push it
  def retrieve(self, proxyList, treshold):
    self.select_behavior(proxyList,treshold)
    if self.push:
        self.push_box(proxyList)
        self.moveTime = 0.128
        self.confidence_value = self.modifyConfidence(self.confidence_value,proxyList)
    else:
        self.converge_to_box(proxyList,treshold)
        self.moveTime = 0.064
        self.confidence_value = 1

  ## Looks at how close the box is. If its close enough, enable pushing.
  def select_behavior(self,proxyList,treshold):
    self.push = False
    self.converge = True

    for i in range(0,2):
        if (proxyList[i] > treshold and self.foundBoxLoc[4] is 1) or (proxyList[7-i] > treshold and self.foundBoxLoc[0] is 1):
            self.push = True
            break
          
  ## Pushing of the actual box
  def push_box(self,proxyList):

    self.push_align(proxyList,2500)

##    #Blink for visual pushing feedback
##    for i in range(0,7):
##        if self.LED[i].get() is 1:
##            self.LED[i].set(0)
##        else:
##            self.LED[i].set(1)

  ## Aligns itself to push straight on the box, and not other bots
  def push_align(self,proxyList,threshold):
      #print "Pushing with foundBoxSum: " +str(self.sumFinds())
      if self.foundBoxLoc[4] is 0:
          print "Avoiding bot on right!!!"
          self.left_wheel_speed = -0.3;
          self.right_wheel_speed = 0.7;
      elif self.foundBoxLoc[0] is 0:
          print "Avoiding bot on left!!!"
          self.left_wheel_speed = 0.7;
          self.right_wheel_speed = -0.3;       
      else:
          if(proxyList[0] > threshold and proxyList[7] > threshold):
              if abs(proxyList[0]-proxyList[7]) < 150:
                  print "Pushing: Straight ahead"
                  self.left_wheel_speed = 1;
                  self.right_wheel_speed = 1;
              else:
                self.fine_align(proxyList)                 
          elif (proxyList[0]+proxyList[1]>proxyList[6]+proxyList[7]):
              print "Pushing: Aligning right"
              self.left_wheel_speed = 0.7;
              self.right_wheel_speed = 0.0;
          elif (proxyList[0]+proxyList[1]<proxyList[6]+proxyList[7]):
              print "Pushing: Aligning left"
              self.left_wheel_speed = 0.0;
              self.right_wheel_speed = 0.7;
                  
  def fine_align(self,proxyList):
      if (proxyList[0]>proxyList[7]):
          print "Fine-Aligning right"
          difference = proxyList[7]/proxyList[0]
          self.left_wheel_speed = 1;
          self.right_wheel_speed = difference;
      else:
          print "Fine-Aligning left"
          difference = proxyList[0]/proxyList[7]
          self.right_wheel_speed = 1;
          self.left_wheel_speed = difference;
          
  
  ## Approach the box
  def converge_to_box(self,proxyList, threshold):
    if (self.foundBoxLoc[2] is 1):
          if (self.foundBoxLoc[1] is 1 and self.foundBoxLoc[3] is 1) or (self.foundBoxLoc[1] is 0 and self.foundBoxLoc[3] is 0):
                print "Going for CENTER"
                self.right_wheel_speed = self.weightedRandom()
                self.left_wheel_speed = self.right_wheel_speed
##                self.LED[0].set(1)
##                self.LED[7].set(1)
          elif (self.foundBoxLoc[4] is 0):
                print "Going for CENTER+LEFT"
                self.right_wheel_speed = self.weightedRandom()
                self.left_wheel_speed = self.right_wheel_speed - 0.1
##                self.LED[0].set(1)
##                self.LED[7].set(1)
##                self.LED[6].set(1)
          elif (self.foundBoxLoc[0] is 0):
                print "Going for CENTER+RIGHT"
                self.left_wheel_speed = self.weightedRandom()
                self.right_wheel_speed = self.right_wheel_speed - 0.1
##                self.LED[0].set(1)
##                self.LED[7].set(1)
##                self.LED[1].set(1)
            
            
      
    elif (self.foundBoxLoc[4] is 0):
      print "Going LEFT"
      self.right_wheel_speed = self.weightedRandom()
      self.left_wheel_speed = self.right_wheel_speed - 0.2
##      self.LED[6].set(1)
      
    elif (self.foundBoxLoc[0] is 0):
      print "Going RIGHT"
      self.left_wheel_speed = self.weightedRandom()
      self.right_wheel_speed = self.right_wheel_speed - 0.2
##      self.LED[1].set(1)
      
      
################################# STAGNATION



  ## A robot is more confident if it has neighbours pushing along with it
  def modifyConfidence(self,confidence,proxyList):
    output = 0.95*confidence
    output = output + self.count_neighbours()
    return output

  ## Selects actions for the stagnation phase. The Epuck can try to get "unstuck", or just send its data
  ## to the retrieve sub-behavior instead. 
  def selectAction(self, proxyList, treshold):
    print "ConfValue: "+str(self.confidence_value)
    if (self.confidence_value > 0.4):
      self.retrieve(proxyList,treshold)
    elif (self.confidence_value > 0.35):
      self.randomWriggle(proxyList)
      print "Wriggle"
    elif (self.confidence_value > 0.25):
      self.retrieve(proxyList,treshold)
    else:
      self.relocate()

  ## Random wriggle when pushing the box  
  def randomWriggle(self,proxyList):
    rand = random.random()
    if rand < 0.5:
        self.right_wheel_speed = .3
        self.left_wheel_speed = -.2
    else:
        self.right_wheel_speed = -.2
        self.left_wheel_speed = .3

    self.timeStep = 0.1*random.random()
    self.confidence_value = self.modifyConfidence(self.confidence_value,proxyList)
    
    
  ## Moves the robot to the left or right face of the box, relative to its current position
  def relocate(self):
    print "RELOCATE"
    
    self.backward(duration = 1.0)
    direction = random.random()

    if direction > 0.5: self.turn_left()
    else: self.turn_right()

    for i in range(3): self.move(duration = 0.65)

    if direction > 0.5: self.turn_right()
    else: self.turn_left()
    
    for i in range(3): self.move(duration = 0.65)

    if direction > 0.5: self.turn_right()
    else: self.turn_left()

    self.confidence_value = 1

  ## Count the number of comrades pushing alongside you.
  def count_neighbours(self):
    
    nc = 0
    proxyList=self.get_proximities()
    if proxyList[2] > 100: nc+=1
    if proxyList[5] > 100: nc+=1
    return nc
  
  
    

################################# MAIN LOOP


        
  def run(self):
    self.accelerometer = self.getAccelerometer('accelerometer')
    self.basic_setup()
    print "Starting controller"
    self.do_timed_action()
    self.init_LED()
    self.accelerometer.enable(1)
    
        
    
    print "My name is "+str(self.getName())

    

      
    self.LEDbody = self.getLED("led8")
    self.LEDfront = self.getLED("led9")
    self.LEDbody.set(0)
    self.LEDfront.set(0)

    # comment this out for single color sims
    self.preferRED = self.forceSurvey()

    ## Main loop - this is where the magic happens
    while True:

      ##init and cleanup
      proxList = self.get_proximities()
      self.reset_LED()
      
      if(not self.foundBox):
        self.survive(proxList)
        self.confidence_value = 1
      elif self.foundBox:
        self.selectAction(proxList,2000)


       

      # Get current position:
      before = self.accelerometer.getValues()

      # Move:  
      self.set_wheel_speeds(self.left_wheel_speed,self.right_wheel_speed)
      self.do_timed_action(self.moveTime)

      # Get new position:
      after = self.accelerometer.getValues()

      # Check how far you traveled
      displacement = 0
      for i in range(3): displacement += ((after[i] - before[i])**2)
      displacement = (displacement**(0.5))

      # If not so far, increase still count. Else, reset it
      if displacement < 0.05: self.standing_still += 1
      else: self.standing_still = 0

      # If you stood still 100 rounds in a row (or more), reset with a 5 % chance
      if self.standing_still >= 100 and random.random() < 0.05:
        self.turn_right()
        self.turn_right()
        self.foundBox = False
        self.standing_still = 0
      

      if self.preferRED is 1:
        self.takeGeneralPhoto(1)
      else:
        self.takeGeneralPhoto(0)
      
      

    

controller = MyEpuck()
controller.run()
