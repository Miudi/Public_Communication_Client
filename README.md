# Public Bus Communication Client Application

## Project Description
This project consists of a JavaFX application for managing bus lines, designed for both administrators and regular users. The application allows administrators to add and remove bus lines, as well as add new drivers. Regular users can view available bus lines, their stops, search for connections between stops, and view arrival times based on selected stops.

## Project Structure
The application is structured into several modules:
1. **HelloApplication**: The main class responsible for starting the application. It loads the initial view "hello-view.fxml".
2. **HelloController**: Controller for the initial view "hello-view.fxml". It allows users to switch between different views, including the login view and the customer view.
  
3. **LoginController**: Controller for the "login-view.fxml" view. It handles user authentication for the driver profile. This controller validates the entered login credentials and redirects to the appropriate view.
4. **DriverController**: Controller for the "driver-view.fxml" view. It manages the driver profile view, including displaying available bus lines and handling navigation to other views for adding new lines and setting delays. This controller initializes by fetching the list of available lines and creating buttons for each line.Additionally, it handles the click event for selecting a specific bus line, navigating to the view for setting delays for that line.
5. **AddController**: Controller for the "add-view.fxml" view. It handles adding new bus lines to the database, removing existing lines, and assigning available bus stops for the new line's route. 
   
6. **CustomerController**: Controller for the "customer-view.fxml" view. It handles user interactions for the customer profile view, including options to navigate to the line view or the search view.
7. **LineController**: Controller for the "line-view.fxml" view. It displays bus lines as buttons and allows users to select a line to view more details.
8. **CourseController**: Controller for the "course-view.fxml" view. It manages the display of bus line courses, including setting the line number and retrieving the list of stops for that line. It provides methods for handling user interactions, such as clicking on a direction button to display the stops in that direction.
9. **SearchController**: Controller for the "search-view.fxml" view. It allows users to search for connections between bus stops at a specified time.
10. **StopController**: Controller for the "stop-view.fxml" view. It manages the display of bus stop information, including departure times for bus lines from that stop. This controller sets the line number, stop name, and direction of travel, and retrieves departure times for the specified line and stop from the database. It dynamically generates a table to display the departure times, organizing them by hour.

11. **Server_connection**: Class responsible for handling communication with the server, sending queries, and receiving responses.


## How to Run
To run the application:
1. Open the project in your Java IDE.
2. Compile and run the `HelloApplication` class to start the application.
3. Use the graphical interface to navigate through different views and functionalities.

## Dependencies
The project utilizes JavaFX for the graphical user interface (GUI) and relies on server communication for data retrieval and manipulation. Make sure you have the required dependencies properly configured in your project.


