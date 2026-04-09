import React, { useState } from 'react';
import {
  Box, Card, Grid, TextInput, MultiSelect, Button, Table,
  Text, Badge, Group, Alert, ScrollArea, Title, Pagination,
  Switch, ThemeIcon,
} from '@mantine/core';
import { IconShieldCheck, IconShieldOff } from '@tabler/icons-react';

const ITEMS_PER_PAGE = 10;

const AppointmentSearch = () => {
  const [filters, setFilters] = useState({
    patientIds: '', doctorIds: '', modesOfAppointment: [],
    appointmentStatuses: [], dateStart: '', dateEnd: ''
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
      patientIds: parseIds(filters.patientIds),
      doctorIds: parseIds(filters.doctorIds),
      modesOfAppointment: filters.modesOfAppointment.length > 0 ? filters.modesOfAppointment : null,
      appointmentStatuses: filters.appointmentStatuses.length > 0 ? filters.appointmentStatuses : null,
      dateStart: filters.dateStart || null,
      dateEnd: filters.dateEnd || null
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const endpoint = privacyEnabled
        ? `${baseUrl}/appointments/search`
        : `${baseUrl}/raw/appointments/search`;

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(requestPayload)
      });

      if (!response.ok) throw new Error(`Server returned ${response.status}`);

      const data = await response.json();
      setResults(data);
    } catch (err) {
      setError('Failed to fetch appointments. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>

        <Group justify="space-between" mb="lg" align="center">
          <Title order={4} style={{ color: '#f8fafc' }}>Search Appointments</Title>

          {/* PRIVACY TOGGLE */}
          <Group
            gap="sm"
            align="center"
            style={{
              background: privacyEnabled ? 'rgba(239,68,68,0.1)' : 'rgba(34,197,94,0.1)',
              border: `1px solid ${privacyEnabled ? 'rgba(239,68,68,0.3)' : 'rgba(34,197,94,0.3)'}`,
              borderRadius: '8px',
              padding: '8px 14px',
              transition: 'all 0.3s ease',
            }}
          >
            <ThemeIcon variant="transparent" color={privacyEnabled ? 'red' : 'green'} size="sm">
              {privacyEnabled
                ? <IconShieldCheck size={16} stroke={1.5} />
                : <IconShieldOff size={16} stroke={1.5} />}
            </ThemeIcon>
            <Text size="sm" fw={500} style={{ color: privacyEnabled ? '#f87171' : '#4ade80' }}>
              {privacyEnabled ? 'Privacy ON' : 'Privacy OFF'}
            </Text>
            <Switch
              checked={!privacyEnabled}
              onChange={(e) => {
                setPrivacyEnabled(!e.currentTarget.checked);
                setResults([]);
                setPage(1);
              }}
              color="green"
              size="md"
            />
            <Text size="xs" c="dimmed">Raw API</Text>
          </Group>
        </Group>

        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput label="Patient IDs" placeholder="e.g. 101, 102, 105"
              value={filters.patientIds}
              onChange={(e) => setFilters({ ...filters, patientIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput label="Doctor IDs" placeholder="e.g. 1, 2, 3"
              value={filters.doctorIds}
              onChange={(e) => setFilters({ ...filters, doctorIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <MultiSelect label="Appointment Mode" placeholder="Select modes"
              data={['IN_PERSON', 'ONLINE', 'PHONE']}
              value={filters.modesOfAppointment}
              onChange={(val) => setFilters({ ...filters, modesOfAppointment: val })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <MultiSelect label="Status" placeholder="Select statuses"
              data={['SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW']}
              value={filters.appointmentStatuses}
              onChange={(val) => setFilters({ ...filters, appointmentStatuses: val })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput type="date" label="Start Date"
              value={filters.dateStart}
              onChange={(e) => setFilters({ ...filters, dateStart: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, md: 6 }}>
            <TextInput type="date" label="End Date"
              value={filters.dateEnd}
              onChange={(e) => setFilters({ ...filters, dateEnd: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button
            onClick={() => setFilters({ patientIds: '', doctorIds: '', modesOfAppointment: [], appointmentStatuses: [], dateStart: '', dateEnd: '' })}
            variant="subtle" color="gray">
            Clear
          </Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">
            Search Records
          </Button>
        </Group>
      </Card>

      {/* RESULTS TABLE */}
      <Card withBorder shadow="sm" radius="md" p="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>

        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>

          {/* ACTIVE API BADGE */}
          <Badge
            color={privacyEnabled ? 'red' : 'green'}
            variant="light"
            size="lg"
            leftSection={
              privacyEnabled
                ? <IconShieldCheck size={12} stroke={1.5} />
                : <IconShieldOff size={12} stroke={1.5} />
            }
          >
            {privacyEnabled ? '/api/appointments/search' : '/api/raw/appointments/search'}
          </Badge>
        </Group>

        <ScrollArea>
          <Table striped highlightOnHover verticalSpacing="sm" style={{ color: '#e2e8f0' }}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>ID</Table.Th>
                <Table.Th>Date</Table.Th>
                <Table.Th>Patient</Table.Th>
                <Table.Th>Doctor</Table.Th>
                <Table.Th>Mode</Table.Th>
                <Table.Th>Status</Table.Th>
                <Table.Th>Amount</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((apt) => (
                  <Table.Tr key={apt.appointmentId}>
                    <Table.Td fw={600}>{apt.appointmentId}</Table.Td>
                    <Table.Td>{apt.appointmentDate}</Table.Td>
                    <Table.Td>{apt.patientFirstName} {apt.patientLastName}</Table.Td>
                    <Table.Td>{apt.doctorFirstName} {apt.doctorLastName}</Table.Td>
                    <Table.Td>
                      <Badge color={apt.modeOfAppointment === 'ONLINE' ? 'blue' : 'teal'} variant="light">
                        {apt.modeOfAppointment}
                      </Badge>
                    </Table.Td>
                    <Table.Td>
                      <Badge
                        color={apt.appointmentStatus === 'COMPLETED' ? 'green' : apt.appointmentStatus === 'CANCELLED' ? 'red' : 'yellow'}
                        variant="dot">
                        {apt.appointmentStatus}
                      </Badge>
                    </Table.Td>
                    <Table.Td>${apt.paymentAmount}</Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={7}>
                    <Text c="dimmed" ta="center" py="xl">No appointments found matching your criteria.</Text>
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

export default AppointmentSearch;