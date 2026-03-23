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
  Accordion,
} from '@mantine/core';

const ITEMS_PER_PAGE = 10;

const SurgeryRecordSearch = () => {
  // State for the request payload
  const [filters, setFilters] = useState({
    patientIds: '',
    surgeonIds: '',
    roomNos: '',
    nurseIds: '',
    helperIds: '',
    surgeryTypes: '',
    surgeryTypeKeyword: '',
    surgeryDateStart: '',
    surgeryDateEnd: '',
    startTimeStart: '',
    startTimeEnd: '',
    endTimeStart: '',
    endTimeEnd: ''
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

  // Helper to convert comma-separated strings into string arrays
  const parseStrings = (str) => {
    if (!str) return null;
    const parsed = str.split(',').map(s => s.trim()).filter(Boolean);
    return parsed.length > 0 ? parsed : null;
  };

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setPage(1);

    // Build the exact SurgeryRecordSearchRequest DTO expected by Spring Boot
    const requestPayload = {
      patientIds: parseIds(filters.patientIds),
      surgeonIds: parseIds(filters.surgeonIds),
      roomNos: parseIds(filters.roomNos),
      nurseIds: parseIds(filters.nurseIds),
      helperIds: parseIds(filters.helperIds),
      surgeryTypes: parseStrings(filters.surgeryTypes),
      surgeryTypeKeyword: filters.surgeryTypeKeyword || null,
      surgeryDateStart: filters.surgeryDateStart || null,
      surgeryDateEnd: filters.surgeryDateEnd || null,
      startTimeStart: filters.startTimeStart ? `${filters.startTimeStart}:00` : null,
      startTimeEnd: filters.startTimeEnd ? `${filters.startTimeEnd}:00` : null,
      endTimeStart: filters.endTimeStart ? `${filters.endTimeStart}:00` : null,
      endTimeEnd: filters.endTimeEnd ? `${filters.endTimeEnd}:00` : null
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const response = await fetch(`${baseUrl}/surgery-records/search`, {
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
      setError('Failed to fetch surgery records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilters({
      patientIds: '', surgeonIds: '', roomNos: '', nurseIds: '', helperIds: '',
      surgeryTypes: '', surgeryTypeKeyword: '',
      surgeryDateStart: '', surgeryDateEnd: '',
      startTimeStart: '', startTimeEnd: '',
      endTimeStart: '', endTimeEnd: ''
    });
  };

  // Calculate Paginated Data
  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Title order={4} mb="lg" style={{ color: '#f8fafc' }}>Search Surgery Records</Title>
        {error && <Alert color="red" mb="md">{error}</Alert>}

        {/* Using Accordion to keep the UI clean since there are so many filters */}
        <Accordion variant="separated" defaultValue="details" styles={{ item: { backgroundColor: 'transparent', borderColor: 'rgba(255,255,255,0.1)' }, control: { color: '#f8fafc' } }}>
          
          {/* Main Details & IDs */}
          <Accordion.Item value="details">
            <Accordion.Control>Core Details & Staff IDs</Accordion.Control>
            <Accordion.Panel>
              <Grid>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Surgery Types (Exact)" placeholder="e.g. Appendectomy" value={filters.surgeryTypes} onChange={(e) => setFilters({ ...filters, surgeryTypes: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Surgery Keyword" placeholder="e.g. bypass" value={filters.surgeryTypeKeyword} onChange={(e) => setFilters({ ...filters, surgeryTypeKeyword: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Patient IDs" placeholder="e.g. 101, 105" value={filters.patientIds} onChange={(e) => setFilters({ ...filters, patientIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Surgeon IDs" placeholder="e.g. 1, 3" value={filters.surgeonIds} onChange={(e) => setFilters({ ...filters, surgeonIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Nurse IDs" placeholder="e.g. 2, 5" value={filters.nurseIds} onChange={(e) => setFilters({ ...filters, nurseIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Helper IDs" placeholder="e.g. 1, 8" value={filters.helperIds} onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Room Numbers" placeholder="e.g. 301, 305" value={filters.roomNos} onChange={(e) => setFilters({ ...filters, roomNos: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
              </Grid>
            </Accordion.Panel>
          </Accordion.Item>

          {/* Time & Date Filters */}
          <Accordion.Item value="schedule">
            <Accordion.Control>Schedule & Timeline</Accordion.Control>
            <Accordion.Panel>
              <Grid>
                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="date" label="Surgery Date (From)" value={filters.surgeryDateStart} onChange={(e) => setFilters({ ...filters, surgeryDateStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="date" label="Surgery Date (To)" value={filters.surgeryDateEnd} onChange={(e) => setFilters({ ...filters, surgeryDateEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>

                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="time" label="Start Time (After)" value={filters.startTimeStart} onChange={(e) => setFilters({ ...filters, startTimeStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="time" label="Start Time (Before)" value={filters.startTimeEnd} onChange={(e) => setFilters({ ...filters, startTimeEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>

                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="time" label="End Time (After)" value={filters.endTimeStart} onChange={(e) => setFilters({ ...filters, endTimeStart: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="time" label="End Time (Before)" value={filters.endTimeEnd} onChange={(e) => setFilters({ ...filters, endTimeEnd: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>
              </Grid>
            </Accordion.Panel>
          </Accordion.Item>
        </Accordion>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">
            Clear
          </Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">
            Search Surgeries
          </Button>
        </Group>
      </Card>

      {/* RESULTS TABLE */}
      <Card withBorder shadow="sm" radius="md" p="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>
        </Group>

        <ScrollArea>
          <Table striped highlightOnHover verticalSpacing="sm" style={{ color: '#e2e8f0', minWidth: 900 }}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>ID</Table.Th>
                <Table.Th>Date</Table.Th>
                <Table.Th>Time (Start - End)</Table.Th>
                <Table.Th>Type</Table.Th>
                <Table.Th>Patient</Table.Th>
                <Table.Th>Surgeon</Table.Th>
                <Table.Th>Room</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((surgery) => (
                  <Table.Tr key={surgery.surgeryId}>
                    <Table.Td fw={600}>{surgery.surgeryId}</Table.Td>
                    <Table.Td>{surgery.surgeryDate}</Table.Td>
                    <Table.Td>
                      <Badge color="red" variant="light">
                        {surgery.startTime} - {surgery.endTime || 'Ongoing'}
                      </Badge>
                    </Table.Td>
                    <Table.Td>
                      <Text fw={500} size="sm">{surgery.surgeryType}</Text>
                    </Table.Td>
                    <Table.Td>{surgery.patientFirstName} {surgery.patientLastName}</Table.Td>
                    <Table.Td>{surgery.surgeonFirstName} {surgery.surgeonLastName}</Table.Td>
                    <Table.Td>
                      {surgery.roomNo ? <Badge color="gray" variant="outline">Rm {surgery.roomNo}</Badge> : 'N/A'}
                    </Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={7}>
                    <Text c="dimmed" ta="center" py="xl">No surgery records found matching your criteria.</Text>
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

export default SurgeryRecordSearch;