import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, useNavigate, useLocation } from 'react-router-dom';
import {
  MantineProvider,
  createTheme,
  Text,
  Box,
  Group,
  Grid,
  Card,
  Title,
} from '@mantine/core';
import '@mantine/core/styles.css';

import {
  Calendar,
  FileText,
  Users,
  Stethoscope,
  HeartPulse,
  BedDouble,
  DoorOpen,
  Activity,
  Clock,
  HandHelping,
  LogOut,
  ChevronRight,
  ChevronLeft,
  LayoutDashboard,
  Shield,
  User,
} from 'lucide-react';

import Login from '../components/Login';
import Register from '../components/Register';
import AppointmentSearch from '../components/AppointmentSearch';
import BedRecordsSearch from '../components/BedRecordsSearch';
import DoctorSearch from '../components/DoctorSearch';
import HelperSearch from '../components/HelperSearch';
import MedicalRecordsSearch from '../components/MedicalRecordsSearch';
import NurseSearch from '../components/NurseSearch';
import PatientSearch from '../components/PatientSearch';
import RoomRecordsSearch from '../components/RoomRecordsSearch';
import SurgeryRecordSearch from '../components/SurgeryRecordSearch';
import StaffShiftSearch from '../components/StaffShiftSearch';

// ─── THEME ────────────────────────────────────────────────────────────────────
const theme = createTheme({
  fontFamily: "'Figtree', sans-serif",
  headings: { fontFamily: "'Bricolage Grotesque', sans-serif" },
  primaryColor: 'cyan',
  defaultRadius: 'md',
  colors: {
    dark: [
      '#C9D1D9','#B0BAC6','#8B96A8','#6E7A8A',
      '#3D4858','#252E3F','#1A2235','#111827','#0B1120','#070C17',
    ],
  },
});

// ─── CONFIG ───────────────────────────────────────────────────────────────────
const roleConfig = {
  PATIENT: { color: '#22d3ee', Icon: User,        label: 'Patient'  },
  DOCTOR:  { color: '#a78bfa', Icon: Stethoscope, label: 'Doctor'   },
  NURSE:   { color: '#34d399', Icon: HeartPulse,  label: 'Nurse'    },
  HELPER:  { color: '#fb923c', Icon: HandHelping, label: 'Helper'   },
  ADMIN:   { color: '#f472b6', Icon: Shield,      label: 'Admin'    },
};

const apiModules = [
  { title: 'Appointments',    path: '/dashboard/appointments',   Icon: Calendar,    desc: 'Search and manage patient visits',       accent: '#22d3ee' },
  { title: 'Medical Records', path: '/dashboard/medical-records',Icon: FileText,    desc: 'Secure patient medical histories',       accent: '#a78bfa' },
  { title: 'Patients',        path: '/dashboard/patients',       Icon: Users,       desc: 'Patient registry and demographics',      accent: '#34d399' },
  { title: 'Doctors',         path: '/dashboard/doctors',        Icon: Stethoscope, desc: 'Medical staff directory',                accent: '#38bdf8' },
  { title: 'Nurses',          path: '/dashboard/nurses',         Icon: HeartPulse,  desc: 'Nursing staff and assignments',          accent: '#f472b6' },
  { title: 'Bed Records',     path: '/dashboard/bed-records',    Icon: BedDouble,   desc: 'Hospital bed occupancy status',          accent: '#fb923c' },
  { title: 'Room Records',    path: '/dashboard/room-records',   Icon: DoorOpen,    desc: 'Operating and ward rooms',               accent: '#fbbf24' },
  { title: 'Surgery Records', path: '/dashboard/surgeries',      Icon: Activity,    desc: 'Surgical procedures and outcomes',       accent: '#f87171' },
  { title: 'Staff Shifts',    path: '/dashboard/shifts',         Icon: Clock,       desc: 'Shift scheduling and tracking',          accent: '#818cf8' },
  { title: 'Helpers',         path: '/dashboard/helpers',        Icon: HandHelping, desc: 'Support staff management',               accent: '#4ade80' },
];

// ─── ROUTE GUARD ─────────────────────────────────────────────────────────────
const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('jwtToken');
  if (!token) return <Navigate to="/login" replace />;
  return children;
};

