/**
 * SearchLayout.jsx
 * ─────────────────────────────────────────────────────────────
 * Shared layout shell for every search/module page.
 * Provides the filter panel card, results table wrapper,
 * empty state, and pagination — all pre-styled to match
 * the Privacy API dark-glass design system.
 *
 * Usage:
 *   <SearchLayout
 *     icon={<Calendar size={18} color="#22d3ee" />}
 *     title="Search Appointments"
 *     accent="#22d3ee"
 *     onSearch={handleSearch}
 *     onClear={handleClear}
 *     loading={loading}
 *     error={error}
 *     results={results}
 *     totalPages={totalPages}
 *     activePage={activePage}
 *     onPageChange={setPage}
 *     columns={['ID', 'Date', 'Patient', ...]}
 *     emptyText="No appointments found."
 *   >
 *     {/ filter inputs go here as children /}
 *   </SearchLayout>
 *
 *   Then render the <tbody> rows yourself via the `rows` prop
 *   OR wrap a <SearchTable> inside.
 */

import React from 'react';
import {
  Box,
  Grid,
  Text,
  Group,
  Alert,
  ScrollArea,
  Pagination,
  Table,
} from '@mantine/core';
import { Search, X, AlertCircle, Inbox } from 'lucide-react';

// ─── SHARED STYLES (injected once) ───────────────────────────────────────────
export function SearchStyles({ accent = '#22d3ee' }) {
  return (
    <style>{`
      /* ── Glass card ── */
      .sp-card {
        background: rgba(9, 15, 29, 0.70) !important;
        border: 1px solid rgba(255,255,255,0.07) !important;
        border-radius: 18px !important;
        backdrop-filter: blur(18px);
        overflow: hidden;
      }

      /* ── Section headers ── */
      .sp-section-label {
        font-size: 0.68rem;
        font-weight: 600;
        letter-spacing: 0.13em;
        text-transform: uppercase;
        color: ${accent};
        margin-bottom: 4px;
      }
      .sp-section-title {
        font-family: 'Bricolage Grotesque', sans-serif;
        font-weight: 700;
        color: #f1f5f9;
        font-size: 1.05rem;
        letter-spacing: -0.015em;
      }

      /* ── Field labels ── */
      .sp-card .mantine-TextInput-label,
      .sp-card .mantine-NumberInput-label,
      .sp-card .mantine-MultiSelect-label,
      .sp-card .mantine-Select-label {
        color: #475569 !important;
        font-size: 0.72rem !important;
        font-weight: 500 !important;
        letter-spacing: 0.07em !important;
        text-transform: uppercase !important;
        margin-bottom: 5px !important;
      }

      /* ── Inputs ── */
      .sp-card .mantine-TextInput-input,
      .sp-card .mantine-NumberInput-input,
      .sp-card .mantine-MultiSelect-input,
      .sp-card .mantine-Select-input {
        background: rgba(255,255,255,0.03) !important;
        border: 1px solid rgba(255,255,255,0.09) !important;
        color: #e2e8f0 !important;
        font-size: 0.875rem !important;
        transition: border-color 0.18s ease, box-shadow 0.18s ease !important;
      }
      .sp-card .mantine-TextInput-input::placeholder,
      .sp-card .mantine-NumberInput-input::placeholder { color: rgba(255,255,255,0.2) !important; }
      .sp-card .mantine-TextInput-input:focus,
      .sp-card .mantine-NumberInput-input:focus,
      .sp-card .mantine-MultiSelect-input:focus-within,
      .sp-card .mantine-Select-input:focus {
        border-color: ${accent} !important;
        box-shadow: 0 0 0 3px ${accent}18 !important;
      }

      /* MultiSelect pills */
      .sp-card .mantine-MultiSelect-pill {
        background: ${accent}18 !important;
        border: 1px solid ${accent}30 !important;
        color: ${accent} !important;
        font-size: 0.75rem !important;
      }

      /* Select dropdown */
      .mantine-Select-dropdown,
      .mantine-MultiSelect-dropdown {
        background: #0d1525 !important;
        border: 1px solid rgba(255,255,255,0.09) !important;
      }
      .mantine-Select-option,
      .mantine-MultiSelect-option {
        color: #94a3b8 !important;
        font-size: 0.85rem !important;
      }
      .mantine-Select-option:hover,
      .mantine-MultiSelect-option:hover { background: rgba(255,255,255,0.05) !important; color: #e2e8f0 !important; }
      .mantine-Select-option[data-combobox-selected],
      .mantine-MultiSelect-option[data-combobox-selected] { background: ${accent}18 !important; color: ${accent} !important; }

      /* ── Date input calendar icon ── */
      input[type="date"]::-webkit-calendar-picker-indicator { filter: invert(0.4); cursor: pointer; }

      /* ── Divider ── */
      .sp-rule {
        height: 1px;
        background: linear-gradient(90deg, rgba(255,255,255,0.07) 0%, transparent 60%);
        margin: 20px 0;
      }

      /* ── Search & Clear buttons ── */
      .sp-search-btn {
        display: inline-flex !important;
        align-items: center !important;
        gap: 7px !important;
        padding: 9px 20px !important;
        border-radius: 10px !important;
        background: linear-gradient(135deg, ${accent}, color-mix(in srgb, ${accent} 65%, #6d28d9)) !important;
        border: none !important;
        color: #fff !important;
        font-size: 0.85rem !important;
        font-weight: 600 !important;
        font-family: 'Figtree', sans-serif !important;
        cursor: pointer !important;
        transition: opacity 0.18s ease, transform 0.15s ease, box-shadow 0.18s ease !important;
        box-shadow: 0 4px 18px ${accent}35 !important;
        letter-spacing: 0.01em;
      }
      .sp-search-btn:hover:not(:disabled) {
        opacity: 0.88 !important;
        transform: translateY(-1px) !important;
        box-shadow: 0 7px 24px ${accent}45 !important;
      }
      .sp-search-btn:disabled { opacity: 0.5 !important; cursor: not-allowed !important; }

      .sp-clear-btn {
        display: inline-flex !important;
        align-items: center !important;
        gap: 6px !important;
        padding: 9px 16px !important;
        border-radius: 10px !important;
        background: transparent !important;
        border: 1px solid rgba(255,255,255,0.09) !important;
        color: #475569 !important;
        font-size: 0.85rem !important;
        font-weight: 500 !important;
        font-family: 'Figtree', sans-serif !important;
        cursor: pointer !important;
        transition: all 0.18s ease !important;
      }
      .sp-clear-btn:hover { background: rgba(255,255,255,0.05) !important; color: #94a3b8 !important; border-color: rgba(255,255,255,0.15) !important; }

      /* ── Table ── */
      .sp-table {
        border-collapse: separate !important;
        border-spacing: 0 !important;
        width: 100%;
      }
      .sp-table thead tr {
        background: rgba(255,255,255,0.025);
      }
      .sp-table thead th {
        color: #334155 !important;
        font-size: 0.7rem !important;
        font-weight: 600 !important;
        letter-spacing: 0.09em !important;
        text-transform: uppercase !important;
        padding: 10px 14px !important;
        border-bottom: 1px solid rgba(255,255,255,0.06) !important;
        white-space: nowrap;
        background: transparent !important;
      }
      .sp-table tbody tr {
        transition: background 0.15s ease !important;
      }
      .sp-table tbody tr:hover td { background: rgba(255,255,255,0.03) !important; }
      .sp-table tbody td {
        color: #94a3b8 !important;
        font-size: 0.845rem !important;
        padding: 11px 14px !important;
        border-bottom: 1px solid rgba(255,255,255,0.04) !important;
        background: transparent;
        vertical-align: middle;
        white-space: nowrap;
      }
      .sp-table tbody tr:last-child td { border-bottom: none !important; }
      .sp-table .td-primary { color: #e2e8f0 !important; font-weight: 600 !important; }
      .sp-table .td-mono { font-family: 'JetBrains Mono', monospace !important; font-size: 0.8rem !important; color: #64748b !important; }

      /* ── Pagination ── */
      .sp-card .mantine-Pagination-control {
        background: rgba(255,255,255,0.04) !important;
        border: 1px solid rgba(255,255,255,0.08) !important;
        color: #64748b !important;
        transition: all 0.15s ease !important;
      }
      .sp-card .mantine-Pagination-control:hover:not([data-disabled]) { background: rgba(255,255,255,0.08) !important; color: #e2e8f0 !important; }
      .sp-card .mantine-Pagination-control[data-active] {
        background: ${accent}22 !important;
        border-color: ${accent}44 !important;
        color: ${accent} !important;
      }

      /* ── Error alert ── */
      .sp-error .mantine-Alert-root {
        background: rgba(239,68,68,0.07) !important;
        border: 1px solid rgba(239,68,68,0.22) !important;
        border-radius: 12px !important;
      }
      .sp-error .mantine-Alert-message { color: #fca5a5 !important; font-size: 0.85rem !important; }

      /* ── Results count badge ── */
      .sp-count {
        display: inline-flex;
        align-items: center;
        padding: 3px 10px;
        border-radius: 100px;
        background: ${accent}12;
        border: 1px solid ${accent}25;
        color: ${accent};
        font-size: 0.75rem;
        font-weight: 600;
        letter-spacing: 0.04em;
      }

      @keyframes spFadeUp {
        from { opacity:0; transform:translateY(14px); }
        to   { opacity:1; transform:translateY(0); }
      }
      .sp-animate { animation: spFadeUp 0.4s cubic-bezier(0.22,1,0.36,1) forwards; }
      .sp-animate-delay { animation: spFadeUp 0.4s 0.08s cubic-bezier(0.22,1,0.36,1) both; }
    `}</style>
  );
}

