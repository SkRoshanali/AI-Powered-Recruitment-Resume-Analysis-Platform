import React, { useState, useEffect } from 'react';
import { Container, Card, Table, Badge, Button, Alert } from 'react-bootstrap';
import { getMyApplications, uploadResume } from '../services/api';

function Dashboard() {
  const [applications, setApplications] = useState([]);
  const [resumeAnalysis, setResumeAnalysis] = useState(null);
  const [message, setMessage] = useState('');

  useEffect(() => {
    loadApplications();
  }, []);

  const loadApplications = async () => {
    try {
      const response = await getMyApplications();
      setApplications(response.data.content);
    } catch (error) {
      console.error('Error loading applications:', error);
    }
  };

  const handleResumeUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      const response = await uploadResume(file);
      setResumeAnalysis(response.data);
      setMessage('Resume analyzed successfully!');
    } catch (error) {
      console.error('Error uploading resume:', error);
      setMessage('Error analyzing resume');
    }
  };

  const getStatusBadge = (status) => {
    const variants = {
      APPLIED: 'primary',
      UNDER_REVIEW: 'info',
      SHORTLISTED: 'success',
      INTERVIEW_SCHEDULED: 'warning',
      REJECTED: 'danger',
      ACCEPTED: 'success'
    };
    return <Badge bg={variants[status] || 'secondary'}>{status}</Badge>;
  };

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Job Seeker Dashboard</h2>

      {message && <Alert variant="info">{message}</Alert>}

      <Card className="mb-4">
        <Card.Body>
          <Card.Title>Upload Resume for AI Analysis</Card.Title>
          <input
            type="file"
            accept=".pdf"
            onChange={handleResumeUpload}
            className="form-control"
          />
          {resumeAnalysis && (
            <div className="mt-3">
              <h5>Analysis Results:</h5>
              <p><strong>ATS Score:</strong> <span className="ats-score">{resumeAnalysis.atsScore?.toFixed(1)}%</span></p>
              <p><strong>Skills Detected:</strong></p>
              <div>
                {resumeAnalysis.skills?.map((skill, index) => (
                  <Badge key={index} bg="success" className="me-1">{skill}</Badge>
                ))}
              </div>
            </div>
          )}
        </Card.Body>
      </Card>

      <Card>
        <Card.Body>
          <Card.Title>My Applications</Card.Title>
          <Table responsive hover>
            <thead>
              <tr>
                <th>Job Title</th>
                <th>Status</th>
                <th>Match Score</th>
                <th>ATS Score</th>
                <th>Rank</th>
                <th>Applied Date</th>
              </tr>
            </thead>
            <tbody>
              {applications.map((app) => (
                <tr key={app.id}>
                  <td>{app.jobTitle}</td>
                  <td>{getStatusBadge(app.status)}</td>
                  <td>
                    {app.matchScore ? (
                      <span className="match-score">{app.matchScore.toFixed(1)}%</span>
                    ) : 'N/A'}
                  </td>
                  <td>{app.atsScore ? app.atsScore.toFixed(1) : 'N/A'}</td>
                  <td>{app.rankPosition || 'N/A'}</td>
                  <td>{new Date(app.appliedAt).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </Container>
  );
}

export default Dashboard;
