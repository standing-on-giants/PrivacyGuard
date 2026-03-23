import React, { useState } from 'react';
import {
  Box,
  Card,
  Grid,
  TextInput,
  MultiSelect,
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

const DoctorSearch = () => {
  // State for the request payload
  const [filters, setFilters] = useState({
    fNames: '',
    lNames: '',
    genders: [],
    surgeonTypes: '',
    departmentNames: '',
    roomNumbers: ''
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Pagination state
  const [activePage, setPage] = useState(1);

  // Helper to convert "1, 2, 3" into [1, 2, 3]
  const parseIds = (idString) => {
    if (!idString) return null;
    const parsed = idString.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));
    return parsed.length > 0 ? parsed : null;
  };

  // Helper to convert "Cardiology, Neurology" into ["Cardiology", "Neurology"]
  const parseStrings = (str) => {
    if (!str) return null;
    const parsed = str.split(',').map(s => s.trim()).filter(Boolean);
    return parsed.length > 0 ? parsed : null;
  };

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setPage(1); // Reset to first page on new search

    // Build the exact DoctorSearchRequest DTO expected by Spring Boot
    const requestPayload = {
      fNames: parseStrings(filters.fNames),
      lNames: parseStrings(filters.lNames),
      genders: filters.genders.length > 0 ? filters.genders : null,
      surgeonTypes: parseStrings(filters.surgeonTypes),
      departmentNames: parseStrings(filters.departmentNames),
      roomNumbers: parseIds(filters.roomNumbers)
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const response = await fetch(`${baseUrl}/doctors/search`, {
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
      setError('Failed to fetch doctor records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilters({
      fNames: '', lNames: '', genders: [], surgeonTypes: '', departmentNames: '', roomNumbers: ''
    });
  };

  // Calculate Paginated Data
  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Title order={4} mb="lg" style={{ color: '#f8fafc' }}>Search Medical Staff</Title>
        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="First Names" placeholder="e.g. John, Sarah" value={filters.fNames} onChange={(e) => setFilters({ ...filters, fNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Last Names" placeholder="e.g. Smith, Doe" value={filters.lNames} onChange={(e) => setFilters({ ...filters, lNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <MultiSelect label="Gender" placeholder="Select genders" data={['M', 'F', 'O']} value={filters.genders} onChange={(val) => setFilters({ ...filters, genders: val })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>

          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Department Names" placeholder="e.g. Cardiology, Neurology" value={filters.departmentNames} onChange={(e) => setFilters({ ...filters, departmentNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Surgeon Types" placeholder="e.g. General, Orthopedic" value={filters.surgeonTypes} onChange={(e) => setFilters({ ...filters, surgeonTypes: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Room Numbers" placeholder="e.g. 101, 205" value={filters.roomNumbers} onChange={(e) => setFilters({ ...filters, roomNumbers: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">
            Clear
          </Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">
            Search Directory
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
                <Table.Th>ID</Table.Th>
                <Table.Th>Name</Table.Th>
                <Table.Th>Gender</Table.Th>
                <Table.Th>Contact No</Table.Th>
                <Table.Th>Department</Table.Th>
                <Table.Th>Surgeon Type</Table.Th>
                <Table.Th>Room</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((doc) => (
                  <Table.Tr key={doc.doctId}>
                    <Table.Td fw={600}>{doc.doctId}</Table.Td>
                    <Table.Td>{doc.fName} {doc.lName}</Table.Td>
                    <Table.Td>{doc.gender}</Table.Td>
                    <Table.Td>{doc.contactNo}</Table.Td>
                    <Table.Td>
                      <Badge color="violet" variant="light">{doc.deptName}</Badge>
                    </Table.Td>
                    <Table.Td>{doc.surgeonType ? doc.surgeonType : <Text c="dimmed" size="xs">N/A</Text>}</Table.Td>
                    <Table.Td>
                      {doc.roomNo ? <Badge color="gray" variant="outline">Rm {doc.roomNo}</Badge> : 'N/A'}
                    </Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={7}>
                    <Text c="dimmed" ta="center" py="xl">No doctors found matching your criteria.</Text>
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

export default DoctorSearch;