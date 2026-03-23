# Privacy API Dashboard — Frontend

A secure, role-based React dashboard for interfacing with the Healthcare Privacy API. Built for authorized hospital personnel to search and manage medical records, staff shifts, patient data, and appointments.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | React 18 + Vite |
| Routing | React Router DOM v6 |
| UI Library | Mantine v7 |
| Icons | Lucide React |
| Auth | JWT (JSON Web Tokens) |
| Fonts | Figtree & Bricolage Grotesque |

---

## Features

- **Role-Based Access Control** — UI adapts dynamically across 5 roles: Admin, Doctor, Nurse, Patient, and Helper.
- **Hub-and-Spoke Navigation** — Central module dashboard routing to 10 distinct API domains.
- **Secure Authentication** — Login and registration flows with password strength indicators and JWT management.
- **Advanced Filtering** — Multi-parameter search forms supporting date ranges, time windows, and array-based ID lookups.
- **Paginated Data Grids** — Smooth handling of large datasets (1,000+ rows) via Mantine's native pagination.

---

## Getting Started

### Prerequisites

Install [Node.js](https://nodejs.org/) v18 or later.

### 1. Install Dependencies

```bash
npm install
```

> If you encounter missing package errors, install the core libraries explicitly:
> ```bash
> npm install react-router-dom @mantine/core @mantine/hooks lucide-react
> ```

### 2. Configure Environment Variables

Create a `.env` file in the project root (next to `package.json`) and set your backend URL:

```env
VITE_API_BASE_URL=http://localhost:8000/api
```

Adjust the port if your Spring Boot server runs elsewhere.

### 3. Start the Dev Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173` (or the URL shown in your terminal).

---

## Project Structure

```
src/
├── components/
│   ├── Login.jsx                 # Authentication entry point
│   ├── Register.jsx              # Account creation with password validation
│   ├── AppointmentSearch.jsx     # Module: Appointments
│   ├── BedRecordsSearch.jsx      # Module: Bed assignments
│   ├── DoctorSearch.jsx          # Module: Medical staff directory
│   ├── HelperSearch.jsx          # Module: Support staff directory
│   ├── MedicalRecordsSearch.jsx  # Module: Patient histories
│   ├── NurseSearch.jsx           # Module: Nursing staff directory
│   ├── PatientSearch.jsx         # Module: Patient demographics
│   ├── RoomRecordsSearch.jsx     # Module: Room occupancy
│   ├── StaffShiftSearch.jsx      # Module: Schedule management
│   └── SurgeryRecordSearch.jsx   # Module: Surgical procedures
├── App.jsx                       # Global routing, layout, and hub UI
└── main.jsx                      # React DOM entry point
```

---

## Security Notes

**CORS** — Ensure your Spring Boot backend accepts requests from this frontend's origin (e.g., `http://localhost:5173`).

**Token Storage** — JWTs are currently stored in `localStorage`. For production, consider migrating to HTTP-only cookies to reduce XSS exposure.

---

## Available Scripts

| Command | Description |
|---|---|
| `npm run dev` | Start the development server |
| `npm run build` | Bundle the app for production |
| `npm run preview` | Preview the production build locally |