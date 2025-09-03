# ThinkDeck 🧠 - The Intelligent Flashcard & Quiz App

ThinkDeck is a modern, desktop-based learning application designed to help users study more effectively. It combines traditional flashcard and quiz functionalities with the power of Artificial Intelligence to provide a dynamic and personalized learning experience.


---

## ✨ Key Features

- **User Authentication**: Secure login and registration system for personalized content.
- **Flashcard Management**: Easily create, view, and organize flashcards across multiple subjects.
- **Standard Quizzes**: Test your knowledge with quizzes generated directly from your own flashcards.
- **🤖 AI-Powered Flashcard Generation**: Instantly create a deck of flashcards for any subject using the Google Gemini API.
- **🤖 AI-Powered Quizzes**: Generate unique, multiple-choice quizzes on any topic, complete with results and answer explanations.
- **User Profiles**: View and update your user information (username, email, location).
- **Learning Analytics**: Track your quiz performance over time with a detailed Quiz History page.
- **Community & Messaging**:
  - View a list of all registered users.
  - Send and receive messages from other users.
  - Privacy controls to hide yourself from the user list or disable incoming messages.
- **Account Management**: A dedicated settings page where users can manage their account, including permanent deletion.
- **Modern & Responsive UI**: A clean, intuitive, and responsive user interface built with Java Swing that adapts to different window sizes.

---

## 🛠️ Technologies Used

- **Core Language**: **Java 17**
- **User Interface**: **Java Swing**
- **Build & Dependency Management**: **Apache Maven**
- **Database**: **MongoDB**
- **AI Integration**: **Google Gemini API** for content generation.
- **Libraries**:
  - `mongodb-driver-sync`: To connect and interact with the MongoDB database.
  - `org.json`: To parse and handle JSON data for API communication.

---

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running on your machine.

### Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher.
- **Apache Maven**: To build the project and manage dependencies.
- **MongoDB**: A running instance of MongoDB (local or cloud-hosted).

### Installation & Setup

1.  **Clone the repository:**

    ```sh
    git clone https://github.com/Kabir470/ThinkDeck.git
    cd ThinkDeck
    ```

2.  **Set up the AI API Key:**

    - Navigate to the file `src/main/java/db/AiHelper.java`.
    - Find the `API_KEY` variable and replace `"YOUR_API_KEY"` with your actual Google Gemini API key.

    ```java
    // In AiHelper.java
    private static final String API_KEY = "YOUR_API_KEY";
    ```

3.  **Build and Run the Application:**
    - Open your terminal in the root directory of the project.
    - Run the following Maven command. Maven will automatically download all the required dependencies and start the application.
    ```sh
    mvn exec:java
    ```
    - The application will start, and the Login window will appear.

---

## 📂 Project Structure

The project is organized into several packages to maintain a clean and understandable architecture:

```
src/main/java/
├── component/      # Reusable UI components (e.g., Toaster notifications).
├── create_flashcard/ # UI and logic for flashcard creation and viewing.
├── create_quiz/    # UI and logic for both standard and AI-powered quizzes.
├── dashboard/      # The main dashboard and related pages (Profile, Settings, etc.).
├── db/             # Manager classes for all database interactions (UserManager, QuizManager, etc.).
├── login/          # UI for the Login and Registration windows.
└── Utils/          # Utility classes, primarily for UI styling (UIUtils).
```

- **`pom.xml`**: The heart of the Maven project. It defines the project's dependencies, build instructions, and metadata.

---

## 🤝 Contributing

Contributions are welcome! If you have ideas for new features or improvements, feel free to fork the repository and submit a pull request.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the `LICENSE` file for details.