// ─── GLOBAL STYLES COMPONENT ─────────────────────────────────────────────────
function GlobalStyles({ accent }) {
  const staggerCSS = apiModules
    .map((_, i) => `.mod-card-${i} { animation: fadeUp 0.45s ${0.04 + i * 0.045}s cubic-bezier(0.22,1,0.36,1) both; }`)
    .join('\n');

  return (
    <style>{`
      @import url('https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:wght@500;700;800&family=Figtree:wght@300;400;500;600&display=swap');

      *, *::before, *::after { box-sizing: border-box; }

      @keyframes fadeUp   { from { opacity:0; transform:translateY(18px); } to { opacity:1; transform:translateY(0); } }
      @keyframes slideDown{ from { opacity:0; transform:translateY(-14px);} to { opacity:1; transform:translateY(0); } }

      .header-in  { animation: slideDown 0.4s cubic-bezier(0.22,1,0.36,1) forwards; }
      .content-in { animation: fadeUp   0.4s 0.05s cubic-bezier(0.22,1,0.36,1) both; }

      /* Dot-grid overlay */
      .dot-grid {
        background-image: radial-gradient(circle, rgba(255,255,255,0.035) 1px, transparent 1px);
        background-size: 26px 26px;
        position: fixed; inset: 0; pointer-events: none; z-index: 0;
      }

      /* Module card */
      .mod-card {
        cursor: pointer;
        position: relative;
        overflow: hidden;
        background: rgba(11,17,32,0.65) !important;
        border: 1px solid rgba(255,255,255,0.06) !important;
        transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease !important;
        backdrop-filter: blur(12px);
      }
      .mod-card:hover {
        transform: translateY(-4px) !important;
        box-shadow: 0 20px 44px rgba(0,0,0,0.55), 0 0 0 1px var(--c40) !important;
        border-color: var(--c40) !important;
      }
      .mod-card::after {
        content: '';
        position: absolute;
        bottom: 0; left: 16px; right: 16px;
        height: 1px;
        background: linear-gradient(90deg, transparent, var(--c), transparent);
        opacity: 0;
        transition: opacity 0.22s ease;
      }
      .mod-card:hover::after { opacity: 1; }

      /* Icon box */
      .icon-wrap {
        width: 42px; height: 42px;
        border-radius: 11px;
        display: flex; align-items: center; justify-content: center;
        background: var(--ibg);
        border: 1px solid var(--iborder);
        transition: transform 0.2s ease;
        flex-shrink: 0;
      }
      .mod-card:hover .icon-wrap { transform: scale(1.1) rotate(-3deg); }

      /* Arrow */
      .mod-arrow {
        color: var(--c);
        opacity: 0;
        transform: translateX(-8px);
        transition: opacity 0.2s ease, transform 0.2s ease;
      }
      .mod-card:hover .mod-arrow { opacity: 1; transform: translateX(0); }

      /* Header pill */
      .user-chip {
        display: inline-flex; align-items: center; gap: 8px;
        padding: 7px 13px;
        background: rgba(255,255,255,0.04);
        border: 1px solid rgba(255,255,255,0.08);
        border-radius: 100px;
      }

      /* Back / logout btns */
      .ghost-btn {
        display: inline-flex; align-items: center; gap: 6px;
        padding: 7px 13px;
        border-radius: 9px;
        background: transparent;
        border: 1px solid rgba(255,255,255,0.09);
        color: #64748b;
        font-size: 0.83rem; font-weight: 500;
        cursor: pointer;
        transition: all 0.18s ease;
        font-family: 'Figtree', sans-serif;
        white-space: nowrap;
      }
      .ghost-btn:hover {
        background: rgba(255,255,255,0.07);
        border-color: rgba(255,255,255,0.16);
        color: #e2e8f0;
      }
      .logout-btn {
        display: inline-flex; align-items: center; gap: 6px;
        padding: 7px 14px;
        border-radius: 9px;
        background: transparent;
        border: 1px solid rgba(255,255,255,0.09);
        color: #64748b;
        font-size: 0.83rem; font-weight: 500;
        cursor: pointer;
        transition: all 0.18s ease;
        font-family: 'Figtree', sans-serif;
      }
      .logout-btn:hover {
        background: rgba(239,68,68,0.10);
        border-color: rgba(239,68,68,0.32);
        color: #fca5a5;
      }

      /* Divider pipe */
      .v-pipe { width:1px; height:18px; background: rgba(255,255,255,0.10); flex-shrink:0; }

      ${staggerCSS}
    `}</style>
  );
}

