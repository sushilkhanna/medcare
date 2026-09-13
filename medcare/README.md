# 🏥 MedCare — Full Stack Healthcare Platform

E-Commerce Medicine & Doctor Appointment Booking System built with **Spring Boot + MySQL + JWT + BCrypt**.

---

## 🗂 Project Structure

```
medcare/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/medcare/
    │   ├── MedCareApplication.java          ← Entry point
    │   ├── config/
    │   │   ├── SecurityConfig.java          ← JWT + BCrypt + CORS
    │   │   ├── DataSeeder.java              ← Auto seed DB on startup
    │   │   └── GlobalExceptionHandler.java  ← Error handling
    │   ├── controller/
    │   │   ├── AuthController.java          ← /api/auth/**
    │   │   ├── MedicineController.java      ← /api/medicines/**
    │   │   ├── CartController.java          ← /api/cart/**
    │   │   ├── OrderController.java         ← /api/orders/**
    │   │   ├── AppointmentController.java   ← /api/appointments/**
    │   │   ├── DoctorController.java        ← /api/doctors/**
    │   │   └── AdminController.java         ← /api/admin/**
    │   ├── entity/
    │   │   ├── User.java
    │   │   ├── Medicine.java
    │   │   ├── Doctor.java
    │   │   ├── Cart.java
    │   │   ├── Order.java
    │   │   ├── OrderItem.java
    │   │   └── Appointment.java
    │   ├── dto/
    │   │   └── PublicDtos.java              ← All DTOs
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── MedicineRepository.java
    │   │   ├── DoctorRepository.java
    │   │   ├── CartRepository.java
    │   │   ├── OrderRepository.java
    │   │   └── AppointmentRepository.java
    │   └── security/
    │       ├── JwtUtil.java
    │       ├── JwtAuthFilter.java
    │       └── UserDetailsServiceImpl.java
    └── resources/
        ├── application.properties
        └── static/
            └── index.html                   ← Complete Frontend
```

---

## ⚙️ Requirements

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.8+ |
| PostgreSQL | 14+ |

---

## 🚀 Setup & Run

### Step 1 — PostgreSQL Setup
```sql
-- Login to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE medcare_db;
```

### Step 2 — Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medcare_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

### Step 3 — Build & Run
```bash
cd medcare
mvn clean install
mvn spring-boot:run
```

OR build a JAR and run it:
```bash
mvn clean package -DskipTests
java -jar target/medcare-1.0.0.jar
```

### Step 4 — Open Browser
```
http://localhost:8080
```

The app will **auto-seed the database** on first run with demo accounts.

---

## 🔑 Demo Accounts

| Role | Email | Password |
|------|-------|----------|
| Patient | patient@medcare.com | patient123 |
| Doctor | arjun@medcare.com | doctor123 |
| Admin | admin@medcare.com | admin123 |

---

## 📡 REST API Endpoints

### Auth
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/auth/me | Authenticated |

### Medicines
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/medicines/list | Public |
| GET | /api/medicines/search?q= | Public |
| GET | /api/medicines/{id} | Public |
| POST | /api/medicines/admin/add | ADMIN |
| PUT | /api/medicines/admin/{id} | ADMIN |
| DELETE | /api/medicines/admin/{id} | ADMIN |
| PATCH | /api/medicines/admin/{id}/stock | ADMIN |
| GET | /api/medicines/admin/low-stock | ADMIN |

### Cart
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/cart | PATIENT |
| POST | /api/cart/add | PATIENT |
| PUT | /api/cart/{cartId}?quantity= | PATIENT |
| DELETE | /api/cart/{cartId} | PATIENT |
| DELETE | /api/cart/clear | PATIENT |

### Orders
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/orders/checkout | PATIENT |
| GET | /api/orders/my | PATIENT |
| GET | /api/orders/{id} | PATIENT |
| GET | /api/orders/admin/all | ADMIN |
| PATCH | /api/orders/admin/{id}/status | ADMIN |

### Appointments
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/appointments/book | PATIENT |
| GET | /api/appointments/my | PATIENT |
| PATCH | /api/appointments/{id}/cancel | PATIENT |
| GET | /api/appointments/doctor/my | DOCTOR |
| PATCH | /api/appointments/doctor/{id}/confirm | DOCTOR |
| PATCH | /api/appointments/doctor/{id}/reject | DOCTOR |
| GET | /api/appointments/admin/all | ADMIN |

### Doctors
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/doctors/list | Public |
| GET | /api/doctors/{id} | Public |
| POST | /api/doctors/admin/add | ADMIN |

### Admin
| Method | Endpoint | Access |
|--------|----------|--------|
| GET | /api/admin/stats | ADMIN |
| GET | /api/admin/users | ADMIN |
| PATCH | /api/admin/users/{id}/toggle | ADMIN |
| DELETE | /api/admin/users/{id} | ADMIN |

---

## 🔒 Security Architecture

