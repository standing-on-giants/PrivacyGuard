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

const StaffShiftSearch = () => {
  // State for the request payload
  const [filters, setFilters] = useState({
    doctorIds: '',
    nurseIds: '',
    helperIds: '',
    shiftDateStart: '',
    shiftDateEnd: '',
    shiftStartStart: '',
    shiftStartEnd: '',
    shiftEndStart: '',
    shiftEndEnd: ''
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

    // Build the exact StaffShiftSearchRequest DTO expected by Spring Boot
    const requestPayload = {
      doctorIds: parseIds(filters.doctorIds),
      nurseIds: parseIds(filters.nurseIds),
      helperIds: parseIds(filters.helperIds),
      // We are formatting time strings slightly if needed, though standard HTML5 "HH:mm" is usually accepted by Spring's LocalTime
      shiftDateStart: filters.shiftDateStart || null,
      shiftDateEnd: filters.shiftDateEnd || null,
      shiftStartStart: filters.shiftStartStart ? `${filters.shiftStartStart}:00` : null, // append seconds for robust LocalTime parsing
      shiftStartEnd: filters.shiftStartEnd ? `${filters.shiftStartEnd}:00` : null,
      shiftEndStart: filters.shiftEndStart ? `${filters.shiftEndStart}:00` : null,
      shiftEndEnd: filters.shiftEndEnd ? `${filters.shiftEndEnd}:00` : null
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const response = await fetch(`${baseUrl}/staff-shifts/search`, {
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
      setError('Failed to fetch staff shifts. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilters({
      doctorIds: '', nurseIds: '', helperIds: '',
      shiftDateStart: '', shiftDateEnd: '',
      shiftStartStart: '', shiftStartEnd: '',
      shiftEndStart: '', shiftEndEnd: ''
    });
  };

  // Calculate Paginated Data
  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Title order={4} mb="lg" style={{ color: '#f8fafc' }}>Search Staff Shifts</Title>
        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          {/* ID Filters */}
          <Grid.Col span={{ base: 12, sm: 4 }}>
            <TextInput label="Doctor IDs" placeholder="e.g. 10, 12" value={filters.doctorIds} onChange={(e) => setFilters({ ...filters, doctorIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 4 }}>
            <TextInput label="Nurse IDs" placeholder="e.g. 2, 5" value={filters.nurseIds} onChange={(e) => setFilters({ ...filters, nurseIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 4 }}>
            <TextInput label="Helper IDs" placeholder="e.g. 1, 8" value={filters.helperIds} onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>

          {/* Date Ranges */}
          <Grid.Col span={{ base: 12, md: 4 }}>
            <Group grow>
              <TextInput type="date" label="Shift Date (From)" value={filters.shiftDateStart} onChange={(e) => setFilters({ ...filters, shiftDateStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="date" label="Shift Date (To)" value={filters.shiftDateEnd} onChange={(e) => setFilters({ ...filters, shiftDateEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>

          {/* Time Ranges: Shift Start */}
          <Grid.Col span={{ base: 12, md: 4 }}>
            <Group grow>
              <TextInput type="time" label="Shift Starts After" value={filters.shiftStartStart} onChange={(e) => setFilters({ ...filters, shiftStartStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="time" label="Shift Starts Before" value={filters.shiftStartEnd} onChange={(e) => setFilters({ ...filters, shiftStartEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>

          {/* Time Ranges: Shift End */}
          <Grid.Col span={{ base: 12, md: 4 }}>
            <Group grow>
              <TextInput type="time" label="Shift Ends After" value={filters.shiftEndStart} onChange={(e) => setFilters({ ...filters, shiftEndStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
              <TextInput type="time" label="Shift Ends Before" value={filters.shiftEndEnd} onChange={(e) => setFilters({ ...filters, shiftEndEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
            </Group>
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">
            Clear
          </Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">
            Search Shifts
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
                <Table.Th>Shift ID</Table.Th>
                <Table.Th>Shift Date</Table.Th>
                <Table.Th>Schedule (Start - End)</Table.Th>
                <Table.Th>Doctor</Table.Th>
                <Table.Th>Nurse</Table.Th>
                <Table.Th>Helper</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((shift) => (
                  <Table.Tr key={shift.shiftId}>
                    <Table.Td fw={600}>{shift.shiftId}</Table.Td>
                    <Table.Td>{shift.shiftDate}</Table.Td>
                    <Table.Td>
                      <Badge color="violet" variant="light" size="lg">
                        {shift.shiftStart} - {shift.shiftEnd}
                      </Badge>
                    </Table.Td>
                    <Table.Td>
                      {shift.doctorFirstName ? `${shift.doctorFirstName} ${shift.doctorLastName}` : <Text c="dimmed" size="xs">N/A</Text>}
                    </Table.Td>
                    <Table.Td>
                      {shift.nurseFirstName ? `${shift.nurseFirstName} ${shift.nurseLastName}` : <Text c="dimmed" size="xs">N/A</Text>}
                    </Table.Td>
                    <Table.Td>
                      {shift.helperFirstName ? `${shift.helperFirstName} ${shift.helperLastName}` : <Text c="dimmed" size="xs">N/A</Text>}
                    </Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={6}>
                    <Text c="dimmed" ta="center" py="xl">No shifts found matching your criteria.</Text>
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

export default StaffShiftSearch;