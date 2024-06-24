# Rescue Animal Management System

## Project Overview
The Rescue Animal Management System is a Java application developed to manage the intake, training status, and reservation of rescued animals, specifically dogs and monkeys. The main goal of the project is to organize information about these animals, ensuring efficient and smooth operations for animal rescues and adoption agencies.

### Summarize the project and what problem it was solving.
- The application offers a menu-driven interface, allowing users to:
  - Intake new dogs or monkeys, capturing specific details pertinent to each animal type.
  - Reserve animals that are in service.
  - Print lists of all dogs, all monkeys, or all animals that aren't reserved.
- The primary problem the system solves is the organization and management of rescued animals, ensuring that each animal's details are accurately tracked, and they are appropriately reserved when needed.

### What did you do particularly well?
- Designed a class hierarchy that separates the generic properties of a rescue animal from the specific properties of dogs and monkeys.
- Implemented encapsulation to ensure data integrity and provide a clear API for working with each animal type.
- Created a user-friendly interface, making the system accessible even to non-tech-savvy users.

### Where could you enhance your code? How would these improvements make your code more efficient, secure, and so on?
- **Data Persistence**: Currently, the application's data does not persist between runs. Integrating a database or file system could ensure data longevity.
- **Input Validation**: Enhanced input validation could prevent potential errors and improve data integrity.
- **Extendability**: Implementing interfaces or abstract classes could make adding new animal types more straightforward in the future.

### Which pieces of the code did you find most challenging to write, and how did you overcome this? What tools or resources are you adding to your support network?
- Designing the class hierarchy to ensure a clear separation of concerns was challenging. To overcome this, I referred to object-oriented design principles and sought feedback from peers.
- Moving forward, I'll leverage resources like online communities, documentation, and design pattern books to strengthen my coding and design skills.

### What skills from this project will be particularly transferable to other projects or coursework?
- Object-Oriented Programming: Designing class hierarchies and understanding encapsulation, inheritance, and polymorphism.
- User Interface Design: Creating user-friendly, menu-driven applications.
- Data Management: The principles applied here can be transferred to other projects requiring data collection, organization, and retrieval.

### How did you make this program maintainable, readable, and adaptable?
- **Maintainability**: Used meaningful variable names, broke down methods to perform singular tasks, and kept a consistent code style.
- **Readability**: Included comments to explain complex code sections and maintained consistent indentation.
- **Adaptability**: Designed a modular class structure, enabling easy addition of new animal types or features in the future.

## Final Words
This project served as a practical exercise in applying object-oriented principles, user interface design, and data management in a real-world scenario. The skills and insights gained will undoubtedly be valuable in future projects and professional endeavors.
