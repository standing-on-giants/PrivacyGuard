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

const HelperSearch = () => {
  // State for the request payload
  const [filters, setFilters] = useState({
    helperIds: '',
    fNames: '',
    lNames: '',
    genders: [],
    departmentNames: ''
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

    // Build the exact HelperSearchRequest DTO expected by Spring Boot
    const requestPayload = {
      helperIds: parseIds(filters.helperIds),
      fNames: parseStrings(filters.fNames),
      lNames: parseStrings(filters.lNames),
      genders: filters.genders.length > 0 ? filters.genders : null,
      departmentNames: parseStrings(filters.departmentNames)
    };

    try {
      const token = localStorage.getItem('jwtToken');
      const baseUrl = import.meta.env.VITE_API_BASE_URL;

      const response = await fetch(`${baseUrl}/helpers/search`, {
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
      setError('Failed to fetch helper records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => {
    setFilters({
      helperIds: '', fNames: '', lNames: '', genders: [], departmentNames: ''
    });
  };

  // Calculate Paginated Data
  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl" style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Title order={4} mb="lg" style={{ color: '#f8fafc' }}>Search Support Staff (Helpers)</Title>
        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Grid>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Helper IDs" placeholder="e.g. 1, 5, 12" value={filters.helperIds} onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="First Names" placeholder="e.g. John, Sarah" value={filters.fNames} onChange={(e) => setFilters({ ...filters, fNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Last Names" placeholder="e.g. Smith, Doe" value={filters.lNames} onChange={(e) => setFilters({ ...filters, lNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>

          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <MultiSelect label="Gender" placeholder="Select genders" data={['M', 'F', 'O']} value={filters.genders} onChange={(val) => setFilters({ ...filters, genders: val })} styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 8 }}>
            <TextInput label="Department Names" placeholder="e.g. Maintenance, Janitorial, Transport" value={filters.departmentNames} onChange={(e) => setFilters({ ...filters, departmentNames: e.currentTarget.value })} styles={{ label: { color: '#cbd5e1' } }} />
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
          <Table striped highlightOnHover verticalSpacing="sm" style={{ color: '#e2e8f0', minWidth: 600 }}>
            <Table.Thead>
              <Table.Tr>
                <Table.Th>ID</Table.Th>
                <Table.Th>Name</Table.Th>
                <Table.Th>Gender</Table.Th>
                <Table.Th>Contact No</Table.Th>
                <Table.Th>Department</Table.Th>
              </Table.Tr>
            </Table.Thead>
            <Table.Tbody>
              {paginatedResults.length > 0 ? (
                paginatedResults.map((helper) => (
                  <Table.Tr key={helper.helperId}>
                    <Table.Td fw={600}>{helper.helperId}</Table.Td>
                    <Table.Td>{helper.fName} {helper.lName}</Table.Td>
                    <Table.Td>{helper.gender}</Table.Td>
                    <Table.Td>{helper.contactNo || <Text c="dimmed" size="xs">N/A</Text>}</Table.Td>
                    <Table.Td>
                      <Badge color="orange" variant="light">{helper.deptName}</Badge>
                    </Table.Td>
                  </Table.Tr>
                ))
              ) : (
                <Table.Tr>
                  <Table.Td colSpan={5}>
                    <Text c="dimmed" ta="center" py="xl">No helpers found matching your criteria.</Text>
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

export default HelperSearch;