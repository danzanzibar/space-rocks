Space Rocks

Author: Zan Owsley


About the game:

This game is inspired by 2D space games I played as a child in the 90s such as (cough)
Asteroids, Maelstrom, and Escape Velocity. Unlike many of these games, you are not
destroying asteroids with your lasers, but rather the point of the game is to kill UFOs
and that are equally trying to kill you.

The game world is toroidal so you can never reach the edge of space. The background nebula
does not move at the same pace as the player to give a sense of parallax. I hope these add
to a sense of truly drifting in the void.


How to build and run:

This project uses the Intellij build system and UI Designer (for the game's UI) and must
be built in the Intellij-IDEA IDE. A jar is included in the root directory
(space_rocks.jar) that can be run using 'java -jar space_rocks.jar'.


Steering behaviours:

This game has 3 primary game entities: the player (a spaceship), asteroids, and UFOs. The
player and asteroids have a kinematic model but have simplified steering behavious. The
player may receive an acceleration in the forward direction and turns at a constant
angular speed. The asteroids simply have a constant velocity (linear and angular) and
float through space. Only the UFOs exhibit complex steering behaviours.

The UFO will by default perform a weights-blended wandering behaviour that incorporates a
collision avoidance system. The "Wander" behaviour is the more complex algorithm outlined
in the textbook that relies upon "Face", which in turn relies upon "Align". The collision
avoidance system lets the UFOs avoid both asteroids and other UFOs. When the UFO comes
within range of the player, it goes into attack mode (note the audible queue) and will
attempt to crash into the player. It changes to a modified "Arrive" behaviour that is
still weighted with collision avoidance but with a stronger focus on "Arrive". The
collision avoidance will now only target asteroids as clearly the UFOs believe crashing
UFOs isn't that bad so long as the player dies.

The wandering behaviour can be observed when the game launches as it fills the game world
with asteroids and UFOs to watch until a new game is started. The observe the attack
behaviour, though, the user must brave the void and test their skills against the hordes
of alien kamikazes (a hair dramatic, perhaps).


Game Design:

On the whole, I may have gone a little overboard on this game. There was a lot of "firsts"
for me, though, and I wanted to learn as much as possible. I had never (1) built a GUI
with Java, (2) really completed any game before, (3) used Swing or AWT, or (4) built a OOP
program of this size. Because I knew that I would need to make several games for this
course, I wanted to build some framework objects that would prove reusable. I quickly
reaslised that I would want to utilize a spatial partitioning scheme (also a first for me)
to institute collision checking and avoidance "the right way". Once the game was
functional, I also wanted to learn more about OOP patterns so broke into the book "Design
Patterns" for the first time and spent A LOT of time refactoring this code for flexibility
and resuse. On the whole, I think the slight proliferation of classes is well justified by
the clearly communicated intent they have brought.

I hope this will make it easier to judge whether this meets the assignment's
requirements. It certainly does have the effect of making for a long design document so I
have organized it as cleanly as possible below - the same as I have organized the packages
in the source code.


   game_framework:

   This package contains classes I have made with the clear intention of reusing
   throughout this course. It is made up of four subpackages.

       "audio":

       This package contains three class that provide basic capabilities. The most
       signifant choice here was to have a 'SoundPool' class that holds multiple instances
       of 'Sound', as I found I needed laser noises or explosions to be able to happen in
       quick succession.

       "core":

       This contains some important and oddball classes. The 'Loop' class provides a
       simple game loop that can register listeners. 'ResourceLoader' offers some static
       methods for loading media files. 'Vector2' provides a vector class that has lots of
       typical vector math operations. Note that it has methods to return new vectors but
       also methods that mutate the vector in place - this was found to be convenient.

       A 'World' object was also defined that allows registering 'Renderer's from the
       graphics package. Basically, this forces rendering to follow updates. While a
       simple and effective interface for the games in this course, I do not really like
       that I coupled updating the rendering so tightly... but it's fairly transparent and
       easy to use.

       "graphics":

       This package provides many useful classes. There is the 'Renderer' interface that
       works with the 'World' class and an implementation of it in 'DrawingPanel'. This
       subclass of 'JPanel' make manually drawing easy to wire into any game.

       The rest of the classes revolve around displaying images using Swing. 'Sprite',
       'Animation', and 'OneShotAnimation' are the key classes for drawing actual entities
       in the game and implement the 'Drawable' interface. There is also 'SpriteSheet'
       that the animations classes rely on.

       "input":

       The class 'KeyBindings' only needs an 'InputMap', 'ActionMap', and Enum that lists
       out the various input actions of interest. The Enum class must implement the
       'KeyBindable' interface which has the 'getKeyCode' method to return a key code for
       each action. The 'KeyBindings' instance can then be used to query the status of a
       key related to an action.

   "steering":

   This package contains all the steering behaviours used in the game. 'Kinematic2D' is
   the core entity representation and 'Steering2D' is a record class to represent a
   steering output. There are also static utility methods in 'SteeringUtils' and classes
   for weighted and blended (via weights) behaviours.

       "behaviours":

       This subpackage has all the actual behaviours. 'SteeringBehaviour' and its
       subclass, 'SingleTargetSteeringBehaviour' provide abstract classes for the rest of
       the behaviours to subclass. In coding all the behaviours from the text, there was
       also a multi target abstract class but I have removed all unnecessary classes from
       the package.

       'SteeringBehaviour' stores the character's kinematic and specifies the
       'getSteering()' abstract method. 'SingleTargetSteeringBehaviour' also adds a target
       kinematic.

       I decided to implement complex steering behaviours using composition over
       inheritance. A delegated-to behaviour is created with a zero-valued target and the
       important kinematic field is set as needed in the course of the delegating
       behaviour's algorithm. On the whole, I feel this has made for a simple design and
       interface.

   "space_rocks":

   This is the main package of the game and contains three subpackages. The only classes
   in this package are the Enum class for key bindings, a very simple 'Launcher',
   'SpaceRocksApp', and 'SpaceRocksWorld'. The app class creates the main Swing panes, the
   world, the UI, and wires up the world to it's 'Renderer' target, a 'DrawingPanel'. It
   also creates the 'Loop' and registers itself to listen to it. When updating, it
   delegates the work to 'GameState' objects (more on that below).

   The world object holds the state of everything in the game and relies heavily on the
   various 'EntityManager' instances (more below).

       "game_states":

       'GameState' is the abstract class of all other state classes. It holds a reference
       to the application object and defines the interface. Each concrete class is
       relatively simple and uses the application to update the UI and world as
       needed. These state objects also handle changing state.

       "entities":

       This is the largets package and and has three abstract base classes: 'Entity',
       'CollisionEntity' (a subclass of 'Entity'), and 'EntityManager'. The last two are
       quite simple: 'CollisionEntity' simply adds collision checking and 'EntityManager'
       holds a list of entities, a reference to the 'Grid' (more below), and has some
       methods for managing the entities.

       'Entity' holds kinematic, spatial, and graphics data. If I were to build it again,
       I would decouple the 'Drawable' from the entity and simply hold an ID for some
       separate 'DrawableManager' to deal with. As it is, though, Entity is also a
       'Drawable' and performs transforms to put it in the correct position and
       orientation before defering to the enclosed drawable to draw the image. It also
       handles updating (though is overriden by some uniqe entities like lasers that have
       a limited life).

       The 'CollisionManager', a subclass of 'EntityManager' is a bit of an oddball. It
       serves two purposes: it's a manager class for 'Explosion's and does collision
       checking. Managing explosions and checking for collisions is related enough to
       justify this.

       "spatial":

       The last subpackage is dedicated to spatial partitioning (well, the 'Background'
       class is also thrown in for want of a better home). As recommended by the text, I
       read up on "Real Time Collision Detection" and implemented a fairly simple (yet
       likely far from optimized) grid partitioning of userspace. The performance
       improvement was massive and more than adequate for the game. 'Grid' and 'Cell'
       implement this scheme - you can find more information about the algorithms in the
       'Grid' class.
   

Bugs:

The window doesn't want to be resized. Currently, it can handle some resizing and keep the
player in the middle of the screen without changing the aspect ratio of the game. But if
it is increased beyond what it is set to, you will notice weird background patches that
are loaded. It wasn't deemed important enough to fix this as the game should not be
resized... and to fix this and handle all kinds of window sizes would justify a whole
display manager. It could be a lot.