// ─── FILTER CARD ─────────────────────────────────────────────────────────────
export function FilterCard({ icon, title, accent = '#22d3ee', onSearch, onClear, loading, error, children }) {
  return (
    <Box className="sp-card sp-animate" mb={20} p={28}>
      {/* Header */}
      <Group align="center" justify="space-between" mb={22}>
        <Group align="center" gap={10}>
          <Box style={{
            width: 36, height: 36, borderRadius: 10,
            background: `${accent}14`, border: `1px solid ${accent}28`,
            display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
          }}>
            {icon}
          </Box>
          <Box>
            <div className="sp-section-label">Module</div>
            <div className="sp-section-title">{title}</div>
          </Box>
        </Group>
      </Group>

      <Box className="sp-rule" />

      {error && (
        <Box className="sp-error" mb={18}>
          <Alert icon={<AlertCircle size={15} />} color="red" radius="md">{error}</Alert>
        </Box>
      )}

      {/* Filter fields */}
      {children}

      {/* Actions */}
      <Group justify="flex-end" mt={22} gap={8}>
        <button className="sp-clear-btn" onClick={onClear} type="button">
          <X size={13} strokeWidth={2.5} />
          Clear
        </button>
        <button
          className="sp-search-btn"
          onClick={onSearch}
          disabled={loading}
          type="button"
        >
          {loading ? (
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83">
                <animateTransform attributeName="transform" type="rotate" from="0 12 12" to="360 12 12" dur="0.7s" repeatCount="indefinite"/>
              </path>
            </svg>
          ) : (
            <Search size={14} strokeWidth={2.5} />
          )}
          {loading ? 'Searching…' : 'Search'}
        </button>
      </Group>
    </Box>
  );
}

