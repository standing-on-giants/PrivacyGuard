import React, { useState } from 'react';
import {
  Box, Card, Grid, TextInput, MultiSelect, Button, Table,
  Text, Badge, Group, Alert, ScrollArea, Title, Pagination,
  Switch, ThemeIcon,
} from '@mantine/core';
import { IconShieldCheck, IconShieldOff } from '@tabler/icons-react';

const ITEMS_PER_PAGE = 10;

const HelperSearch = () => {
  const [filters, setFilters] = useState({
    helperIds: '', fNames: '', lNames: '', genders: [], departmentNames: ''
  });

  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [activePage, setPage] = useState(1);
  const [privacyEnabled, setPrivacyEnabled] = useState(true);

  const parseIds = (idString) => {
    if (!idString) return null;
    const parsed = idString.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));
    return parsed.length > 0 ? parsed : null;
  };

  const parseStrings = (str) => {
    if (!str) return null;
    const parsed = str.split(',').map(s => s.trim()).filter(Boolean);
    return parsed.length > 0 ? parsed : null;
  };

  const handleSearch = async () => {
    setLoading(true);
    setError('');
    setPage(1);

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

      const endpoint = privacyEnabled
        ? `${baseUrl}/helpers/search`
        : `${baseUrl}/raw/helpers/search`;

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(requestPayload)
      });

      if (!response.ok) throw new Error(`Server returned ${response.status}`);
      setResults(await response.json());
    } catch (err) {
      setError('Failed to fetch helper records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => setFilters({
    helperIds: '', fNames: '', lNames: '', genders: [], departmentNames: ''
  });

  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>

        <Group justify="space-between" mb="lg" align="center">
          <Title order={4} style={{ color: '#f8fafc' }}>Search Support Staff (Helpers)</Title>

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
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Helper IDs" placeholder="e.g. 1, 5, 12" value={filters.helperIds}
              onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="First Names" placeholder="e.g. John, Sarah" value={filters.fNames}
              onChange={(e) => setFilters({ ...filters, fNames: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <TextInput label="Last Names" placeholder="e.g. Smith, Doe" value={filters.lNames}
              onChange={(e) => setFilters({ ...filters, lNames: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
            <MultiSelect label="Gender" placeholder="Select genders" data={['M', 'F', 'O']}
              value={filters.genders}
              onChange={(val) => setFilters({ ...filters, genders: val })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
          <Grid.Col span={{ base: 12, sm: 6, md: 8 }}>
            <TextInput label="Department Names" placeholder="e.g. Maintenance, Janitorial"
              value={filters.departmentNames}
              onChange={(e) => setFilters({ ...filters, departmentNames: e.currentTarget.value })}
              styles={{ label: { color: '#cbd5e1' } }} />
          </Grid.Col>
        </Grid>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">Clear</Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">Search Directory</Button>
        </Group>
      </Card>

      <Card withBorder shadow="sm" radius="md" p="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>
          <Badge color={privacyEnabled ? 'red' : 'green'} variant="light" size="lg"
            leftSection={privacyEnabled ? <IconShieldCheck size={12} stroke={1.5} /> : <IconShieldOff size={12} stroke={1.5} />}>
            {privacyEnabled ? '/api/helpers/search' : '/api/raw/helpers/search'}
          </Badge>
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
                    <Table.Td><Badge color="orange" variant="light">{helper.deptName}</Badge></Table.Td>
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

        {totalPages > 1 && (
          <Group justify="center" mt="xl">
            <Pagination total={totalPages} value={activePage} onChange={setPage} color="cyan" withEdges />
          </Group>
        )}
      </Card>
    </Box>
  );
};

export default HelperSearch;