```
Request → JwtAuthFilter → SecurityFilterChain → Controller
              ↓
         Extract JWT token from Authorization header
              ↓
         Validate signature + expiry (JwtUtil)
              ↓
         Load UserDetails from DB (UserDetailsServiceImpl)
              ↓
         Set SecurityContext → Role-based access enforced
```

- Passwords hashed with **BCrypt (strength 10)**
- JWT tokens signed with **HMAC-SHA256**
- Token expiry: **24 hours**
- Role-based method security via `@PreAuthorize`

---

## 🗄️ Database Tables

| Table | Purpose |
|-------|---------|
| users | All user accounts (patient, doctor, admin) |
| doctors | Doctor profiles linked to user accounts |
| medicines | Medicine catalog with stock |
| cart | Patient cart items |
| orders | Placed orders |
| order_items | Individual items per order |
| appointments | Doctor appointment bookings |

---

## 🎯 Features Implemented

### Patient
- Register & login
- Browse & search medicines
- Add to cart / update qty / remove
- Checkout → auto stock deduction + GST
- View order history
- Book doctor appointments
- View & cancel appointments
- Profile page

### Doctor
- Login & dashboard
- View appointment requests
- Accept / Reject appointments
- View patient list

### Admin
- Dashboard with stats
- Add / Edit / Delete medicines
- Manage stock levels
- View all orders & update status
- View all appointments
- Manage users (suspend / enable)
- Low-stock alerts

---

## 🚧 Planned Features

- [ ] Mock Payment Gateway (Razorpay integration)
- [ ] Prescription upload (PDF/image)
- [ ] Doctor–Patient real-time chat (WebSocket) — note: WebSocket infra now exists (medicine/order sync), so this can reuse the same `/ws` endpoint
- [ ] Email notifications (Spring Mail)
- [ ] Admin analytics charts

---

## 🛠 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2, Spring Security, Spring Data JPA |
| Real-time | Spring WebSocket (STOMP + SockJS) |
| AI | Groq (free, OpenAI-compatible) via Spring WebClient |
| Database | PostgreSQL 14+ |
| Auth | JWT (jjwt 0.12.3) + BCrypt |
| ORM | Hibernate |
| Frontend | HTML5, CSS3, Vanilla JavaScript, SockJS/Stomp.js |
| Build | Maven |
| Java | 17 |
| Deployment | Render (Docker) |

---

## ⚡ Real-Time Medicine Sync (WebSocket)

Stock and order updates now push live to every connected browser tab — no manual refresh needed.

- **Transport:** STOMP over WebSocket with SockJS fallback, endpoint `/ws`.
- **Topics:**
  - `/topic/medicines` — broadcast on add / edit / delete / restock / checkout stock deduction. Anyone on the Medicine Store or Admin Medicines page sees quantities update instantly.
  - `/topic/orders/{userId}` — broadcast when an admin changes an order's status; that patient's "My Orders" page updates live.
- Server side: `WebSocketConfig` + `RealtimeService`, wired into `MedicineController` and `OrderController`.
- Client side: `connectRealtime()` in `index.html`, called right after login; auto-retries every 5s if the socket drops.

---

## 🤖 AI Medicine Assistant (Buy Medicine page)

A chat assistant is available on the Medicine Store page — click **"🤖 Ask AI Assistant"** at the top, or **"🤖 Ask AI about this"** on any medicine card. It answers questions like:
- "Should I take this before or after food?"
- "What else has the same salt as this?"

Answers come from the AI model's own general knowledge (not the app database), with a disclaimer to confirm with a pharmacist/doctor — it's informational, not medical advice.

**Setup (free, no credit card required):**
1. Create a free API key at **https://console.groq.com/keys**.
2. Set the environment variable `AI_API_KEY` (locally in `.env`, or as a Render env var).
3. That's it — defaults already point at Groq's free `llama-3.1-8b-instant` model.

Want a different provider? Any OpenAI-compatible `/chat/completions` endpoint works — just override `AI_API_URL` / `AI_API_MODEL` (e.g. OpenRouter's free models).

If `AI_API_KEY` isn't set, the endpoint responds with a friendly "not configured yet" message instead of failing, so the rest of the app still works during a demo.

---

## ⚠️ Note on TROUBLESHOOTING.md

That file references MySQL and Lombok — both are stale leftovers from an earlier version of this project. The current codebase uses **PostgreSQL** (see `application.properties` / `pom.xml`) and has **no Lombok dependency** at all. If you hit a MySQL/Lombok-flavored error, it's not from this code — recheck your local environment setup instead.

---

1. Push this repo to GitHub
2. Go to [Render Dashboard](https://dashboard.render.com)
3. Click **New → Blueprint** and connect your GitHub repo
4. Render auto-detects `render.yaml` and provisions:
   - Free PostgreSQL database
   - Docker-based web service
5. After deploy, set `CORS_ALLOWED_ORIGINS` env var to your Render URL:
   ```
   https://medcare-xxxx.onrender.com
   ```
6. Visit your Render URL — the app will auto-seed demo data on first boot.