// ─── DASHBOARD LAYOUT ────────────────────────────────────────────────────────
function DashboardLayout({ children }) {
  const navigate      = useNavigate();
  const location      = useLocation();
  const role          = localStorage.getItem('userRole') || 'User';
  const email         = localStorage.getItem('userEmail') || '';
  const cfg           = roleConfig[role] || { color: '#22d3ee', Icon: User, label: role };
  const { Icon: RoleIcon, color: accent } = cfg;
  const isHome        = location.pathname === '/dashboard';
  const currentModule = apiModules.find(m => location.pathname.startsWith(m.path));

  return (
    <Box style={{ minHeight: '100vh', background: '#070C17', fontFamily: "'Figtree', sans-serif" }}>
      <GlobalStyles accent={accent} />

      {/* Dot-grid texture */}
      <Box className="dot-grid" />

      {/* Role glow */}
      <Box style={{
        position: 'fixed', inset: 0, pointerEvents: 'none', zIndex: 0,
        background: `radial-gradient(ellipse 55% 30% at 50% -4%, ${accent}15 0%, transparent 60%)`,
        transition: 'background 0.7s ease',
      }} />

      {/* Page */}
      <Box style={{ position: 'relative', zIndex: 1, maxWidth: 1240, margin: '0 auto', padding: '24px 24px 48px' }}>

        {/* ── HEADER ── */}
        <Box
          className="header-in"
          mb={36}
          style={{
            display: 'flex', alignItems: 'center', justifyContent: 'space-between',
            padding: '12px 16px',
            background: 'rgba(9,14,27,0.75)',
            border: '1px solid rgba(255,255,255,0.07)',
            borderRadius: 14,
            backdropFilter: 'blur(24px)',
            gap: 12,
          }}
        >
          {/* LEFT */}
          <Group align="center" gap={10} wrap="nowrap">
            {/* Wordmark */}
            <Group align="center" gap={9} wrap="nowrap">
              <Box style={{
                width: 32, height: 32, borderRadius: 9,
                background: `${accent}16`,
                border: `1px solid ${accent}30`,
                display: 'flex', alignItems: 'center', justifyContent: 'center',
              }}>
                <Activity size={16} color={accent} strokeWidth={2} />
              </Box>
              <Text style={{
                fontFamily: "'Bricolage Grotesque', sans-serif",
                fontWeight: 700, color: '#f1f5f9',
                fontSize: '0.95rem', letterSpacing: '-0.01em',
                whiteSpace: 'nowrap',
              }}>
                Privacy API
              </Text>
            </Group>

            <Box className="v-pipe" />

            {/* Home crumb */}
            {isHome ? (
              <Group gap={6} align="center" wrap="nowrap">
                <LayoutDashboard size={13} color="#475569" strokeWidth={2} />
                <Text style={{ color: '#475569', fontSize: '0.82rem' }}>Dashboard</Text>
              </Group>
            ) : (
              <>
                <button className="ghost-btn" onClick={() => navigate('/dashboard')}>
                  <ChevronLeft size={13} />
                  Dashboard
                </button>
                {currentModule && (
                  <>
                    <ChevronRight size={12} color="#334155" strokeWidth={2} />
                    <Group gap={6} align="center" wrap="nowrap">
                      <currentModule.Icon size={13} color={currentModule.accent} strokeWidth={2} />
                      <Text style={{ color: '#e2e8f0', fontSize: '0.82rem', fontWeight: 500 }}>
                        {currentModule.title}
                      </Text>
                    </Group>
                  </>
                )}
              </>
            )}
          </Group>

          {/* RIGHT */}
          <Group align="center" gap={8} wrap="nowrap">
            <Box className="user-chip">
              <Box style={{
                width: 22, height: 22, borderRadius: '50%',
                background: `${accent}15`,
                border: `1px solid ${accent}30`,
                display: 'flex', alignItems: 'center', justifyContent: 'center',
              }}>
                <RoleIcon size={12} color={accent} strokeWidth={2} />
              </Box>
              <Text style={{ fontSize: '0.8rem', color: '#64748b', whiteSpace: 'nowrap' }}>
                <span style={{ color: accent, fontWeight: 600 }}>{cfg.label}</span>
                {email && <span style={{ color: '#334155' }}>  ·  {email}</span>}
              </Text>
            </Box>

            <button className="logout-btn" onClick={() => { localStorage.clear(); navigate('/login'); }}>
              <LogOut size={13} strokeWidth={2} />
              Sign Out
            </button>
          </Group>
        </Box>

        {/* ── CONTENT ── */}
        <Box className="content-in">
          {children}
        </Box>
      </Box>
    </Box>
  );
}

