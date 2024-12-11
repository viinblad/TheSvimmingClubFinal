Swim Club Management System

Project Description

The Swim Club Management System is a comprehensive software project developed by four programming student developers.
The goal of this project is to simplify and automate the management of swim club members, memberships, activities, payments, and administrative tasks.
This project was created as part of a school assignment to demonstrate our programming and software development skills.

Key Features:

      1.Member Management: Register, update, delete, and search for members.

      2.Payment Management: Track and manage member payments, calculate fees, and set reminders.

      3.Activity Tracking: Record and display member activity types (e.g., swimming styles).

      4.Reminders: Create and manage payment reminders.

      5.Role-Based Access: Admin, Chairman, Treasurer, and Coach roles with tailored menu options.

This application improves efficiency by reducing manual errors and streamlining administrative processes for swim clubs.

Technologies Used

    - Java: Core programming language.

    - JUnit: For testing controllers and services.

    - File Handling: Data is stored and retrieved from .dat files.

    - Object-Oriented Programming (OOP): A class- and object-based structure.

    - Draw.io: Used to create visual documentation like class diagrams.


Description of Key Directories

/docs: Contains documentation and diagrams.

/main/java/swimclub/controllers: Includes controllers for managing application logic (e.g., MemberController, PaymentController).

/main/java/swimclub/models: Defines key data models like Member and Payment.

/main/java/swimclub/repositories: Manages file-based data persistence for members and payments.

/main/java/swimclub/services: Contains reusable business logic, such as fee calculations and validations.

/main/java/swimclub/ui: Implements the user interface for interacting with the application.

/test/java/swimclub: Contains unit tests for controllers and services.


File Structure

src
├── docs
│   ├── classdiagram.png.svg          # Class diagram
│   ├── domainmodeldefinprojekt.drawio # Domain model for the project
├── main
│   ├── java
│   │   ├── swimclub
│   │   │   ├── controllers           # Handles business logic and user interactions
│   │   │   ├── exceptions            # Custom exceptions for error handling
│   │   │   ├── models                # Data structures for members, payments, etc.
│   │   │   ├── repositories          # Handles data persistence (e.g., file management)
│   │   │   ├── services              # Contains business logic (e.g., calculating fees)
│   │   │   ├── ui                    # User interface for interaction (console-based)
│   │   │   ├── utilities             # General-purpose utilities (e.g., validators)
│   │   │   └── Main.java             # Entry point of the application
│   ├── resources
│       ├── competitionResults.dat    # Data file for competition result records
│       ├── members.dat               # Data file for member information
│       ├── paymentsRates.dat         # Data file for payment rates
│       ├── payments.dat              # Data file for payment records
│       ├── reminders.dat             # Data file for payment reminders
│       ├── staff.dat                 # Data file for staff records
│       ├── teams.dat                 # Data file for teams records
│       ├── trainingResults.dat       # Data file for training results records
│       ├── users.dat                 # Data file for users records
├── test
│   ├── java
│   │   ├── swimclub
│   │   │   ├── controllers
│   │   │   │   └── MemberControllerTest.java  # Tests for MemberController
│   │   │   ├── services
│   │   │   │   └── PaymentServiceTest.java    # Tests for PaymentService
│   ├── testResources                          # Resources for testing
README.txt

Description of Key Directories:

    /controllers: Contains the logic for handling user requests, such as registering members, processing payments, etc.
    /models: Defines the data structures for members, payments, and related entities.
    /repositories: Manages data persistence, including reading and writing from text files.
    /services: Contains business logic, such as calculating fees or validating data.
    /ui: The User Interface code that interacts with the user via the console.
    /utilities: Utility classes for general tasks, such as file handling and input validation.

Requirements

    Java 11 or higher
    JUnit: For testing
    A text editor or IDE such as IntelliJ IDEA or Eclipse for running and editing the code.

Setup and Running the Project
1. Clone the repository or download the project files.

git clone https:https://github.com/viinblad/TheSvimmingClubFinal.git

2. Navigate to the project directory.

cd swim-club-management

3. Compile and run the project using Java.

If you're using a command line interface, compile the project using:

javac -d bin src/main/java/swimclub/*.java

Then run the Main class:

java -cp bin swimclub.Main

4. Interaction with the System

Once the system is running, the user will be presented with a menu in the terminal to:

    Register new members
    Search for members
    Update or delete members
    Manage payments
    View summaries and reminders

Follow the prompts to interact with the system.

Features Walkthrough

Member Management:

   Register: Add a new member with personal and membership details.

   Search: Search by ID, name, or phone number.

   Update: Modify member details.

   Delete: Remove a member from the system.

Payment Management:

   Register Payments: Record payments and update payment statuses.

   View Payments: Check payment history for a specific member.

   Filter: Find members by payment status (Complete, Pending, Failed).

   Reminders: Set reminders for outstanding payments.

Role-Based Menus:

   Admin: Full access to all features.

   Chairman: Access to member and activity tracking.

   Treasurer: Manage payments and reminders.

   Coach: Access training results and member activities.


Testing

   Unit tests are included to ensure the robustness of the system.
   Navigate to the /test directory and run the test classes using your IDE or a Java testing framework like JUnit.

Current Test Coverage:

   MemberControllerTest

   PaymentServiceTest

License

This project is licensed under the MIT License.

Copyright (c) [2024] [Victor Irmand Nielsen, Rasmus Biehl, Martin Mainmann Dantoft, Esben Viinblad Jensen]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
