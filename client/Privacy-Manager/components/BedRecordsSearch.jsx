import React, { useState } from 'react';
import {
  Box, Card, Grid, TextInput, NumberInput, Button, Table,
  Text, Badge, Group, Alert, ScrollArea, Title, Pagination,
  Switch, ThemeIcon,
} from '@mantine/core';
import { IconShieldCheck, IconShieldOff } from '@tabler/icons-react';

const ITEMS_PER_PAGE = 10;

const BedRecordsSearch = () => {
  const [filters, setFilters] = useState({
    bedNos: '', patientIds: '', nurseIds: '', helperIds: '',
    admissionDateStart: '', admissionDateEnd: '',
    dischargeDateStart: '', dischargeDateEnd: '',
    minAmount: '', maxAmount: ''
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activePage, setPage] = useState(1);
  const [privacyEnabled, setPrivacyEnabled] = useState(true);

  const parseIds = (idString) => {
    if (!idString) return [];
    return idString.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));
  };

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setPage(1);

    const requestPayload = {
      bedNos: parseIds(filters.bedNos),
      patientIds: parseIds(filters.patientIds),
      nurseIds: parseIds(filters.nurseIds),
      helperIds: parseIds(filters.helperIds),
      admissionDateStart: filters.admissionDateStart || null,
      admissionDateEnd: filters.admissionDateEnd || null,
      dischargeDateStart: filters.dischargeDateStart || null,
      dischargeDateEnd: filters.dischargeDateEnd || null,
      minAmount: filters.minAmount !== '' ? filters.minAmount : null,
      maxAmount: filters.maxAmount !== '' ? filters.maxAmount : null
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const endpoint = privacyEnabled
        ? `${baseUrl}/bed-records/search`
        : `${baseUrl}/raw/bed-records/search`;

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(requestPayload)
      });

      if (!response.ok) throw new Error(`Server returned ${response.status}`);
      setResults(await response.json());
    } catch (err) {
      setError('Failed to fetch bed records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => setFilters({
    bedNos: '', patientIds: '', nurseIds: '', helperIds: '',
    admissionDateStart: '', admissionDateEnd: '',
    dischargeDateStart: '', dischargeDateEnd: '',
    minAmount: '', maxAmount: ''
  });

  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>

        <Group justify="space-between" mb="lg" align="center">
          <Title order={4} style={{ color: '#f8fafc' }}>Search Bed Records</Title>

          <Group gap="sm" align="center"
            style={{
              background: privacyEnabled ? 'rgba(239,68,68,0.1)' : 'rgba(34,197,94,0.1)',
              border: `1px solid ${privacyEnabled ? 'rgba(239,68,68,0.3)' : 'rgba(34,197,94,0.3)'}`,
              borderRadius: '8px', padding: '8px 14px', transition: 'all 0.3s ease',
            }}>
            <ThemeIcon variant="transparent" color={privacyEnabled ? 'red' : 'green'} size="sm">
              {privacyEnabled ? <IconShieldCheck size={16} stroke={1.5} /> : <IconShieldOff size={16} stroke={1.5} />}
            </ThemeIcon>
            <Text size="sm" fw={500} style={{ color: privacyEnabled ? '#f87171' : '#4ade80' }}>
              {privacyEnabled ? 'Privacy ON' : 'Privacy OFF'}
            </Text>
            <Switch
              checked={!privacyEnabled}
              onChange={(e) => { setPrivacyEnabled(!e.currentTarget.checked); setResults([]); setPage(1); }}
              color="green" size="md"
            />
            <Text size="xs" c="dimmed">Raw API</Text>
          </Group>
        </Group>

        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Bed Numbers" placeholder="e.g. 10, 12" value={filters.bedNos}
              onChange={(e) => setFilters({ ...filters, bedNos: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Patient IDs" placeholder="e.g. 101, 102" value={filters.patientIds}
              onChange={(e) => setFilters({ ...filters, patientIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Nurse IDs" placeholder="e.g. 5, 8" value={filters.nurseIds}
              onChange={(e) => setFilters({ ...filters, nurseIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Helper IDs" placeholder="e.g. 2, 4" value={filters.helperIds}
              onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6 }}>
            <Group grow>
              <TextInput type="date" label="Admission Start" value={filters.admissionDateStart}
                onChange={(e) => setFilters({ ...filters, admissionDateStart: e.currentTarget.value })}
                styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="date" label="Admission End" value={filters.admissionDateEnd}
                onChange={(e) => setFilters({ ...filters, admissionDateEnd: e.currentTarget.value })}
                styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6 }}>
            <Group grow>
              <TextInput type="date" label="Discharge Start" value={filters.dischargeDateStart}
                onChange={(e) => setFilters({ ...filters, dischargeDateStart: e.currentTarget.value })}
                styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="date" label="Discharge End" value={filters.dischargeDateEnd}
                onChange={(e) => setFilters({ ...filters, dischargeDateEnd: e.currentTarget.value })}
                styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6 }}>
            <Group grow>
              <NumberInput label="Min Amount ($)" placeholder="0" value={filters.minAmount}
                onChange={(val) => setFilters({ ...filters, minAmount: val })}
                styles={{ label: { color: '#cbd5e1' } }} />
              <NumberInput label="Max Amount ($)" placeholder="10000" value={filters.maxAmount}
                onChange={(val) => setFilters({ ...filters, maxAmount: val })}
                styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">Clear</Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">Search Records</Button>
        </Group>
      </Card>

      <Card withBorder shadow="sm" radius="md" p="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>
          <Badge color={privacyEnabled ? 'red' : 'green'} variant="light" size="lg"
            leftSection={privacyEnabled ? <IconShieldCheck size={12} stroke={1.5} /> : <IconShieldOff size={12} stroke={1.5} />}>
            {privacyEnabled ? '/api/bed-records/search' : '/api/raw/bed-records/search'}
          </Badge>
        </Group>

        <ScrollArea>
          <Table striped highlightOnHover verticalSpacing="sm" style={{ color: '#e2e8f0', minWidth: 800 }}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Adm. ID</Table.Th>
                <Table.Th>Bed No</Table.Th>
                <Table.Th>Admission</Table.Th>
                <Table.Th>Discharge</Table.Th>
                <Table.Th>Patient</Table.Th>
                <Table.Th>Nurse</Table.Th>
                <Table.Th>Helper</Table.Th>
                <Table.Th>Amount</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((record) => (
                  <Table.Tr key={record.admissionId}>
                    <Table.Td fw={600}>{record.admissionId}</Table.Td>
                    <Table.Td><Badge color="blue" variant="light">Bed {record.bedNo}</Badge></Table.Td>
                    <Table.Td>{record.admissionDate}</Table.Td>
                    <Table.Td>{record.dischargeDate ? record.dischargeDate : <Badge color="yellow" variant="dot">Active</Badge>}</Table.Td>
                    <Table.Td>{record.patientFirstName} {record.patientLastName}</Table.Td>
                    <Table.Td>{record.nurseFirstName} {record.nurseLastName}</Table.Td>
                    <Table.Td>{record.helperFirstName} {record.helperLastName}</Table.Td>
                    <Table.Td>${record.amount}</Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={8}>
                    <Text c="dimmed" ta="center" py="xl">No bed records found matching your criteria.</Text>
                  </Table.Td>
                </Table.Tr>
              )}
            </Table.Tbody>
          </Table>
        </ScrollArea>

        {totalPages > 1 && (
          <Group justify="center" mt="xl">
            <Pagination total={totalPages} value={activePage} onChange={setPage} color="cyan" withEdges />
          </Group>
        )}
      </Card>
    </Box>
  );
};

export default BedRecordsSearch;