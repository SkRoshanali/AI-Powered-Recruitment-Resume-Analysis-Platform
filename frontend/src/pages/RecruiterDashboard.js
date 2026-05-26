import React, { useState } from 'react';
import { Container, Card, Form, Button, Alert, Table, Badge } from 'react-bootstrap';
import { createJob, getRankedCandidates } from '../services/api';

function RecruiterDashboard() {
  const [jobData, setJobData] = useState({
    title: '',
    description: '',
    location: '',
    jobType: 'FULL_TIME',
    experienceLevel: 'MID_LEVEL',
    salaryRange: '',
    minExperience: 0,
    maxExperience: 5,
    requiredSkills: []
  });
  const [skillInput, setSkillInput] = useState('');
  const [message, setMessage] = useState('');
  const [candidates, setCandidates] = useState([]);
  const [selectedJobId, setSelectedJobId] = useState('');

  const handleAddSkill = () => {
    if (skillInput.trim()) {
      setJobData({
        ...jobData,
        requiredSkills: [...jobData.requiredSkills, skillInput.trim()]
      });
      setSkillInput('');
    }
  };

  const handleRemoveSkill = (index) => {
    const newSkills = jobData.requiredSkills.filter((_, i) => i !== index);
    setJobData({ ...jobData, requiredSkills: newSkills });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createJob(jobData);
      setMessage('Job posted successfully!');
      setJobData({
        title: '',
        description: '',
        location: '',
        jobType: 'FULL_TIME',
        experienceLevel: 'MID_LEVEL',
        salaryRange: '',
        minExperience: 0,
        maxExperience: 5,
        requiredSkills: []
      });
    } catch (error) {
      setMessage('Error posting job');
    }
  };

  const handleViewCandidates = async () => {
    if (!selectedJobId) return;
    try {
      const response = await getRankedCandidates(selectedJobId);
      setCandidates(response.data);
    } catch (error) {
      console.error('Error loading candidates:', error);
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Recruiter Dashboard</h2>

      {message && <Alert variant="info">{message}</Alert>}

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>Post New Job</Card.Title>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Job Title</Form.Label>
              <Form.Control
                type="text"
                value={jobData.title}
                onChange={(e) => setJobData({ ...jobData, title: e.target.value })}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={4}
                value={jobData.description}
                onChange={(e) => setJobData({ ...jobData, description: e.target.value })}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Location</Form.Label>
              <Form.Control
                type="text"
                value={jobData.location}
                onChange={(e) => setJobData({ ...jobData, location: e.target.value })}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Job Type</Form.Label>
              <Form.Select
                value={jobData.jobType}
                onChange={(e) => setJobData({ ...jobData, jobType: e.target.value })}
              >
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
                <option value="CONTRACT">Contract</option>
                <option value="INTERNSHIP">Internship</option>
                <option value="REMOTE">Remote</option>
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Experience Level</Form.Label>
              <Form.Select
                value={jobData.experienceLevel}
                onChange={(e) => setJobData({ ...jobData, experienceLevel: e.target.value })}
              >
                <option value="ENTRY_LEVEL">Entry Level</option>
                <option value="MID_LEVEL">Mid Level</option>
                <option value="SENIOR_LEVEL">Senior Level</option>
                <option value="LEAD">Lead</option>
                <option value="EXECUTIVE">Executive</option>
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Salary Range</Form.Label>
              <Form.Control
                type="text"
                value={jobData.salaryRange}
                onChange={(e) => setJobData({ ...jobData, salaryRange: e.target.value })}
                placeholder="e.g., $80,000 - $120,000"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Required Skills</Form.Label>
              <div className="d-flex mb-2">
                <Form.Control
                  type="text"
                  value={skillInput}
                  onChange={(e) => setSkillInput(e.target.value)}
                  placeholder="Add a skill"
                />
                <Button onClick={handleAddSkill} className="ms-2">Add</Button>
              </div>
              <div>
                {jobData.requiredSkills.map((skill, index) => (
                  <Badge key={index} bg="primary" className="me-1">
                    {skill}
                    <span
                      style={{ cursor: 'pointer', marginLeft: '5px' }}
                      onClick={() => handleRemoveSkill(index)}
                    >
                      ×
                    </span>
                  </Badge>
                ))}
              </div>
            </Form.Group>

            <Button variant="primary" type="submit">Post Job</Button>
          </Form>
        </Card.Body>
      </Card>

      <Card>
        <Card.Body>
          <Card.Title>View Ranked Candidates</Card.Title>
          <Form.Group className="mb-3">
            <Form.Label>Job ID</Form.Label>
            <Form.Control
              type="number"
              value={selectedJobId}
              onChange={(e) => setSelectedJobId(e.target.value)}
              placeholder="Enter job ID"
            />
          </Form.Group>
          <Button onClick={handleViewCandidates}>Load Candidates</Button>

          {candidates.length > 0 && (
            <Table responsive hover className="mt-3">
              <thead>
                <tr>
                  <th>Rank</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Match Score</th>
                  <th>ATS Score</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {candidates.map((candidate) => (
                  <tr key={candidate.id}>
                    <td><strong>#{candidate.rankPosition}</strong></td>
                    <td>{candidate.applicantName}</td>
                    <td>{candidate.applicantEmail}</td>
                    <td className="match-score">{candidate.matchScore?.toFixed(1)}%</td>
                    <td>{candidate.atsScore?.toFixed(1)}</td>
                    <td><Badge bg="info">{candidate.status}</Badge></td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
}

export default RecruiterDashboard;
