Swim Club Management System
Overview

The Swim Club Management System is a software solution for managing a swim club's members, payments, and related activities. The system allows you to:

    Register and manage members.
    Track membership types and payment statuses.
    Calculate and register payments.
    Generate payment reminders and track payment statuses.

This application is designed to automate the key administrative tasks of a swim club, improving efficiency and reducing manual errors.
Features

    Member Management: Add, update, and delete members.
    Payment Management: Record payments, calculate fees, and track payment status (Pending, Completed, or Failed).
    Activity Tracking: Record and display member activity types.
    Reminders: Set and manage payment reminders for members.
    Search: Search for members based on ID, name, or phone number.

Technologies Used

    Java: The core programming language.
    File Handling: For saving and loading member and payment data from text files.
    Object-Oriented Programming: Designed using classes and objects to manage members, payments, and activities.

File Structure

/src
    /swimclub
        /controllers
        /models
        /repositories
        /services
        /ui
        /utilities
    /resources
        members.txt
        payments.txt
        reminders.txt
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
    A text editor or IDE such as IntelliJ IDEA or Eclipse for running and editing the code.

Setup and Running the Project
1. Clone the repository or download the project files.

git clone https://github.com/your-repository-link/swim-club-management.git

2. Navigate to the project directory.

cd swim-club-management

3. Compile and run the project using Java.

If you're using a command line interface, compile the project using:

javac -d bin src/swimclub/*.java

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
Member Management

    Register a New Member: Enter the member’s personal details, including name, age, address, contact information, and membership type.
    Update Member Information: Modify an existing member’s details.
    Delete Member: Remove a member from the system.
    Search Members: Find members by ID, name, or phone number.

Payment Management

    Register Payment: Record a payment made by a member, including payment amount and payment status (Completed, Pending, Failed).
    View Member Payments: View all payments made by a specific member.
    Filter Members by Payment Status: View members filtered by payment status.
    Payment Summary: Display a summary of total payments and paid members.

Reminder Management

    Set Payment Reminder: Set a reminder for a member about an outstanding payment.
    View All Reminders: View all the set payment reminders.
    Clear All Reminders: Remove all payment reminders from the system.

Example Usage
Registering a Member:

--- Swim Club Member Management ---
1. Register New Member
Enter member name: John Doe
Enter age: 25
Enter member email: john.doe@example.com
Enter city of member: New York
Enter street of member: 123 Elm St
Enter region of member: NY
Enter Zip code: 10001
Enter membership type (Junior/Senior, Competitive/Exercise): Senior Competitive
Enter activity type (Breaststroke, Crawl, Backcrawl or Butterfly): Crawl
Enter phone number (8 digits): 12345678

Member registered successfully.

Viewing Payment Summary:

--- Payment Summary ---
Total Payments Made: $5000
Number of Members Paid: 25

Searching for Members:

Enter search query (ID, name, or phone number): 12345678
ID: 1, Name: John Doe, Membership: Senior Competitive Swimmer, Status: Active, Activity: Crawl, Payment: Complete

Contributing

We welcome contributions to improve the Swim Club Management System! Feel free to:

    Fork the repository
    Create a feature branch
    Submit a pull request with your changes

License

This project is licensed under the MIT License.

Copyright (c) [2024] [Victor Irmand Nielsen, Rasmus Biehl, Martin Mainmann Dantoft]

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
