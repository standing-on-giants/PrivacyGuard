import React, { useState } from 'react';
import {
  Box,
  Card,
  Grid,
  TextInput,
  Button,
  Table,
  Text,
  Badge,
  Group,
  Alert,
  ScrollArea,
  Title,
  Pagination,
} from '@mantine/core';

const ITEMS_PER_PAGE = 10;

const RoomRecordsSearch = () => {
  // State for the request payload
  const [filters, setFilters] = useState({
    roomNos: '',
    patientIds: '',
    nurseIds: '',
    helperIds: '',
    admissionDateStart: '',
    admissionDateEnd: '',
    dischargeDateStart: '',
    dischargeDateEnd: ''
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Pagination state
  const [activePage, setPage] = useState(1);

  // Helper to convert comma-separated strings into integer arrays
  const parseIds = (idString) => {
    if (!idString) return null;
    const parsed = idString.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));
    return parsed.length > 0 ? parsed : null;
  };

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setPage(1); // Reset to first page on new search

    // Build the exact RoomRecordSearchRequest DTO expected by Spring Boot
    const requestPayload = {
      roomNos: parseIds(filters.roomNos),
      patientIds: parseIds(filters.patientIds),
      nurseIds: parseIds(filters.nurseIds),
      helperIds: parseIds(filters.helperIds),
      admissionDateStart: filters.admissionDateStart || null,
      admissionDateEnd: filters.admissionDateEnd || null,
      dischargeDateStart: filters.dischargeDateStart || null,
      dischargeDateEnd: filters.dischargeDateEnd || null
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const response = await fetch(`${baseUrl}/room-records/search`, {
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
      setError('Failed to fetch room records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilters({
      roomNos: '', patientIds: '', nurseIds: '', helperIds: '',
      admissionDateStart: '', admissionDateEnd: '',
      dischargeDateStart: '', dischargeDateEnd: ''
    });
  };

  // Calculate Paginated Data
  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Title order={4} mb="lg" style={{ color: '#f8fafc' }}>Search Room Records</Title>
        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          {/* ID Filters */}
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Room Numbers" placeholder="e.g. 101, 204" value={filters.roomNos} onChange={(e) => setFilters({ ...filters, roomNos: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Patient IDs" placeholder="e.g. 10, 15" value={filters.patientIds} onChange={(e) => setFilters({ ...filters, patientIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Nurse IDs" placeholder="e.g. 2, 5" value={filters.nurseIds} onChange={(e) => setFilters({ ...filters, nurseIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
            <TextInput label="Helper IDs" placeholder="e.g. 1, 8" value={filters.helperIds} onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>

          {/* Admission Dates */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Group grow>
              <TextInput type="date" label="Admission Start Date" value={filters.admissionDateStart} onChange={(e) => setFilters({ ...filters, admissionDateStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="date" label="Admission End Date" value={filters.admissionDateEnd} onChange={(e) => setFilters({ ...filters, admissionDateEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>

          {/* Discharge Dates */}
          <Grid.Col span={{ base: 12, md: 6 }}>
            <Group grow>
              <TextInput type="date" label="Discharge Start Date" value={filters.dischargeDateStart} onChange={(e) => setFilters({ ...filters, dischargeDateStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="date" label="Discharge End Date" value={filters.dischargeDateEnd} onChange={(e) => setFilters({ ...filters, dischargeDateEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">
            Clear
          </Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">
            Search Records
          </Button>
        </Group>
      </Card>

      {/* RESULTS TABLE */}
      <Card withBorder shadow="sm" radius="md" p="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>
        </Group>

        <ScrollArea>
          <Table striped highlightOnHover verticalSpacing="sm" style={{ color: '#e2e8f0', minWidth: 800 }}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>Record ID</Table.Th>
                <Table.Th>Room No</Table.Th>
                <Table.Th>Admission Date</Table.Th>
                <Table.Th>Discharge Date</Table.Th>
                <Table.Th>Patient</Table.Th>
                <Table.Th>Nurse</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((record) => (
                  <Table.Tr key={record.recordId}>
                    <Table.Td fw={600}>{record.recordId}</Table.Td>
                    <Table.Td>
                      <Badge color="violet" variant="light">Rm {record.roomNo}</Badge>
                    </Table.Td>
                    <Table.Td>{record.admissionDate}</Table.Td>
                    <Table.Td>
                      {record.dischargeDate ? (
                        record.dischargeDate
                      ) : (
                        <Badge color="yellow" variant="dot">Occupied</Badge>
                      )}
                    </Table.Td>
                    <Table.Td>{record.patientFirstName} {record.patientLastName}</Table.Td>
                    <Table.Td>{record.nurseFirstName} {record.nurseLastName}</Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={6}>
                    <Text c="dimmed" ta="center" py="xl">No room records found matching your criteria.</Text>
                  </Table.Td>
                </Table.Tr>
              )}
            </Table.Tbody>
          </Table>
        </ScrollArea>

        {/* PAGINATION CONTROLS */}
        {totalPages > 1 && (
          <Group justify="center" mt="xl">
            <Pagination 
              total={totalPages} 
              value={activePage} 
              onChange={setPage} 
              color="cyan" 
              withEdges 
            />
          </Group>
        )}
      </Card>
    </Box>
  );
};

export default RoomRecordsSearch;