// ─── DASHBOARD HUB ───────────────────────────────────────────────────────────
function DashboardHub() {
  const navigate = useNavigate();
  const role     = localStorage.getItem('userRole') || 'User';
  const cfg      = roleConfig[role] || { color: '#22d3ee', label: role };

  return (
    <Box>
      {/* Section header */}
      <Box mb={28}>
        <Text style={{
          fontSize: '0.7rem', fontWeight: 600,
          letterSpacing: '0.14em', textTransform: 'uppercase',
          color: cfg.color, marginBottom: 10,
        }}>
          Healthcare Data Platform
        </Text>
        <Title order={2} style={{
          fontFamily: "'Bricolage Grotesque', sans-serif",
          color: '#f1f5f9', fontWeight: 800,
          fontSize: 'clamp(1.4rem, 3vw, 1.9rem)',
          letterSpacing: '-0.025em', lineHeight: 1.15,
        }}>
          Select a module
        </Title>
        <Text mt={7} style={{ color: '#3d4f6b', fontSize: '0.875rem' }}>
          {apiModules.length} modules available · {cfg.label} access
        </Text>
      </Box>

      {/* Rule */}
      <Box mb={28} style={{
        height: 1,
        background: 'linear-gradient(90deg, rgba(255,255,255,0.07) 0%, transparent 55%)',
      }} />

      {/* Grid */}
      <Grid gutter={{ base: 'md', md: 'lg' }}>
        {apiModules.map((mod, i) => (
          <Grid.Col span={{ base: 12, sm: 6, md: 4, lg: 3 }} key={mod.title}>
            <ModuleCard mod={mod} index={i} onClick={() => navigate(mod.path)} />
          </Grid.Col>
        ))}
      </Grid>
    </Box>
  );
}

// ─── MODULE CARD ─────────────────────────────────────────────────────────────
function ModuleCard({ mod, index, onClick }) {
  const { Icon, accent, title, desc } = mod;
  return (
    <>
      <style>{`
        .mod-card-${index} {
          --c: ${accent};
          --c40: ${accent}40;
          --ibg: ${accent}12;
          --iborder: ${accent}28;
        }
      `}</style>
      <Card
        className={`mod-card mod-card-${index}`}
        p="xl"
        radius="xl"
        onClick={onClick}
        style={{ height: '100%', minHeight: 148 }}
      >
        {/* Top row */}
        <Box style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: 18 }}>
          <Box className="icon-wrap">
            <Icon size={19} color={accent} strokeWidth={1.8} />
          </Box>
          <ChevronRight className="mod-arrow" size={15} strokeWidth={2} />
        </Box>

        {/* Label */}
        <Text style={{
          fontFamily: "'Bricolage Grotesque', sans-serif",
          fontWeight: 700, color: '#f0f4f8',
          fontSize: '0.92rem', letterSpacing: '-0.01em',
          marginBottom: 5,
        }}>
          {title}
        </Text>
        <Text style={{ color: '#3d5068', fontSize: '0.78rem', lineHeight: 1.55 }}>
          {desc}
        </Text>
      </Card>
    </>
  );
}

// ─── APP ─────────────────────────────────────────────────────────────────────
function App() {
  const wrap = (Child) => (
    <ProtectedRoute>
      <DashboardLayout>
        <Child />
      </DashboardLayout>
    </ProtectedRoute>
  );

  return (
    <MantineProvider theme={theme} defaultColorScheme="dark" withCssVariables withGlobalClasses>
      <BrowserRouter>
        <Routes>
          <Route path="/"         element={<Navigate to="/dashboard" replace />} />
          <Route path="/login"    element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard"                 element={wrap(DashboardHub)} />
          <Route path="/dashboard/appointments"    element={wrap(AppointmentSearch)} />
          <Route path="/dashboard/bed-records"     element={wrap(BedRecordsSearch)} />
          <Route path="/dashboard/doctors"         element={wrap(DoctorSearch)} />
          <Route path="/dashboard/helpers"         element={wrap(HelperSearch)} />
          <Route path="/dashboard/medical-records" element={wrap(MedicalRecordsSearch)} />
          <Route path="/dashboard/nurses"          element={wrap(NurseSearch)} />
          <Route path="/dashboard/patients"        element={wrap(PatientSearch)} />
          <Route path="/dashboard/room-records"    element={wrap(RoomRecordsSearch)} />
          <Route path="/dashboard/surgeries"       element={wrap(SurgeryRecordSearch)} />
          <Route path="/dashboard/shifts"          element={wrap(StaffShiftSearch)} />
        </Routes>
      </BrowserRouter>
    </MantineProvider>
  );
}

export default App;