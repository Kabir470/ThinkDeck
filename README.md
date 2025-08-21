# ThinkDeck

ThinkDeck is a Java-based desktop application designed to help users create, manage, and review flashcards and quizzes. It features a user-friendly interface for learning and revision, supporting multiple users and MongoDB integration for data storage.

## Features

- **User Authentication:** Secure login and registration system.
- **Flashcard Creation:** Easily create, edit, and review flashcards.
- **Quiz Module:** Generate and take quizzes based on your flashcards.
- **Dashboard:** View all users and manage your learning progress.
- **MongoDB Integration:** Persistent storage for flashcards, quizzes, and user data.
- **Modern UI Components:** Custom UI elements for a better user experience.

## Project Structure

```
ThinkDeck/
├── src/
│   ├── component/         # UI components (Toaster, HyperlinkText, etc.)
│   ├── create_flashcard/  # Flashcard creation and management
│   ├── create_quiz/       # Quiz creation and testing
│   ├── dashboard/         # Dashboard and user management
│   ├── db/                # Database integration (MongoDB)
│   ├── login/             # Login and registration UI
│   └── Utils/             # Utility classes
├── lib/                   # External libraries (MongoDB drivers)
├── images/                # Application images
├── bin/                   # Compiled classes
```

## Getting Started

### Prerequisites

- Java 8 or higher
- MongoDB server

### Setup

1. Clone the repository:
   ```
   git clone <repo-url>
   ```
2. Add the JAR files from the `lib/` directory to your classpath.
3. Compile the source code:
   ```
   javac -cp "lib/*" -d bin src/**/*.java
   ```
4. Run the application:
   ```
   java -cp "lib/*;bin" login.LoginUI
   ```

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.
p