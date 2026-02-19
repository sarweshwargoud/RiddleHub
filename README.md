# 🧠 RiddleHub – Social Puzzle Platform

RiddleHub is a modern Android social application built using **Kotlin and Jetpack Compose**, where users can post riddles, interact through likes and comments, follow other users, and engage in a real-time puzzle-sharing community powered by **Firebase**.

This project was developed as part of an Android Development Internship and demonstrates complete frontend-to-backend integration with production-level architecture.

---

## 🚀 Features

### 🔐 Authentication
- Email & Password Login
- User Registration
- Secure Firebase Authentication integration
- Persistent user sessions

### 🧩 Puzzle System
- Post riddles and puzzle questions
- Add optional hints
- Set difficulty level (Easy / Medium / Hard / Expert)
- Real-time feed updates using Firestore

### ❤️ Social Features
- Like / Unlike puzzles
- Comment system
- Reply to comments
- Follow / Unfollow users
- User profile system

### 🎨 UI/UX
- Animated splash screen logo
- Modern dark theme UI (Material 3)
- Bottom navigation (Home, Create, Profile)
- Smooth screen transitions
- Clean modular structure

---

## 🏗 Architecture

The app follows a modular and scalable structure:

```
com.sarweshwar.riddlehub
│
├── data/                # Firebase repositories
├── model/               # Data models (User, Puzzle, Comment)
├── ui/                  # Compose screens
│   ├── auth/
│   ├── home/
│   ├── create/
│   ├── detail/
│   └── profile/
├── navigation/          # Navigation graph
└── MainActivity.kt
```

- Repository pattern
- Firebase real-time listeners
- Kotlin Coroutines
- Jetpack Compose UI architecture

---

## 🔥 Backend Integration

- Firebase Authentication
- Firebase Firestore
- Secure Firestore rules
- Real-time database updates
- Structured collections for users, puzzles, comments, and likes

---

## 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Navigation Compose
- Material 3
- Firebase Authentication
- Firebase Firestore
- Gradle Kotlin DSL

---

## 📱 How To Run

1. Clone this repository
2. Create a Firebase project
3. Enable:
   - Email/Password Authentication
   - Firestore Database
4. Download `google-services.json`
5. Place it inside:
   ```
   app/
   ```
6. Sync Gradle
7. Run on emulator or physical device

---

## 📦 Build APK

To generate APK:

```
Build → Generate Signed Bundle / APK → APK
```

Release APK can be shared manually or uploaded to Play Store.

---

## 🎯 Learning Outcomes

Through this project, I gained experience in:

- Android backend integration
- Secure authentication flows
- Real-time database architecture
- Modular app structuring
- Release build generation
- Play Store deployment process

---

## 🚀 Future Enhancements

- AI-powered hint generation
- Intelligent difficulty analysis
- Push notifications
- Google Sign-In
- Puzzle recommendation engine

---

## 👨‍💻 Author

**Sarweshwar Goud**  
Android Developer | AI Enthusiast  

---

⭐ If you found this project interesting, consider starring the repository!
