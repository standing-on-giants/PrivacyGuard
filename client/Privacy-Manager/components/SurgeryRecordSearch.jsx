import React, { useState } from 'react';
import {
  Box, Card, Grid, TextInput, Button, Table,
  Text, Badge, Group, Alert, ScrollArea, Title, Pagination,
  Accordion, Switch, ThemeIcon,
} from '@mantine/core';
import { IconShieldCheck, IconShieldOff } from '@tabler/icons-react';

const ITEMS_PER_PAGE = 10;

const SurgeryRecordSearch = () => {
  const [filters, setFilters] = useState({
    patientIds: '', surgeonIds: '', roomNos: '', nurseIds: '', helperIds: '',
    surgeryTypes: '', surgeryTypeKeyword: '',
    surgeryDateStart: '', surgeryDateEnd: '',
    startTimeStart: '', startTimeEnd: '',
    endTimeStart: '', endTimeEnd: ''
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

      const endpoint = privacyEnabled
        ? `${baseUrl}/surgery-records/search`
        : `${baseUrl}/raw/surgery-records/search`;

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
        body: JSON.stringify(requestPayload)
      });

      if (!response.ok) throw new Error(`Server returned ${response.status}`);
      setResults(await response.json());
    } catch (err) {
      setError('Failed to fetch surgery records. Please check your connection and permissions.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClear = () => setFilters({
    patientIds: '', surgeonIds: '', roomNos: '', nurseIds: '', helperIds: '',
    surgeryTypes: '', surgeryTypeKeyword: '',
    surgeryDateStart: '', surgeryDateEnd: '',
    startTimeStart: '', startTimeEnd: '',
    endTimeStart: '', endTimeEnd: ''
  });

  const totalPages = Math.ceil(results.length / ITEMS_PER_PAGE);
  const paginatedResults = results.slice((activePage - 1) * ITEMS_PER_PAGE, activePage * ITEMS_PER_PAGE);

  return (
    <Box>
      {/* FILTER PANEL */}
      <Card withBorder shadow="sm" radius="md" p="xl" mb="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>

        <Group justify="space-between" mb="lg" align="center">
          <Title order={4} style={{ color: '#f8fafc' }}>Search Surgery Records</Title>

          <Group gap="sm" align="center"
            style={{
              background: privacyEnabled ? 'rgba(239,68,68,0.1)' : 'rgba(34,197,94,0.1)',
              border: `1px solid ${privacyEnabled ? 'rgba(239,68,68,0.3)' : 'rgba(34,197,94,0.3)'}`,
              borderRadius: '8px', padding: '8px 14px', transition: 'all 0.3s ease',
            }}>
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
              onChange={(e) => { setPrivacyEnabled(!e.currentTarget.checked); setResults([]); setPage(1); }}
              color="green" size="md"
            />
            <Text size="xs" c="dimmed">Raw API</Text>
          </Group>
        </Group>

        {error && <Alert color="red" mb="md">{error}</Alert>}

        <Accordion variant="separated" defaultValue="details"
          styles={{
            item: { backgroundColor: 'transparent', borderColor: 'rgba(255,255,255,0.1)' },
            control: { color: '#f8fafc' }
          }}>

          {/* Core Details & IDs */}
          <Accordion.Item value="details">
            <Accordion.Control>Core Details & Staff IDs</Accordion.Control>
            <Accordion.Panel>
              <Grid>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Surgery Types (Exact)" placeholder="e.g. Appendectomy"
                    value={filters.surgeryTypes}
                    onChange={(e) => setFilters({ ...filters, surgeryTypes: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Surgery Keyword" placeholder="e.g. bypass"
                    value={filters.surgeryTypeKeyword}
                    onChange={(e) => setFilters({ ...filters, surgeryTypeKeyword: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 4 }}>
                  <TextInput label="Patient IDs" placeholder="e.g. 101, 105"
                    value={filters.patientIds}
                    onChange={(e) => setFilters({ ...filters, patientIds: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Surgeon IDs" placeholder="e.g. 1, 3"
                    value={filters.surgeonIds}
                    onChange={(e) => setFilters({ ...filters, surgeonIds: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Nurse IDs" placeholder="e.g. 2, 5"
                    value={filters.nurseIds}
                    onChange={(e) => setFilters({ ...filters, nurseIds: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Helper IDs" placeholder="e.g. 1, 8"
                    value={filters.helperIds}
                    onChange={(e) => setFilters({ ...filters, helperIds: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
                <Grid.Col span={{ base: 12, sm: 6, md: 3 }}>
                  <TextInput label="Room Numbers" placeholder="e.g. 301, 305"
                    value={filters.roomNos}
                    onChange={(e) => setFilters({ ...filters, roomNos: e.currentTarget.value })}
                    styles={{ label: { color: '#cbd5e1' } }} />
                </Grid.Col>
              </Grid>
            </Accordion.Panel>
          </Accordion.Item>

          {/* Schedule & Timeline */}
          <Accordion.Item value="schedule">
            <Accordion.Control>Schedule & Timeline</Accordion.Control>
            <Accordion.Panel>
              <Grid>
                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="date" label="Surgery Date (From)" value={filters.surgeryDateStart}
                      onChange={(e) => setFilters({ ...filters, surgeryDateStart: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="date" label="Surgery Date (To)" value={filters.surgeryDateEnd}
                      onChange={(e) => setFilters({ ...filters, surgeryDateEnd: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>
                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="time" label="Start Time (After)" value={filters.startTimeStart}
                      onChange={(e) => setFilters({ ...filters, startTimeStart: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="time" label="Start Time (Before)" value={filters.startTimeEnd}
                      onChange={(e) => setFilters({ ...filters, startTimeEnd: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>
                <Grid.Col span={{ base: 12, md: 4 }}>
                  <Group grow>
                    <TextInput type="time" label="End Time (After)" value={filters.endTimeStart}
                      onChange={(e) => setFilters({ ...filters, endTimeStart: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                    <TextInput type="time" label="End Time (Before)" value={filters.endTimeEnd}
                      onChange={(e) => setFilters({ ...filters, endTimeEnd: e.currentTarget.value })}
                      styles={{ label: { color: '#cbd5e1' } }} />
                  </Group>
                </Grid.Col>
              </Grid>
            </Accordion.Panel>
          </Accordion.Item>
        </Accordion>

        <Group justify="flex-end" mt="xl">
          <Button onClick={handleClear} variant="subtle" color="gray">Clear</Button>
          <Button onClick={handleSearch} loading={loading} color="cyan">Search Surgeries</Button>
        </Group>
      </Card>

      {/* RESULTS TABLE */}
      <Card withBorder shadow="sm" radius="md" p="xl"
        style={{ background: 'rgba(30, 41, 59, 0.5)', borderColor: 'rgba(255,255,255,0.1)' }}>
        <Group justify="space-between" mb="md">
          <Title order={4} style={{ color: '#f8fafc' }}>Results ({results.length})</Title>
          <Badge
            color={privacyEnabled ? 'red' : 'green'} variant="light" size="lg"
            leftSection={privacyEnabled
              ? <IconShieldCheck size={12} stroke={1.5} />
              : <IconShieldOff size={12} stroke={1.5} />}>
            {privacyEnabled ? '/api/surgery-records/search' : '/api/raw/surgery-records/search'}
          </Badge>
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
                      {surgery.roomNo
                        ? <Badge color="gray" variant="outline">Rm {surgery.roomNo}</Badge>
                        : 'N/A'}
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

        {totalPages > 1 && (
          <Group justify="center" mt="xl">
            <Pagination total={totalPages} value={activePage} onChange={setPage} color="cyan" withEdges />
          </Group>
        )}
      </Card>
    </Box>
  );
};

export default SurgeryRecordSearch;