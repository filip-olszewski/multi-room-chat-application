# Multi-Chat Room Application over TCP  

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
    - [Client-Side](#client-side)
    - [Server-Side](#server-side)
    - [Communication](#communication)
3. [Architecture](#architecture)
    - [Client](#client)
    - [Server](#server)
    - [Data Models](#data-models)
4. [Getting Started](#getting-started)
    - [Prerequisites](#1-prerequisites)
    - [Clone the Repository](#2-clone-the-repository)
    - [Build with Maven](#3-build-with-maven)
    - [Run the Server](#4-run-the-server)
    - [Run the Client](#5-run-the-client)
5. [Project Structure](#project-structure)
6. [Usage Example](#usage-example)
7. [Room Rules](#room-rules)
8. [Key Highlights](#key-highlights)
9. [License](#license)

---

## Overview  
This project is a client-server chat application built using **Java sockets over TCP**. It allows multiple users to connect to a central server, create and join chat rooms, send messages, and manage room privacy policies.  

The system encapsulates the following:  
- A **TCP-based communication layer** for request/response messaging.  
- A **client application** with a Swing-based UI for interacting with chat rooms.  
- A **server application** that manages users, rooms, events, and broadcasts.  
- **Command-based design** for client actions (login, create room, join room, send message, etc.).  
- **Event-driven server architecture** to propagate room and user updates across clients.  
- Thread-safe services for **room management** and **user management**.  

---

## Features  

### Client-Side  
- Connects to server using `SocketConnection`.  
- GUI built with **Swing** (`AppWindow`).  
- Supports commands:  
  - **Login**  
  - **Create Room**  
  - **Delete Room**  
  - **Join Room**  
  - **Leave Room**  
  - **Send Message**  
  - **Fetch Rooms**  
- Dynamically updates UI when rooms are created, modified, or deleted.  
- Displays feedback dialogs for errors and successes.  

### Server-Side  
- Manages multiple client connections via `ClientHandler` running on **virtual threads**.  
- Provides services for:  
  - Creating, joining, leaving, and deleting rooms.  
  - Handling messages between users in a room.  
  - Broadcasting system and user messages to all room members.  
- **EventBus** for handling room lifecycle events:  
  - Room creation  
  - Room deletion  
  - Room modification  
- Supports both **public** and **private** rooms (`RoomPrivacyPolicy`).  

### Communication  
- Core entities:  
  - `Request<T extends Payload>`  
  - `Response<T extends Payload>`  
  - `Payload` (specific data type like `MessagePayload`, `CreateRoomPayload`, etc.)  
- Serializable object streams for sending requests/responses across the network.  
- Thread-safe `SocketConnection` ensuring synchronized writes to the output stream.  

---

## Architecture  

### Client  
- **Client.java**: Entry point that initializes connection, registers commands, binds UI actions, and handles server responses.  
- **CommandRegistry**: Manages and executes client commands.  
- **Handlers**: Process incoming responses (`ResponseHandler` implementations).  
- **UI**: Swing-based screens (`HomeScreen`, `ChatScreen`) bound to commands.  

### Server  
- **Server.java**: Listens on a TCP port and spawns `ClientHandler` for each connection.  
- **ClientHandler**: Handles requests from a specific client, maps them to request handlers.  
- **RequestHandlers**: One per payload type (e.g., `CreateRoomRequestHandler`, `JoinRoomRequestHandler`).  
- **ServerContext**: Provides access to `RoomService`, `UserService`, and `EventBus`.  
- **EventBus**: Publishes events to notify all clients of room updates.  

### Data Models  
- **Room**: Represents a chat room with admin, capacity, privacy, and active users.  
- **User**: Represents a connected user and their current room.  
- **RoomDTO**: Transfer object for rooms across client/server.  
- **RoomMapper**: Converts between models and DTOs.  

---

## Getting Started  

Follow these steps to set up and run the project locally.  

---

### 1. Prerequisites
- **Java 21 or higher** (required for virtual threads).  
- **Maven** installed and available in your system path.  
  - Verify with:  
    ```bash
    java -version
    mvn -version
    ```
---

### 2. Clone the Repository
Clone the project from GitHub:  
```bash
git clone https://github.com/filip-olszewski/multi-room-chat-application.git
cd multi-room-chat-application
```

### 3. Build with Maven
Compile and package the project using Maven:

```bash
mvn clean install
```

### 4. Run the Server
Start the server first so it can accept connections:

```bash
mvn exec:java -Dexec.mainClass="io.github.filipolszewski.server.ServerStarter"
```
### 5. Run the Client
In another terminal window, run the client:

```bash
mvn exec:java -Dexec.mainClass="io.github.filipolszewski.client.ClientStarter"
```

## Project Structure
```bash
/client
  ├── commands/            # Client-side command implementations
  ├── handlers/            # Response handlers
  ├── ui/                  # Swing UI components
  ├── Client.java          # Main client entry point
  └── ClientStarter.java   # Starter for the client

/server
  ├── handlers/            # Request handlers
  ├── events/              # Event bus and event definitions
  ├── services/            # RoomService, UserService
  ├── Server.java          # Main server entry point
  └── ServerStarter.java   # Starter for the server

/communication
  ├── core/                # Request, Response, Payload abstractions
  ├── payloads/            # Specific payload classes
  └── RoomUpdateType       # Enum for room update events

/connection
  └── SocketConnection.java

/constants
  ├── AppConfig.java
  ├── RoomConfig.java
  ├── UIConfig.java
  ├── WindowConfig.java
  └── RoomPrivacyPolicy.java

/dto
  ├── RoomDTO.java

/model
  ├── room/Room.java
  └── user/User.java

/util/mappers
  └── RoomMapper.java
```

## Usage Example  

### 1. Login  
- Launch the client.  
- The system automatically requests a login and you have to provide a unique userID.
- You will see the **home screen** with a list of all **public rooms**.  

### 2. Create a Room  
- Click **Create Room**.  
- Enter:  
  - **Room ID** (unique string)  
  - **Capacity** (maximum users)  
  - **Privacy policy** (public/private)  
- If successful, you will see a confirmation dialog.  
- Other clients will automatically see the new public room in their list.  

### 3. Join a Room  
- **Public rooms** → visible in the list, just select and join.  
- **Private rooms** → not visible in the list, joinable only if you enter the **room ID** directly.  
- *(WIP)* Private rooms will later restrict access only to invited users.  

### 4. Send a Message  
- Type a message in the chat input field.  
- Click **Send**.  
- All users in the room (except you) will receive the message in real time.  

### 5. Leave a Room  
- Click **Leave Room**.  
- You will be taken back to the home screen.  

---

## Room Rules  

- **Public rooms** are listed on the home screen and open for anyone to join.  
- **Private rooms** are hidden and can only be joined via **room ID**.  
- *(WIP)* Private rooms will not allow joining unless a user is **explicitly added**.  

### Room Deletion  
- Only the **room admin** can delete a room.  
- A room **cannot** be deleted if there are still users inside.  

## Key Highlights
- Uses TCP sockets with object serialization for request/response communication.
- Employs Command pattern for client actions.
- Implements Event-driven design on the server side to notify all clients about state changes.
- Ensures thread safety in room management using synchronization and concurrent data structures.
- Built with modular, extensible architecture for future feature additions (authentication, private messages, etc.).

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.