# LocalZeroToLocalHero

_LocalZeroToLocalHero_ is a Java-based client-server application that fosters local community engagement. It enables user registration, login, role management, initiative creation, messaging, and achievement tracking.

## Features

- **User Management:** Register, log in, manage roles, and track achievements.
- **Initiatives:** Create, view, and join local initiatives (e.g., Gardening, Car Pool, Garage Sale, Tool Sharing).
- **Messaging:** Exchange messages between users, with support for offline storage.
- **Notifications:** Receive real-time or stored notifications (e.g., role changes).
- **Logging:** Track user actions and view logs in the client.
- **Achievements:** Earn and improve achievements through participation.

## Project Structure

```
LocalZeroToLocalHero/
├── client/    # JavaFX client application
├── server/    # Java server application
├── shared/    # Shared models and utilities
└── README.md  # Project overview and instructions
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8 or newer

### Build and Run

1. **Clone the repository:**
    ```bash
    git clone https://github.com/qqequalstears/LocalZeroToLocalHero.git
    cd LocalZeroToLocalHero
    ```
    2. **Build the project using Maven:**
        ```bash
        mvn clean install
        ```
        This command compiles the code and installs dependencies for all modules.

3. **Run the server:**
    - In your IDE or terminal, navigate to the `server` module and run `Start.java`.

4. **Run the client:**
    - In your IDE or terminal, navigate to the `client` module and run `Launcher.java` (JavaFX application).