// ─── RESULTS CARD ─────────────────────────────────────────────────────────────
export function ResultsCard({ accent = '#22d3ee', count, columns, emptyText = 'No results found.', children, totalPages, activePage, onPageChange }) {
  return (
    <Box className="sp-card sp-animate-delay" p={28}>
      {/* Header */}
      <Group justify="space-between" align="center" mb={20}>
        <Group align="center" gap={10}>
          <Text style={{ fontFamily: "'Bricolage Grotesque', sans-serif", fontWeight: 700, color: '#e2e8f0', fontSize: '0.95rem' }}>
            Results
          </Text>
          <span className="sp-count">{count} records</span>
        </Group>
      </Group>

      <Box className="sp-rule" style={{ marginTop: 0 }} />

      <ScrollArea>
        <table className="sp-table" style={{ minWidth: 700 }}>
          <thead>
            <tr>
              {columns.map(col => <th key={col}>{col}</th>)}
            </tr>
          </thead>
          <tbody>
            {count > 0 ? children : (
              <tr>
                <td colSpan={columns.length}>
                  <Box style={{ padding: '48px 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 12 }}>
                    <Box style={{
                      width: 44, height: 44, borderRadius: 12,
                      background: 'rgba(255,255,255,0.04)', border: '1px solid rgba(255,255,255,0.07)',
                      display: 'flex', alignItems: 'center', justifyContent: 'center',
                    }}>
                      <Inbox size={20} color="#334155" strokeWidth={1.5} />
                    </Box>
                    <Text style={{ color: '#334155', fontSize: '0.85rem' }}>{emptyText}</Text>
                  </Box>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </ScrollArea>

      {totalPages > 1 && (
        <>
          <Box className="sp-rule" />
          <Group justify="center">
            <Pagination total={totalPages} value={activePage} onChange={onPageChange} color="cyan" withEdges size="sm" />
          </Group>
        </>
      )}
    </Box>
  );
}

// ─── SMALL BADGE HELPERS ──────────────────────────────────────────────────────
export function StatusBadge({ status }) {
  const map = {
    COMPLETED:  { bg: '#22c55e18', border: '#22c55e30', color: '#4ade80' },
    SCHEDULED:  { bg: '#f59e0b18', border: '#f59e0b30', color: '#fcd34d' },
    CANCELLED:  { bg: '#ef444418', border: '#ef444430', color: '#f87171' },
    NO_SHOW:    { bg: '#64748b18', border: '#64748b30', color: '#94a3b8' },
    ACTIVE:     { bg: '#22d3ee18', border: '#22d3ee30', color: '#22d3ee' },
    ONLINE:     { bg: '#38bdf818', border: '#38bdf830', color: '#7dd3fc' },
    IN_PERSON:  { bg: '#34d39918', border: '#34d39930', color: '#6ee7b7' },
    PHONE:      { bg: '#a78bfa18', border: '#a78bfa30', color: '#c4b5fd' },
  };
  const s = map[status] || { bg: '#ffffff0d', border: '#ffffff15', color: '#94a3b8' };
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center', gap: 5,
      padding: '3px 9px', borderRadius: 100,
      background: s.bg, border: `1px solid ${s.border}`,
      color: s.color, fontSize: '0.73rem', fontWeight: 600,
      letterSpacing: '0.04em', whiteSpace: 'nowrap',
    }}>
      <span style={{ width: 5, height: 5, borderRadius: '50%', background: s.color, flexShrink: 0 }} />
      {status}
    </span>
  );
}

export function PillBadge({ label, color = '#22d3ee' }) {
  return (
    <span style={{
      display: 'inline-flex', alignItems: 'center',
      padding: '3px 9px', borderRadius: 100,
      background: `${color}12`, border: `1px solid ${color}28`,
      color, fontSize: '0.73rem', fontWeight: 600,
      letterSpacing: '0.03em', whiteSpace: 'nowrap',
    }}>
      {label}
    </span>
  );
}