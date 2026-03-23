import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  TextInput, PasswordInput, Button, Paper, Title, Select, Text, Anchor, Alert, Box, Stack, Divider,
} from '@mantine/core';

const roleAccents = { PATIENT: '#22d3ee', DOCTOR: '#a78bfa', NURSE: '#34d399', HELPER: '#fb923c', ADMIN: '#f472b6' };

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '', role: 'PATIENT' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const accent = roleAccents[formData.role] || '#22d3ee';

  const handleChange = (value, name) => {
    if (typeof value === 'string') {
      setFormData({ ...formData, [name]: value });
    } else {
      setFormData({ ...formData, [value.target.name]: value.target.value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const baseUrl = import.meta.env.VITE_API_BASE_URL;
      const response = await fetch(`${baseUrl}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });
      const data = await response.json();
      if (response.ok && data.success) {
        localStorage.setItem('jwtToken', data.token);
        localStorage.setItem('userRole', formData.role);
        localStorage.setItem('referenceId', data.referenceId);
        localStorage.setItem('userEmail', formData.email);
        navigate('/dashboard');
      } else {
        setError(data.message || 'Invalid credentials. Please try again.');
      }
    } catch {
      setError('Network error. Is the server running?');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box style={{ minHeight: '100vh', background: `radial-gradient(ellipse 70% 50% at 50% -5%, ${accent}18 0%, transparent 65%), #070C17`, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '24px', fontFamily: "'Figtree', sans-serif", transition: 'background 0.6s ease' }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Bricolage+Grotesque:wght@400;600;700;800&family=Figtree:wght@300;400;500;600&display=swap');
        @keyframes fadeUp { from { opacity: 0; transform: translateY(28px); } to { opacity: 1; transform: translateY(0); } }
        .auth-card { animation: fadeUp 0.55s cubic-bezier(0.22,1,0.36,1) forwards; }
        .auth-title { background: linear-gradient(135deg, #f1f5f9 30%, #94a3b8 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }
        .role-select .mantine-Select-input { background: rgba(255,255,255,0.04) !important; border: 1px solid rgba(255,255,255,0.10) !important; color: #e2e8f0 !important; transition: border-color 0.2s ease, box-shadow 0.2s ease; }
        .role-select .mantine-Select-input:focus { border-color: var(--accent) !important; box-shadow: 0 0 0 2px color-mix(in srgb, var(--accent) 20%, transparent) !important; }
        .field-input .mantine-TextInput-input, .field-input .mantine-PasswordInput-input { background: rgba(255,255,255,0.04) !important; border: 1px solid rgba(255,255,255,0.10) !important; color: #e2e8f0 !important; transition: border-color 0.2s ease, box-shadow 0.2s ease; }
        .field-input .mantine-TextInput-input:focus, .field-input .mantine-PasswordInput-input:focus-within { border-color: var(--accent) !important; box-shadow: 0 0 0 3px color-mix(in srgb, var(--accent) 15%, transparent) !important; }
        .field-input .mantine-TextInput-input::placeholder, .field-input .mantine-PasswordInput-innerInput::placeholder { color: rgba(255,255,255,0.25) !important; }
        .field-input .mantine-TextInput-label, .field-input .mantine-PasswordInput-label, .role-select .mantine-Select-label { color: #64748b !important; font-size: 0.78rem !important; font-weight: 500 !important; letter-spacing: 0.06em !important; text-transform: uppercase !important; }
        .field-input .mantine-PasswordInput-visibilityToggle { color: #475569; } .field-input .mantine-PasswordInput-visibilityToggle:hover { color: var(--accent); }
        .submit-btn button { background: linear-gradient(135deg, var(--accent), color-mix(in srgb, var(--accent) 70%, #7c3aed)) !important; border: none !important; font-weight: 600 !important; letter-spacing: 0.02em !important; transition: opacity 0.2s ease, transform 0.15s ease, box-shadow 0.2s ease !important; box-shadow: 0 4px 20px color-mix(in srgb, var(--accent) 30%, transparent) !important; }
        .submit-btn button:hover:not(:disabled) { opacity: 0.92 !important; transform: translateY(-1px) !important; box-shadow: 0 8px 28px color-mix(in srgb, var(--accent) 40%, transparent) !important; }
        .submit-btn button:active { transform: translateY(0) !important; }
        .nav-anchor { color: var(--accent) !important; font-weight: 600; transition: opacity 0.15s ease; } .nav-anchor:hover { opacity: 0.8; }
        .error-alert .mantine-Alert-root { background: rgba(239,68,68,0.08) !important; border: 1px solid rgba(239,68,68,0.25) !important; color: #fca5a5 !important; }
      `}</style>
      <style>{`:root { --accent: ${accent}; }`}</style>

      <Box className="auth-card" style={{ width: '100%', maxWidth: 440 }}>
        <Box mb={32} style={{ textAlign: 'center' }}>
          <Box style={{ display: 'inline-flex', alignItems: 'center', gap: 10, padding: '6px 16px', borderRadius: 100, background: `${accent}14`, border: `1px solid ${accent}28`, marginBottom: 20 }}>
            <Box style={{ width: 7, height: 7, borderRadius: '50%', background: accent, boxShadow: `0 0 8px ${accent}` }} />
            <Text style={{ color: accent, fontSize: '0.72rem', fontWeight: 600, letterSpacing: '0.12em', textTransform: 'uppercase' }}>Privacy API</Text>
          </Box>
          <Title order={1} className="auth-title" style={{ fontFamily: "'Bricolage Grotesque', sans-serif", fontWeight: 800, fontSize: '2rem', lineHeight: 1.1 }}>Welcome back</Title>
          <Text mt={6} style={{ color: '#475569', fontSize: '0.9rem' }}>Sign in to your secure health dashboard</Text>
        </Box>

        <Paper radius="xl" p={36} style={{ background: 'rgba(15, 23, 42, 0.80)', border: '1px solid rgba(255,255,255,0.08)', backdropFilter: 'blur(24px)', boxShadow: '0 32px 64px rgba(0,0,0,0.45), inset 0 1px 0 rgba(255,255,255,0.06)' }}>
          <Box style={{ height: 2, background: `linear-gradient(90deg, ${accent}, transparent 70%)`, borderRadius: 1, marginBottom: 28 }} />
          {error && <Box className="error-alert" mb={20}><Alert radius="md" color="red">{error}</Alert></Box>}

          <form onSubmit={handleSubmit}>
            <Stack gap={18}>
              <Select className="role-select" label="Account Type" name="role" value={formData.role} onChange={(val) => handleChange(val, 'role')} data={[{ value: 'PATIENT', label: '🏥  Patient' }, { value: 'DOCTOR', label: '⚕️  Doctor' }, { value: 'NURSE', label: '🩺  Nurse' }, { value: 'HELPER', label: '🤝  Helper' }, { value: 'ADMIN', label: '🛡️  Admin' }]} required size="md" />
              <TextInput className="field-input" label="Email Address" name="email" placeholder="you@hospital.com" value={formData.email} onChange={handleChange} required size="md" />
              <PasswordInput className="field-input" label="Password" name="password" placeholder="Your password" value={formData.password} onChange={handleChange} required size="md" />
              <Box className="submit-btn" mt={6}><Button type="submit" fullWidth size="md" loading={loading} radius="md">Sign In</Button></Box>
            </Stack>
          </form>

          <Divider my={24} style={{ borderColor: 'rgba(255,255,255,0.06)' }} />
          <Text ta="center" style={{ color: '#475569', fontSize: '0.875rem' }}>
            Don't have an account?{' '}
            <Anchor className="nav-anchor" component="button" onClick={() => navigate('/register')}>Create one</Anchor>
          </Text>
        </Paper>
      </Box>
    </Box>
  );
};

export default Login;